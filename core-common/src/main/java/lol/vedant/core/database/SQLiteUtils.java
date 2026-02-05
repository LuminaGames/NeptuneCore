package lol.vedant.core.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.function.Function;

public class SQLiteUtils implements StorageHelper {

    private final DataSource dataSource;
    private static final ThreadLocal<Connection> CONNECTION = new ThreadLocal<>();

    public SQLiteUtils(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void bind(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    private Connection acquire() throws SQLException {
        Connection c = CONNECTION.get();
        if (c == null) {
            c = dataSource.getConnection();
            CONNECTION.set(c);
        }
        return c;
    }

    private void release(Connection c) throws SQLException {
        if (CONNECTION.get() == c) {
            CONNECTION.remove();
            c.close();
        }
    }

    @Override
    public int update(String sql, Object... params) {
        Connection c = null;
        try {
            c = acquire();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                bind(ps, params);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (c != null) release(c);
            } catch (SQLException ignored) {}
        }
    }

    @Override
    public <T> T query(String sql, Function<ResultSet, T> mapper, Object... params) {
        Connection c = null;
        try {
            c = acquire();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                bind(ps, params);
                try (ResultSet rs = ps.executeQuery()) {
                    return mapper.apply(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (c != null) release(c);
            } catch (SQLException ignored) {}
        }
    }
}
