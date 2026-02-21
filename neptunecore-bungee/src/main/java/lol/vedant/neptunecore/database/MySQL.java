package lol.vedant.neptunecore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.api.friends.Friend;
import net.md_5.bungee.config.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MySQL implements Database {

    private NeptuneCore plugin;
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
    private Configuration config;

    public MySQL(NeptuneCore plugin) {

        plugin.getLogger().info("Using storage method [MYSQL]");

        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.host = config.getString("database.host");
        this.database = config.getString("database.database");
        this.user = config.getString("database.user");
        this.pass = config.getString("database.pass");
        this.port = config.getInt("database.port");
        this.ssl = config.getBoolean("database.ssl");
        this.certificateVerification = config.getBoolean("database.verify-certificate", true);
        this.poolSize = config.getInt("database.pool-size", 10);
        this.maxLifetime = config.getInt("database.max-lifetime", 1800);
        connect();
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
            plugin.getLogger().info("Connected to MySQL database successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Could not connect to MySQL database!");
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

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(friendsTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String pendingRequestsTableSql = "CREATE TABLE neptune_pending_requests (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "sender_username VARCHAR(255) NOT NULL," +
                "receiver_username VARCHAR(255) NOT NULL," +
                "date_sent TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(pendingRequestsTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        plugin.getLogger().info("Created database tables..");
    }


    @Override
    public void addFriend(String adder, String friend) {
        String sql = "INSERT INTO neptune_friends (player1_username, player2_username) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, adder);
            stmt.setString(2, friend);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFriend(String player, String removedFriend) {
        String sql = "DELETE FROM neptune_friends WHERE (player1_username = ? AND player2_username = ?) OR (player1_username = ? AND player2_username = ?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, player);
            stmt.setString(2, removedFriend);
            stmt.setString(3, removedFriend);
            stmt.setString(4, player);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFriendRequest(String player, String friend) {
        String sql = "INSERT INTO neptune_pending_requests (sender_username, receiver_username) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, player);
            stmt.setString(2, friend);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Friend> getPendingRequests(String player) {
        String sql = "SELECT * FROM neptune_pending_requests WHERE receiver_username=?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                List<Friend> requests = new ArrayList<>();
                while(rs.next()) {
                    requests.add(new Friend(rs.getString("sender_username"), Timestamp.valueOf(rs.getString("date_sent"))));
                }
                return requests;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Friend> getFriends(String playerUsername) {
        List<Friend> friends = new ArrayList<>();
        String query = "SELECT CASE WHEN player1_username = ? THEN player2_username ELSE player1_username END AS friend_username, "
                + "date_added "
                + "FROM neptune_friends WHERE player1_username = ? OR player2_username = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, playerUsername);
            statement.setString(2, playerUsername);
            statement.setString(3, playerUsername);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String friendName = resultSet.getString("friend_username");
                    Timestamp friendSince = Timestamp.valueOf(resultSet.getString("date_sent"));
                    friends.add(new Friend(friendName, friendSince));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return friends;
    }

    @Override
    public void denyFriendRequest(String player, String sender) {
        String sql = "DELETE FROM neptune_pending_requests WHERE sender_username=? AND receiver_username=?";

        if(!areFriends(player, sender)) {
            return;
        }

        try (Connection connection  = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, sender);
            ps.setString(2, player);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean areFriends(String player, String friend) {
        String sql = "SELECT * FROM neptune_friends WHERE player1_username=? AND player2_username=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player);
            ps.setString(2, friend);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
