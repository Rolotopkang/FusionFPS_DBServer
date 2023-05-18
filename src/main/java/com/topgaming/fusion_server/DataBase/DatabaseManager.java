package com.topgaming.fusion_server.DataBase;

import java.sql.*;

public class DatabaseManager {
    private Connection con;
    private Statement sta;
    private ResultSet rs;
    public static String[] list;
    public static DatabaseManager instance = null;

//    static {
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public static DatabaseManager getInstance() throws SQLException {
        if(instance == null)
        {
            instance = new DatabaseManager();
            instance.getConnection();
        }
        return instance;
    }



    /**
     * 加载驱动程序
     */
    public Connection getConnection() throws SQLException {
        if(con != null && con.isValid(5))
        {
            return con;
        }
//        String url = "jdbc:mysql://localhost:3306/fusion_test";
        String url = "jdbc:mysql://47.120.10.76:3306/fusion_test";
        try {
            con = DriverManager.getConnection(url, "root", "root");
            sta = con.createStatement();
//            System.out.println("连接成功");
        } catch (SQLException e) {
//            System.out.println("连接失败");
            e.printStackTrace();
        }
        return con;
    }


    public void close() {
        if (con!= null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




}
