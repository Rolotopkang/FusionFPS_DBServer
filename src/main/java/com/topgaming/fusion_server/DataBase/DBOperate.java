package com.topgaming.fusion_server.DataBase;

import com.topgaming.fusion_server.NetMessagee.EconomicMsg;
import com.topgaming.fusion_server.Status.DefaultStatus;
import com.topgaming.fusion_server.Status.LoginStatus;
import com.topgaming.fusion_server.Status.RegisterStatus;
import lombok.Builder;

import java.sql.*;

public class DBOperate {
    Statement st = null;
    public static DBOperate instance = null;

    public static DBOperate getInstance() {
        if (instance == null) {
            instance = new DBOperate();
        }
        return instance;
    }

    public LoginStatus Login(String name, String password) throws SQLException {
        Connection con = DatabaseManager.getInstance().getConnection();
        try {
            st = con.createStatement();
            String sql = "SELECT Password FROM UserInfo where UserName='" + name + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String m1 = rs.getString(1);
                if (password.equals(m1)) {
                    System.out.println("登录成功");
                    return LoginStatus.SUCCESS;
                } else {
                    System.out.println("登录失败");
                    return LoginStatus.PASSWORD_ERROR;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 关闭连接、语句和结果集
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    // 处理异常
                }
            }

            if(con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    // 处理异常
                }
        }
        return LoginStatus.ID_NOT_EXIST;
    }

    public RegisterStatus register(String name, String password) throws SQLException {
        Connection con = DatabaseManager.getInstance().getConnection();
        try {
            Statement st = con.createStatement();
            String sql = "SELECT 1 FROM UserInfo WHERE UserName = '" + name + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    System.out.println("注册重名");
                    return RegisterStatus.FAILED_SAMENAME;
                }
            }

            try {
                sql = "insert into UserInfo(UserName,Password,Money) values(?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, password);
                pst.setInt(3, 0);
                int rs1 = pst.executeUpdate();
                System.out.println("注册成功");
                return RegisterStatus.SUCCESS;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
// 关闭连接、语句和结果集
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    // 处理异常
                }
            }

            if(con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    // 处理异常
                }
        }
        System.out.println("注册失败");
        return RegisterStatus.CONNECTION_FAILED;
    }

    public int UserMoneyGet(String name) throws SQLException {
        Connection con = DatabaseManager.getInstance().getConnection();
        try {
            st = con.createStatement();
            String sql = "SELECT Money FROM UserInfo where UserName='" + name + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int tmp_Money = rs.getInt(1);
                System.out.println("查询成功");
                return tmp_Money;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
// 关闭连接、语句和结果集
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    // 处理异常
                }
            }

            if(con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    // 处理异常
                }
        }

        System.out.println("查询失败");
        return -1;
    }

    public DefaultStatus UserMoneyAdd(String name , int add) throws SQLException
    {
        int Current_Money = UserMoneyGet(name);
        Connection con = DatabaseManager.getInstance().getConnection();
        try{
            st = con.createStatement();
            String sql = "UPDATE UserInfo SET Money=? where UserName=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,Current_Money+add);
            ps.setString(2,name);
            int rs = ps.executeUpdate();
            return rs>0? DefaultStatus.SUCCESS : DefaultStatus.FAILED;
        }catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
// 关闭连接、语句和结果集
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    // 处理异常
                }
            }

            if(con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    // 处理异常
                }
        }
        return DefaultStatus.SERVERWRONG;
    }


    public static void main(String[] args) {
//        DatabaseManager db=new DatabaseManager();
//      System.out.println(db.exist_chatroom("1231"));
        //System.out.println(db.returnChatRoomId());
        //db.getUserChatroomList("1");
        //System.out.println(db.random2());
//        DBOperate.getInstance().Login("A", "000000");

    }
}
