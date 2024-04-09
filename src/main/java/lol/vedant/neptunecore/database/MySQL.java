package lol.vedant.neptunecore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.neptunecore.NeptuneCore;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;


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
    private Configuration config;

    public MySQL(NeptuneCore plugin) {
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

        hikariConfig.setPoolName("UltimateBounty-Pool");

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
        String sql = "CREATE TABLE IF NOT EXISTS friends_data (" +
                "player_uuid VARCHAR(200)," +
                "player_name VARCHAR(200)," +
                "friend_uuid VARCHAR(200)," +
                "friend_name VARCHAR(200)," +
                "friends_since TIMESTAMP);";

        try(Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFriend(ProxiedPlayer adder, ProxiedPlayer friend) {
        String sql = "INSERT INTO friends_data (player_uuid, player_name, friend_uuid, friends_since";
    }

    @Override
    public void removeFriend() {

    }
}
