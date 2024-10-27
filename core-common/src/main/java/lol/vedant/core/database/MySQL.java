package lol.vedant.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.core.data.DatabaseSettings;
import lol.vedant.core.data.Friend;
import lol.vedant.core.data.UserSettings;

import java.sql.*;
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
        String friendsTableSql = "CREATE TABLE neptune_friends (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "player1_username VARCHAR(255) NOT NULL," +
                "player2_username VARCHAR(255) NOT NULL," +
                "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String pendingRequestsTableSql = "CREATE TABLE neptune_pending_requests (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "sender_username VARCHAR(255) NOT NULL," +
                "receiver_username VARCHAR(255) NOT NULL," +
                "date_sent TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String userSettingsTableSql = "CREATE TABLE IF neptune_user_settings (" +
                "uuid VARCHAR(255) PRIMARY KEY," +
                "username VARCHAR(255) NOT NULL," +
                "personal_message_enabled BOOLEAN DEFAULT TRUE," +
                "friend_request_enabled BOOLEAN DEFAULT TRUE," +
                "party_invite_enabled BOOLEAN DEFAULT TRUE," +
                "hidden_staff BOOLEAN DEFAULT FALSE" +
                ");";

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(friendsTableSql);
            statement.executeUpdate(pendingRequestsTableSql);
            statement.executeUpdate(userSettingsTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void insert(String name, UUID uuid) {
        String sql = "INSERT INTO neptune_user_settings (uuid, username) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(UUID player) {
        String sql = "SELECT * FROM neptune_user_settings WHERE uuid = ?";

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player.toString());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public UserSettings getUserSettings(UUID player) {
        String sql = "SELECT * FROM neptune_user_settings WHERE uuid = ?";
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player.toString());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return new UserSettings(
                        rs.getString("username"),
                        UUID.fromString(rs.getString("uuid")),
                        rs.getBoolean("personal_message_enabled"),
                        rs.getBoolean("friend_request_enabled"),
                        rs.getBoolean("party_invite_enabled"),
                        rs.getBoolean("hidden_staff")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveUserSettings(UserSettings settings, UUID player) {
        String sql = "UPDATE neptune_user_settings SET personal_message_enabled = ?, friend_request_enabled = ?, party_invite_enabled = ?, hidden_staff = ? WHERE uuid = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setBoolean(1, settings.isPersonalMessageEnabled());
            ps.setBoolean(2, settings.isFriendRequestEnabled());
            ps.setBoolean(3, settings.isPartyInviteEnabled());
            ps.setBoolean(3, settings.isHiddenStaff());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Friend> getFriends(String player) {
        return List.of();
    }

    @Override
    public void saveFriends(List<Friend> friends) {

    }
}
