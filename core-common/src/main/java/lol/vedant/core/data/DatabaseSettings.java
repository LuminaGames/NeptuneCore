package lol.vedant.core.data;

public class DatabaseSettings {

    private String host;
    private String database;
    private int port;
    private String user;
    private String password;
    private boolean ssl;
    private boolean certificateVerification;
    private int poolSize;
    private int maxLifetime;

    public DatabaseSettings(String host, String database, int port, String user, String password, boolean ssl, boolean certificateVerification, int poolSize, int maxLifetime) {
        this.host = host;
        this.database = database;
        this.port = port;
        this.user = user;
        this.password = password;
        this.ssl = ssl;
        this.certificateVerification = certificateVerification;
        this.poolSize = poolSize;
        this.maxLifetime = maxLifetime;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSsl() {
        return ssl;
    }

    public boolean isCertificateVerification() {
        return certificateVerification;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public int getMaxLifetime() {
        return maxLifetime;
    }
}
