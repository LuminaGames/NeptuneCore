package lol.vedant.neptunecore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.core.data.DatabaseSettings;
import lol.vedant.core.data.Friend;
import lol.vedant.core.data.FriendRequest;
import lol.vedant.core.data.UserSettings;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class MySQL implements Database {

    private HikariDataSource dataSource;
    private final String host;
    private final String database;
    private final String user;
    private final String pass;
    private final int port;
    private final boolean ssl;
    private final boolean certificateVerification;
    private final int poolSize;
    private final int maxLifetime;

    public MySQL(DatabaseSettings settings) {
        this.host = settings.getHost();
        this.database = settings.getDatabase();
        this.user = settings.getUser();
        this.pass = settings.getPassword();
        this.port = settings.getPort();
        this.ssl = settings.isSsl();
        this.certificateVerification = settings.isCertificateVerification();
        this.poolSize = settings.getPoolSize();
        this.maxLifetime = settings.getMaxLifetime();
    }

    public void connect() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName("NeptuneCore-Pool");

        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setMaxLifetime(maxLifetime * 1000L);

        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pass);

        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(ssl));
        if (!certificateVerification) {
            hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
        }

        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");

        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        // Recover if connection gets interrupted
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        dataSource = new HikariDataSource(hikariConfig);

        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        String playersTable = "CREATE TABLE IF NOT EXISTS neptune_players ("
                + "player_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "player_uuid CHAR(36) UNIQUE NOT NULL,"
                + "player_name VARCHAR(16) NOT NULL"
                + ");";

        String settingsTable = "CREATE TABLE IF NOT EXISTS neptune_settings (" +
                "player_id INT NOT NULL PRIMARY KEY," +
                "settings JSON NOT NULL," +
                "FOREIGN KEY (player_id) REFERENCES neptune_players(player_id)" +
                ");";

        String friendsTable = "CREATE TABLE IF NOT EXISTS neptune_friends ("
                + "friend1_id INT NOT NULL,"
                + "friend2_id INT NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "PRIMARY KEY (friend1_id, friend2_id),"
                + "FOREIGN KEY (friend1_id) REFERENCES neptune_players(player_id),"
                + "FOREIGN KEY (friend2_id) REFERENCES neptune_players(player_id),"
                + "CHECK (friend1_id < friend2_id)"
                + ");";

        String friendRequestsTable = "CREATE TABLE IF NOT EXISTS neptune_friend_requests ("
                + "requester_id INT NOT NULL,"
                + "receiver_id INT NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "PRIMARY KEY (requester_id, receiver_id),"
                + "FOREIGN KEY (requester_id) REFERENCES neptune_players(player_id),"
                + "FOREIGN KEY (receiver_id) REFERENCES neptune_players(player_id)"
                + ");";

        String lastInteractionTable = "CREATE TABLE IF NOT EXISTS neptune_last_interactions ("
                + "player_id INT PRIMARY KEY,"
                + "last_interacted_with INT NOT NULL,"
                + "FOREIGN KEY (player_id) REFERENCES neptune_players(player_id),"
                + "FOREIGN KEY (last_interacted_with) REFERENCES neptune_players(player_id)"
                + ");";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(playersTable);
            stmt.execute(settingsTable);
            stmt.execute(friendsTable);
            stmt.execute(friendRequestsTable);
            stmt.execute(lastInteractionTable);

            // Create indexes
            createIndexIfNotExists(stmt, "neptune_players", "idx_neptune_players_uuid", "player_uuid");
            createIndexIfNotExists(stmt, "neptune_players", "idx_neptune_players_name", "player_name");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPlayer(UUID uuid, String name) {
        String sql = "INSERT INTO neptune_players (player_uuid, player_name) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getPlayerSettings(int playerId) {
        String sql = "SELECT settings FROM neptune_settings WHERE player_id = ?;";
        Map<String, Object> settings = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString("settings");
                    JSONObject jsonObject = new JSONObject(json);
                    // Convert JSONObject to Map
                    settings = jsonObject.toMap();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }

    @Override
    public void updatePlayerSettings(int playerId, Map<String, Object> settings) {
        String sql = """
        INSERT INTO neptune_settings (player_id, settings)
        VALUES (?, ?)
        ON DUPLICATE KEY UPDATE settings = ?;
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            JSONObject jsonObject = new JSONObject(settings);
            String json = jsonObject.toString();

            ps.setInt(1, playerId);
            ps.setString(2, json);
            ps.setString(3, json);

            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addFriendship(int player1, int player2) {
        String sql = """
            INSERT INTO neptune_friends (friend1_id, friend2_id)
            VALUES (
                (SELECT player_id FROM neptune_players WHERE player_uuid = ?),
                (SELECT player_id FROM neptune_players WHERE player_uuid = ?)
            )
            """;


        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, player1);
            ps.setInt(2, player2);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeFriendship(int player1, int player2) {
        String sql = """
            DELETE FROM neptune_friends 
            WHERE (friend1_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?)
            AND friend2_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?))
            OR (friend1_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?)
            AND friend2_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?))
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, player1);
            ps.setInt(2, player2);
            ps.setInt(3, player2);
            ps.setInt(4, player1);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createFriendRequest(int requester, int receiver) {
        String sql = """
            INSERT INTO neptune_friend_requests (requester_id, receiver_id)
            VALUES (
                (SELECT player_id FROM neptune_players WHERE player_id = ?),
                (SELECT player_id FROM neptune_players WHERE player_id = ?)
            )
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requester);
            ps.setInt(2, receiver);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeFriendRequest(int player1, int player2) {
        String sql = "DELETE FROM neptune_friend_requests WHERE (requester_id = ? AND receiver_id = ?) OR (requester_id = ? AND receiver_id = ?)";
        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, player1);
            ps.setInt(2, player2);

            ps.setInt(3, player2);
            ps.setInt(4, player1);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FriendRequest> getFriendRequests(int receiverId) {
        String sql = """
        SELECT sender.player_name AS sender_username,
               receiver.player_name AS receiver_username
        FROM neptune_friend_requests fr
        JOIN neptune_players sender ON fr.sender_id = sender.player_uuid
        JOIN neptune_players receiver ON fr.receiver_id = receiver.player_uuid
        WHERE fr.receiver_id = ?;
        """;

        List<FriendRequest> requests = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, receiverId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(new FriendRequest(
                            rs.getString("sender_username"),
                            rs.getString("receiver_username")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    @Override
    public List<Friend> getFriends(int playerId) {
        String sql = """
        SELECT p1.player_name AS friend1_name,
               p2.player_name AS friend2_name,
               f.created_at
        FROM neptune_friends f
        JOIN neptune_players p1 ON f.friend1_id = p1.player_id
        JOIN neptune_players p2 ON f.friend2_id = p2.player_id
        JOIN neptune_players p ON (p.player_id = f.friend1_id OR p.player_id = f.friend2_id)
        WHERE p.player_uuid = ?;
        """;

        List<Friend> friends = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Determine which friend is the other player
                    String friendName = rs.getString("friend1_name").equals(getPlayerName(playerId))
                            ? rs.getString("friend2_name")
                            : rs.getString("friend1_name");

                    friends.add(new Friend(
                            friendName,
                            rs.getTimestamp("created_at").toInstant()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

    private String getPlayerName(int playerId) {
        String sql = "SELECT player_name FROM neptune_players WHERE player_id = ?;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("player_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Unknown Player";
    }

    @Override
    public int getPlayerId(UUID uuid) {
        String sql = "SELECT player_uuid FROM neptune_players WHERE player_id = ?;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("player_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int getPlayerId(String username) {
        String sql = "SELECT player_id FROM neptune_players WHERE player_name = ?";

        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt("player_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void createIndexIfNotExists(Statement stmt, String tableName, String indexName, String columnName) throws SQLException {
        // Check if the index exists
        String checkIndexSql = String.format(
                "SELECT COUNT(*) FROM information_schema.statistics " +
                        "WHERE table_schema = DATABASE() " +
                        "AND table_name = '%s' " +
                        "AND index_name = '%s';",
                tableName, indexName
        );

        try (ResultSet rs = stmt.executeQuery(checkIndexSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                // Index does not exist, create it
                String createIndexSql = String.format(
                        "CREATE INDEX %s ON %s(%s);",
                        indexName, tableName, columnName
                );
                stmt.execute(createIndexSql);
                System.out.println("Created index: " + indexName);
            } else {
                System.out.println("Index already exists: " + indexName);
            }
        }
    }


}
