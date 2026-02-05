package lol.vedant.core.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.function.Function;

public class MySQLUtils implements StorageHelper {

    private final DataSource dataSource;

    public MySQLUtils(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void bind(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    @Override
    public int update(String sql, Object... params) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            bind(ps, params);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T query(String sql, Function<ResultSet, T> mapper, Object... params) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return mapper.apply(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
