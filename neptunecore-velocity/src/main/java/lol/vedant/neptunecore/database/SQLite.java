package lol.vedant.neptunecore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.core.data.Friend;
import lol.vedant.core.data.FriendRequest;
import lol.vedant.core.data.Setting;
import lol.vedant.core.data.UserSettings;
import lol.vedant.core.database.SQLiteUtils;
import lol.vedant.neptunecore.NeptuneCore;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public class SQLite implements Database {

    Path databaseFile = NeptuneCore.getInstance().getDataFolder();
    SQLiteUtils utils;
    HikariDataSource dataSource;

    @Override
    public void init() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:sqlite:" + databaseFile.toAbsolutePath() + "/data.db");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(1);
        config.setPoolName("NeptuneCore-Pool");

        String neptune_players = """
                CREATE TABLE IF NOT EXISTS neptune_players (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT,
                    uuid TEXT UNIQUE
                );
                """;
        String neptune_player_settings = """
                CREATE TABLE IF NOT EXISTS neptune_player_settings (
                    player_id INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    value TEXT,
                    PRIMARY KEY (player_id, type)
                );
                """;
        String neptune_friends = """
                CREATE TABLE IF NOT EXISTS neptune_friends (
                    player_1 INTEGER NOT NULL,
                    player_2 INTEGER NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    CHECK (player_1 < player_2),
                    PRIMARY KEY (player_1, player_2)
                );
                """;
        String neptune_friend_requests = """
                CREATE TABLE IF NOT EXISTS neptune_friend_requests (
                    sender INTEGER NOT NULL,
                    receiver INTEGER NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (sender, receiver)
                );
                """;

        dataSource = new HikariDataSource(config);
        this.utils = new SQLiteUtils(dataSource);

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
        String sql = "SELECT * FROM neptune_players WHERE uuid = ? LIMIT 1";

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
        String sql = "INSERT INTO neptune_player_settings (player_id, type, value) VALUES (?, ?, ?) ON CONFLICT(player_id, type) DO UPDATE SET value = excluded.value";
        utils.update(sql, id, type.toString(), value);
    }

    @Override
    public int getPlayerId(UUID uuid) {
        String sql = "SELECT id FROM neptune_players WHERE uuid = ?";
        return utils.query(sql, rs -> {
            try {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }, uuid);
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
        }, username);
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
                            String createdAt = rs.getString("created_at");
                            Instant instant = LocalDateTime
                                    .parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant();

                            friends.add(
                                    new Friend(
                                            rs.getString("friend_name"),
                                            instant
                                    )
                            );
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
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
                        e.printStackTrace();
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
