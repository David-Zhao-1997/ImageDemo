package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * General ENVI Image.
 *
 * @see ByteImageFile
 * @see ShortImageFile
 */
public class EnviImageFile
{
    protected int samples;//列序号
    protected int lines;//行序号
    protected double Lon;
    protected double Lat;
    protected double deltaLon;
    protected double deltaLat;
    protected BufferedInputStream bis;
    protected String path;

    /**
     * 用于读取图像文件的工具类
     *
     * @param filename 传入文件路径
     * @throws IOException
     */
    public EnviImageFile(String filename) throws IOException
    {
        path = filename;
        BufferedReader bufr = new BufferedReader(new FileReader(filename + ".hdr"));
        String line;
        while ((line = bufr.readLine()) != null)
        {
            if (line.startsWith("lines"))
            {
                lines = Integer.parseInt(line.split("=")[1].trim());
            }
            else if (line.startsWith("samples"))
            {
                samples = Integer.parseInt(line.split("=")[1].trim());
            }
            else if (line.startsWith("map"))
            {
                Lon = Double.parseDouble(line.split(",")[3]);
                Lat = Double.parseDouble(line.split(",")[4]);
                deltaLon = Double.parseDouble(line.split(",")[5]);
                deltaLat = Double.parseDouble(line.split(",")[6]);
            }
        }
        bufr.close();
    }

    /**
     * 获取列数<br>
     * Sample在每一行中表现为一个列(点)
     *
     * @return 文件的列数
     */
    public int getSamples()
    {
        return samples;
    }

    /**
     * 获取行数
     *
     * @return 文件的行数
     */
    public int getLines()
    {
        return lines;
    }

    /**
     * 获取缓冲输入流
     *
     * @return 对应文件的缓冲输入流
     * @throws IOException
     */
    public BufferedInputStream getInputStream() throws IOException
    {
        bis = new BufferedInputStream(new FileInputStream(path));
        return bis;
    }

    /**
     * 获取起始经度
     *
     * @return lon
     */
    public double getLon()
    {
        return Lon;
    }

    /**
     * 获取起始纬度
     *
     * @return lat
     */
    public double getLat()
    {
        return Lat;
    }

    /**
     * 获取水平相邻两个像素点的经度差(delta)
     *
     * @return deltaLon
     */
    public double getDeltaLon()
    {
        return deltaLon;
    }

    /**
     * 获取垂直相邻两个像素点的纬度差(delta)
     *
     * @return deltaLat
     */
    public double getDeltaLat()
    {
        return deltaLat;
    }

}
