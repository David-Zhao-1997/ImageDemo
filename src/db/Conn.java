package db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by David on 2017/2/9.
 */
public class Conn
{
    public Connection getCon()
    {
        Connection conn = null;
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbURL = "jdbc:sqlserver://123.206.204.252:1433;DatabaseName=GreenTide";
            String userName = "david";
            String userPwd = "qaz13047409865";
            conn = DriverManager.getConnection(dbURL, userName, userPwd);
//            System.out.println(conn.getMetaData().getURL());
            return conn;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args)
    {
        Conn conn = new Conn();
        System.out.println(conn.getCon());
    }
}
