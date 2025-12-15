package lol.vedant.neptunecore.database;

import lol.vedant.core.data.Friend;
import lol.vedant.core.data.FriendRequest;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@SuppressWarnings("ALL")
public class SQLite implements Database{
    private File dataDirectory;
    private Connection connection;

    public SQLite(String dataDirectory) {
        this.dataDirectory = new File(dataDirectory);

        if(!this.dataDirectory.exists()) {
            this.dataDirectory.mkdirs();
        }
        File dataFile = new File(dataDirectory, "data.db");

        if(!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String url = "jdbc:sqlite:" + dataFile;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
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

        try {
            Statement stmt = connection.createStatement();

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
    public Map<String, Object> getPlayerSettings(int playerId) {
        String sql = "SELECT settings FROM neptune_settings WHERE player_id = ?;";
        Map<String, Object> settings = new HashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString("settings");
                    JSONObject jsonObject = new JSONObject(json);
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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

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
    public boolean addFriendship(UUID player1, UUID player2) {
        String sql = """
            INSERT INTO neptune_friends (friend1_id, friend2_id)
            VALUES (
                (SELECT player_id FROM neptune_players WHERE player_uuid = ?),
                (SELECT player_id FROM neptune_players WHERE player_uuid = ?)
            )
            """;

        List<UUID> ordered = orderUuids(player1, player2);

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, ordered.get(0).toString());
            ps.setString(2, ordered.get(1).toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeFriendship(UUID player1, UUID player2) {
        String sql = """
            DELETE FROM neptune_friends 
            WHERE (friend1_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?)
            AND friend2_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?))
            OR (friend1_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?)
            AND friend2_id = (SELECT player_id FROM neptune_players WHERE player_uuid = ?))
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, player1.toString());
            ps.setString(2, player2.toString());
            ps.setString(3, player2.toString());
            ps.setString(4, player1.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createFriendRequest(UUID requester, UUID receiver) {
        String sql = """
            INSERT INTO neptune_friend_requests (requester_id, receiver_id)
            VALUES (
                (SELECT player_id FROM neptune_players WHERE player_uuid = ?),
                (SELECT player_id FROM neptune_players WHERE player_uuid = ?)
            )
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, requester.toString());
            ps.setString(2, receiver.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<FriendRequest> getFriendRequests(UUID receiverUuid) {
        String sql = """
        SELECT sender.player_name AS sender_username,
               receiver.player_name AS receiver_username
        FROM neptune_friend_requests fr
        JOIN neptune_players sender ON fr.sender_id = sender.player_uuid
        JOIN neptune_players receiver ON fr.receiver_id = receiver.player_uuid
        WHERE fr.receiver_id = ?;
        """;

        List<FriendRequest> requests = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, receiverUuid.toString());

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
    public List<Friend> getFriends(UUID playerUuid) {
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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, playerUuid.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Determine which friend is the other player
                    String friendName = rs.getString("friend1_name").equals(getPlayerName(playerUuid))
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

    private List<UUID> orderUuids(UUID uuid1, UUID uuid2) {
        return uuid1.compareTo(uuid2) < 0 ?
                List.of(uuid1, uuid2) :
                List.of(uuid2, uuid1);
    }

    private String getPlayerName(UUID playerUuid) {
        String sql = "SELECT player_name FROM neptune_players WHERE player_uuid = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, playerUuid.toString());

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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

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

    private void createIndexIfNotExists(Statement stmt, String tableName, String indexName, String columnName) throws SQLException {
        // Check if the index exists using PRAGMA index_list
        String checkIndexSql = String.format(
                "SELECT 1 FROM pragma_index_list('%s') WHERE name = '%s';",
                tableName, indexName
        );

        try (ResultSet rs = stmt.executeQuery(checkIndexSql)) {
            if (!rs.next()) {
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
