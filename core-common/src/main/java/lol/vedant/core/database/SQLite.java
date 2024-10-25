package lol.vedant.core.database;

import lol.vedant.core.data.Friend;
import lol.vedant.core.data.UserSettings;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public class SQLite implements Database {

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
        String friendsTableSql = "CREATE TABLE IF NOT EXISTS neptune_friends (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "player1_username VARCHAR(255) NOT NULL," +
                "player2_username VARCHAR(255) NOT NULL," +
                "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String pendingRequestsTableSql = "CREATE TABLE IF NOT EXISTS neptune_pending_requests (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "sender_username VARCHAR(255) NOT NULL," +
                "receiver_username VARCHAR(255) NOT NULL," +
                "date_sent TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String userSettingsTableSql = "CREATE TABLE IF NOT EXISTS neptune_user_settings (" +
                "uuid VARCHAR(255) PRIMARY KEY," +
                "username VARCHAR(255) NOT NULL," +
                "personal_message_enabled BOOLEAN DEFAULT TRUE," +
                "friend_request_enabled BOOLEAN DEFAULT TRUE," +
                "party_invite_enabled BOOLEAN DEFAULT TRUE," +
                "hidden_staff BOOLEAN DEFAULT FALSE" +
                ");";

        try {
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

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(UUID player) {
        String sql = "SELECT * FROM neptune_user_settings WHERE uuid = ?";

        try {
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
        try {

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
        try {
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
        List<Friend> friends = new ArrayList<>();
        String query = "SELECT CASE WHEN player1_username = ? THEN player2_username ELSE player1_username END AS friend_username, "
                + "date_added "
                + "FROM neptune_friends WHERE player1_username = ? OR player2_username = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, player);
            statement.setString(2, player);
            statement.setString(3, player);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String friendName = resultSet.getString("friend_username");
                    Timestamp friendSince = resultSet.getTimestamp("date_added");
                    friends.add(new Friend(friendName, friendSince.toInstant()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return friends;
    }

    @Override
    public void saveFriends(List<Friend> friends) {

    }
}
