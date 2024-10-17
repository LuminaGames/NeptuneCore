package lol.vedant.neptunecore.database;


import lol.vedant.neptunecore.NeptuneCore;
import lol.vedant.neptunecore.api.friends.Friend;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class SQLite implements Database {

    private NeptuneCore plugin;
    private File dataFolder;
    private Connection connection;


    public SQLite(NeptuneCore plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        plugin.getLogger().info("Using storage method [SQLITE]");

        File dataFile = new File(dataFolder, "data.db");

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
            plugin.getLogger().info("Connected to SQLite database successfully");
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("Cannot connect to SQLite Database");
            e.printStackTrace();
        }

    }

    @Override
    public void init() {
        try {
            String friendsTableSql = "CREATE TABLE IF NOT EXISTS neptune_friends (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player1_username VARCHAR(255) NOT NULL," +
                    "player2_username VARCHAR(255) NOT NULL," +
                    "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            PreparedStatement ps = connection.prepareStatement(friendsTableSql);
            ps.executeUpdate();

            String pendingRequestsTableSql = "CREATE TABLE IF NOT EXISTS neptune_pending_requests (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "sender_username VARCHAR(255) NOT NULL," +
                    "receiver_username VARCHAR(255) NOT NULL," +
                    "date_sent TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            ps = connection.prepareStatement(pendingRequestsTableSql);
            ps.executeUpdate();

            plugin.getLogger().info("Created database tables...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFriend(String adder, String friend) {
        String sql = "INSERT INTO neptune_friends (player1_username, player2_username) VALUES (?, ?)";

        try {
            PreparedStatement ps  = connection.prepareStatement(sql);
            ps.setString(1, adder);
            ps.setString(2, friend);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFriend(String player, String removedFriend) {
        String sql = "DELETE FROM neptune_friends WHERE (player1_username = ? AND player2_username = ?) OR (player1_username = ? AND player2_username = ?)";

        try {
            PreparedStatement ps  = connection.prepareStatement(sql);
            ps.setString(1, player);
            ps.setString(2, removedFriend);
            ps.setString(3, removedFriend);
            ps.setString(4, player);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFriendRequest(String player, String friend) {
        String sql = "INSERT INTO neptune_pending_requests (sender_username, receiver_username) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player);
            ps.setString(2, friend);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Friend> getPendingRequests(String player) {
        String sql = "SELECT * FROM neptune_pending_requests WHERE receiver_username=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();

            List<Friend> requests = new ArrayList<>();
            while(rs.next()) {
                requests.add(new Friend(rs.getString("sender_username"), Timestamp.valueOf(rs.getString("date_sent"))));
            }
            System.out.println(requests);
            return requests;

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

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, playerUsername);
            statement.setString(2, playerUsername);
            statement.setString(3, playerUsername);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String friendName = resultSet.getString("friend_username");
                    Timestamp friendSince = resultSet.getTimestamp("date_added");
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

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, sender);
            ps.setString(2, player);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean areFriends(String player, String friend) {
        String sql = "SELECT * FROM neptune_friends WHERE player1_username=? AND player2_username=?";

        try {
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
