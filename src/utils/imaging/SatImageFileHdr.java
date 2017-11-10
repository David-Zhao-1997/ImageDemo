package utils.imaging;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * General ENVI Image.
 *
 * @see utils.functional.ZiYuan3Reader
 * @see ShortSatImage
 */
public class SatImageFileHdr
{

    /**
     * <p>HDR中data type=1 表示文件以BYTE存储</p>
     */
    public static final int BYTE_DATA = 1;

    /**
     * <p>Short形式存储文件</p>
     */
    public static final int SHORT_DATA = 2;

    /**
     * <p>波段顺序格式(Band Sequential Format)</p>
     */
    public static final int BSQ = 10;
    /**
     * <p>波段按行交叉格式(Band Interleave by Line Format)</p>
     */
    public static final int BIL = 11;
    /**
     * <p>波段按像元(像素)交叉(Band Interleave by Pixel Format)</p>
     */
    public static final int BIP = 12;

    protected int interleave;
    protected int data_type;
    protected int bandCount;
    protected int samples;//列序号
    protected int lines;//行序号
    protected int xStart;
    protected int yStart;
    protected double lon;
    protected double lat;
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
    public SatImageFileHdr(String filename) throws IOException
    {
        path = filename;
        readHdr();
    }

    public void readHdr() throws IOException
    {
        BufferedReader bufr = new BufferedReader(new FileReader(path + ".hdr"));
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
                lon = Double.parseDouble(line.split(",")[3]);
                lat = Double.parseDouble(line.split(",")[4]);
                deltaLon = Double.parseDouble(line.split(",")[5]);
                deltaLat = Double.parseDouble(line.split(",")[6]);
            }
            else if (line.startsWith("bands"))
            {
                bandCount = Integer.parseInt(line.split("=")[1].trim());
            }
            else if (line.startsWith("data type"))
            {
                data_type = Integer.parseInt(line.split("=")[1].trim());
            }
            else if (line.startsWith("interleave"))
            {
                String interleave_ = line.split("=")[1].trim();
                if (interleave_.equalsIgnoreCase("bsq"))
                {
                    interleave = BSQ;
                }
                else if (interleave_.equalsIgnoreCase("bip"))
                {
                    interleave = BIP;
                }
                else if (interleave_.equalsIgnoreCase("bil"))
                {
                    interleave = BIL;
                }
            }
            else if (line.startsWith("x start"))
            {
                xStart = Integer.parseInt(line.split("=")[1].trim());
            }
            else if (line.startsWith("y start"))
            {
                yStart = Integer.parseInt(line.split("=")[1].trim());
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
        return lon;
    }

    /**
     * 获取起始纬度
     *
     * @return lat
     */
    public double getLat()
    {
        return lat;
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

    public void setSamples(int samples)
    {
        this.samples = samples;
    }

    public void setLines(int lines)
    {
        this.lines = lines;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public void setDeltaLon(double deltaLon)
    {
        this.deltaLon = deltaLon;
    }

    public void setDeltaLat(double deltaLat)
    {
        this.deltaLat = deltaLat;
    }

    public int getInterleave()
    {
        return interleave;
    }

    public void setInterleave(int interleave)
    {
        this.interleave = interleave;
    }

    public int getData_type()
    {
        return data_type;
    }

    public void setData_type(int data_type)
    {
        this.data_type = data_type;
    }

    public int getBandCount()
    {
        return bandCount;
    }

    public void setBandCount(int bandCount)
    {
        this.bandCount = bandCount;
    }

    public int getxStart()
    {
        return xStart;
    }

    public void setxStart(int xStart)
    {
        this.xStart = xStart;
    }

    public int getyStart()
    {
        return yStart;
    }

    public void setyStart(int yStart)
    {
        this.yStart = yStart;
    }
}
