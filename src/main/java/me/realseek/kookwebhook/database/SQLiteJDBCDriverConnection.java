package me.realseek.kookwebhook.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.realseek.kookwebhook.Main;
import me.realseek.kookwebhook.util.SendMsg;

import javax.xml.soap.Detail;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteJDBCDriverConnection {
    static File file = new File(Main.getInstance().getDataFolder() + "\\test.db");
    /**
     * 连接到 SQLite
     *
     * @return
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + file.getAbsolutePath();
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println("You need the JDBC driver for SQLite: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * 创建新表
     */
    public static void createNewTables() {
        String sql1 = "CREATE TABLE IF NOT EXISTS repositories (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " name text NOT NULL UNIQUE\n"
                + ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS channels (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " repository_id integer,\n"
                + " channel_id text NOT NULL,\n"
                + " FOREIGN KEY(repository_id) REFERENCES repositories(id)\n"
                + ");";

        String sql3 = "CREATE TABLE IF NOT EXISTS details (\n"
                + " id INTEGER PRIMARY KEY,\n"
                + " repository_id INTEGER,\n"
                + " server_name TEXT,\n"
                + " channel_name TEXT,\n"
                + " FOREIGN KEY(repository_id) REFERENCES repositories(id)\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert a new repository
    public static void insertRepository(String name) {
        String sql = "INSERT INTO repositories(name) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert a new channel for a repository
    public static void insertChannel(String repositoryName, String channelId) {
        String sql1 = "SELECT id FROM repositories WHERE name = ?";
        String sql2 = "SELECT 1 FROM channels WHERE repository_id = ? AND channel_id = ?";
        String sql3 = "INSERT INTO channels(repository_id,channel_id) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2);
             PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {

            pstmt1.setString(1, repositoryName);
            ResultSet rs1 = pstmt1.executeQuery();

            if (rs1.next()) {
                int repositoryId = rs1.getInt("id");

                pstmt2.setInt(1, repositoryId);
                pstmt2.setString(2, channelId);
                ResultSet rs2 = pstmt2.executeQuery();

                if (!rs2.next()) {  // 如果没有找到对应的 channel_id，那就插入
                    pstmt3.setInt(1, repositoryId);
                    pstmt3.setString(2, channelId);
                    pstmt3.executeUpdate();
                    SendMsg.send(channelId, "绑定成功");
                    System.out.println("绑定成功");
                } else {
                    SendMsg.send(channelId, "这个频道ID已经绑定过了");
                    System.out.println("这个频道ID已经绑定过了");
                }
            } else {
                System.out.println("该仓库并不在数据库内");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertDetail(String repositoryName, String serverName, String channelName) {
        String sql1 = "SELECT id FROM repositories WHERE name = ?";
        String sql2 = "SELECT 1 FROM details WHERE repository_id = ? AND channel_name = ?";
        String sql3 = "INSERT INTO details(repository_id,server_name,channel_name) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2);
             PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {

            pstmt1.setString(1, repositoryName);
            ResultSet rs1 = pstmt1.executeQuery();

            if (rs1.next()) {
                int repositoryId = rs1.getInt("id");

                pstmt2.setInt(1, repositoryId);
                pstmt2.setString(2, channelName);
                ResultSet rs2 = pstmt2.executeQuery();

                if (!rs2.next()) {  // 如果没有找到对应的 channel_name，那就插入
                    pstmt3.setInt(1, repositoryId);
                    pstmt3.setString(2, serverName);
                    pstmt3.setString(3, channelName);
                    pstmt3.executeUpdate();
                    System.out.println("Inserted the detail successfully.");
                } else {
                    System.out.println("The detail is already in the database.");
                }
            } else {
                System.out.println("The repository is not in the database.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean checkTableExists(String tableName) {
        String sql = "PRAGMA table_info(" + tableName + ")";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // 如果表存在，这个查询会返回至少一行
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }


    public static List<String> selectRepositories() {
        List<String> repositories = new ArrayList<>();

        String sql = "SELECT name FROM repositories";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String repositoryName = rs.getString("name");
                repositories.add(repositoryName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return repositories;
    }

    // Delete a repository
    public static void deleteRepository(String name) {
        String sql = "DELETE FROM repositories WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete a channel
    public static void deleteChannel(String repositoryName, String channelId) {
        String sql = "DELETE FROM channels WHERE repository_id = "
                + "(SELECT id FROM repositories WHERE name = ?) AND channel_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, repositoryName);
            pstmt.setString(2, channelId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteDetail(String repositoryName, String channelName) {
        String sql1 = "SELECT id FROM repositories WHERE name = ?";
        String sql2 = "DELETE FROM details WHERE repository_id = ? AND channel_name = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            pstmt1.setString(1, repositoryName);
            ResultSet rs1 = pstmt1.executeQuery();

            if (rs1.next()) {
                int repositoryId = rs1.getInt("id");

                pstmt2.setInt(1, repositoryId);
                pstmt2.setString(2, channelName);
                pstmt2.executeUpdate();
                System.out.println("Deleted the detail successfully.");
            } else {
                System.out.println("The repository is not in the database.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<String> selectChannels(String repositoryName) {
        String sql = "SELECT c.channel_id "
                + "FROM channels c "
                + "JOIN repositories r ON c.repository_id = r.id "
                + "WHERE r.name = ?";

        List<String> channelIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, repositoryName);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                channelIds.add(rs.getString("channel_id"));
            }
        } catch (SQLException e) {
            System.out.println("查询方法" + e.getMessage());
        }

        return channelIds;
    }

    public static Multimap<String, String> selectDetails(String repositoryName) {
        Multimap<String, String> details = ArrayListMultimap.create();

        String sql = "SELECT d.server_name, d.channel_name "
                + "FROM details d "
                + "JOIN repositories r ON d.repository_id = r.id "
                + "WHERE r.name = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, repositoryName);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                String serverName = rs.getString("server_name");
                String channelName = rs.getString("channel_name");
                details.put(serverName, channelName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return details;
    }

    public static boolean repositoryExists(String repositoryName) {
        String sql = "SELECT id FROM repositories WHERE name = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, repositoryName);
            ResultSet rs = pstmt.executeQuery();

            // if rs.next() returns false, it means there are no rows in the result set
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}