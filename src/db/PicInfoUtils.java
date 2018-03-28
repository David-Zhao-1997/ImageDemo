package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import utils.io.ShortImageReader;
import utils.imaging.ShortSatImage;

public class PicInfoUtils
{
    private static Connection connection = null;

    public static boolean uploadImageInfo(ShortSatImage shortSatImage, String fileName)
    {
        connection = new Conn().getCon();
        double[] averages = new double[5];
        double[] variances = new double[5];
        for (int i = 1; i <= 4; i++)
        {
            averages[i] = shortSatImage.getAverage(i);
            variances[i] = shortSatImage.getVariance(i);
        }
        PreparedStatement pstmt = null;
        try
        {
            pstmt = connection.prepareStatement("insert into PicInfo values (?,?,?,?,?,?,?,?,?,NULL,NULL);");
            pstmt.setString(1, fileName);
            pstmt.setDouble(2, averages[1]);
            pstmt.setDouble(3, averages[2]);
            pstmt.setDouble(4, averages[3]);
            pstmt.setDouble(5, averages[4]);
            pstmt.setDouble(6, variances[1]);
            pstmt.setDouble(7, variances[2]);
            pstmt.setDouble(8, variances[3]);
            pstmt.setDouble(9, variances[4]);
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws IOException
    {
        for (int i = 0; i <= 327; i++)
        {
            uploadImageInfo(new ShortImageReader("c:\\4BandsOut\\4-Bands-_" + i).getImage(), "4-Bands-_" + i);
            System.out.println(i);
        }
    }
}
