package lol.vedant.neptunecore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.core.data.*;
import lol.vedant.core.database.MySQLUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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
    private MySQLUtils utils;

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

    @Override
    public void init() {
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
        this.utils = new MySQLUtils(dataSource);

        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String neptune_players = "CREATE TABLE IF NOT EXISTS neptune_players (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(16) UNIQUE," +
                "uuid VARCHAR(36)" +
                ");";

        String neptune_player_settings = "CREATE TABLE IF NOT EXISTS neptune_player_settings (" +
                "player_id INT," +
                "type VARCHAR(255)," +
                "value VARCHAR(255)," +
                "UNIQUE (player_id, type)" +
                ");";

        String neptune_friends = "CREATE TABLE IF NOT EXISTS neptune_friends (" +
                "player_1 INT NOT NULL," +
                "player_2 INT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "CHECK (player_1 < player_2)," +
                "UNIQUE (player_1, player_2)" +
                ");";

        String neptune_friend_requests = "CREATE TABLE IF NOT EXISTS neptune_friend_requests (" +
                "sender INT NOT NULL," +
                "receiver int NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE (sender, receiver)" +
                ");";

        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute(neptune_players);
            stmt.execute(neptune_friend_requests);
            stmt.execute(neptune_friends);
            stmt.execute(neptune_player_settings);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        try(Connection conn = dataSource.getConnection()) {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(UUID player) {
        String sql = "SELECT 1 FROM neptune_players WHERE uuid = ?";

        return utils.query(
                sql,
                rs -> {
                    try {
                        return rs.next();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return false;
                },
                player.toString()
        );
    }

    @Override
    public void createPlayer(String username, UUID uuid) {
        String sql = "INSERT INTO neptune_players (uuid, username) VALUES (?, ?)";
        utils.update(sql, uuid, username);
    }

    @Override
    public UserSettings getSettings(int playerId) {
        String sql = "SELECT type, value FROM neptune_player_settings WHERE player_id = ?";

        return utils.query(
                sql,
                rs -> {
                    UserSettings settings = new UserSettings();
                    HashMap<Setting, String> map = new HashMap<>();

                    try {
                        while (rs.next()) {
                            Setting type = Setting.valueOf(rs.getString("type"));
                            String value = rs.getString("value");
                            map.put(type, value);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    settings.setUserSettings(map);
                    return settings;
                },
                playerId
        );
    }

    @Override
    public void saveSetting(int id, Setting type, String value) {
        String sql = "INSERT INTO neptune_player_settings (id, type, value) (?, ?, ?) ON DUPLICATE KEY UPDATE value = ?";
        utils.update(sql, id, type.toString(), value);
    }

    @Override
    public int getPlayerId(UUID uuid) {
        String sql = "SELECT id FROM neptune_players WHERE uuid = ?";
        return utils.query(sql, rs -> {
            try {
                if(rs.next()) {
                    return rs.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        });
    }

    @Override
    public int getPlayerId(String username) {
        String sql = "SELECT id FROM neptune_players WHERE username = ?";
        return utils.query(sql, rs -> {
            try {
                if(rs.next()) {
                    return rs.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        });
    }

    @Override
    public String getNameById(int playerId) {
        String sql = "SELECT username FROM neptune_players WHERE id = ?";
        return utils.query(sql, rs -> {
            try {
                 if(rs.next()) {
                     return rs.getString("username");
                 }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, playerId);
    }

    @Override
    public String getNameByUUID(UUID uuid) {
        String sql = "SELECT username FROM neptune_players WHERE uuid = ?";
        return utils.query(sql, rs -> {
            try {
                if(rs.next()) {
                    return rs.getString("username");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, uuid.toString());
    }

    @Override
    public List<Friend> getFriends(int playerId) {
        String sql = """
        SELECT
            f.created_at,
            p.username AS friend_name
        FROM neptune_friends f
        JOIN neptune_players p
          ON p.id = CASE
              WHEN f.player_1 = ? THEN f.player_2
              ELSE f.player_1
          END
        WHERE f.player_1 = ? OR f.player_2 = ?
        """;

        return utils.query(
                sql,
                rs -> {
                    List<Friend> friends = new ArrayList<>();
                    try {
                        while (rs.next()) {
                            friends.add(
                                    new Friend(
                                            rs.getString("friend_name"),
                                            rs.getTimestamp("created_at").toInstant()
                                    )
                            );
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return friends;
                },
                playerId, playerId, playerId
        );
    }

    @Override
    public void removeFriend(int playerId, int targetId) {
        String sql = "DELETE FROM neptune_friends WHERE player_1 = ? AND player_2 = ? OR player_1 = ? AND player_2 = ?";
        utils.update(sql, playerId, targetId, targetId, playerId);
    }

    @Override
    public void addFriend(int playerId, int targetId) {
        String sql = "INSERT INTO neptune_friends (player_1, player_2) VALUES (?, ?)";
        int p1 = Math.min(playerId, targetId);
        int p2 = Math.max(playerId, targetId);
        utils.update(sql, p1, p2);
    }

    @Override
    public List<FriendRequest> getFriendRequests(int playerId) {

        String sql = """
        SELECT
            s.username AS sender_name,
            r.username AS receiver_name
        FROM neptune_friend_requests fr
        JOIN neptune_players s ON s.id = fr.sender
        JOIN neptune_players r ON r.id = fr.receiver
        WHERE fr.receiver = ?
        """;

        return utils.query(
                sql,
                rs -> {
                    List<FriendRequest> requests = new ArrayList<>();
                    try {
                        while (rs.next()) {
                            requests.add(
                                    new FriendRequest(
                                            rs.getString("sender_name"),
                                            rs.getString("receiver_name")
                                    )
                            );
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return requests;
                },
                playerId
        );
    }


    @Override
    public void createFriendRequest(int sender, int receiver) {
        String sql = "INSERT INTO neptune_friend_requests (sender, receiver) VALUES (?, ?)";
        utils.update(sql, sender, receiver);

    }

    @Override
    public void removeFriendRequest(int sender, int receiver) {
        String sql = "DELETE FROM neptune_friend_requests WHERE sender = ? AND receiver = ?";
        utils.update(sql, sender, receiver);
    }
}
