package utils.imaging;

import java.util.ArrayList;

public class ShortSatImage
{

    private ArrayList<short[][]> bands;
    private Statistics stats;

    private int bandCount;
    private int samples;
    private int lines;
    private int xStart;
    private int yStart;
    private double lon;
    private double lat;
    private double deltaLon;
    private double deltaLat;

    public ShortSatImage(int bandCount, int samples, int lines, int xStart, int yStart, double lon, double lat, double deltaLon, double deltaLat, ArrayList<short[][]> bands_)
    {
        initBands(bands_);
        this.bandCount = bandCount;
        this.samples = samples;
        this.lines = lines;
        this.lon = lon;
        this.lat = lat;
        this.xStart = xStart;
        this.yStart = yStart;
        this.deltaLon = deltaLon;
        this.deltaLat = deltaLat;
//		getStats();
    }

    /**
     * 对原图进行修改后传入的图像原型，请务必修改对应的参数，例如合并band后的bandCount！
     *
     * @param origin
     * @param bands_
     */
    public ShortSatImage(ShortSatImage origin, ArrayList<short[][]> bands_)
    {
        initBands(bands_);
        bandCount = origin.getBandCount();
        samples = origin.getSamples();
        lines = origin.getLines();
        lon = origin.getLon();
        lat = origin.getLat();
        xStart = origin.getxStart();
        yStart = origin.getyStart();
        deltaLon = origin.getDeltaLon();
        deltaLat = origin.getDeltaLat();
//		getStats();
    }

    /**
     * 对原图进行修改后传入的图像原型，请务必修改对应的参数，例如合并band后的bandCount！
     *
     * @param origin
     * @param bands_
     */
    public ShortSatImage(SatImageFileHdr origin, ArrayList<short[][]> bands_)
    {
        initBands(bands_);
        bandCount = origin.getBandCount();
        samples = origin.getSamples();
        lines = origin.getLines();
        lon = origin.getLon();
        lat = origin.getLat();
        xStart = origin.getxStart();
        yStart = origin.getyStart();
        deltaLon = origin.getDeltaLon();
        deltaLat = origin.getDeltaLat();
//		getStats();
    }

    private void initBands(ArrayList<short[][]> bands_)
    {
        bands = bands_;
    }

    public short[][] getBand(int bandIndex)
    {
        return bands.get(bandIndex - 1);
    }

    private void getStats()
    {
        if (stats == null)
        {
            stats = new Statistics();
            for (int x = 0; x < bands.size(); x++)
            {
                long sum = 0;
                double nXVariance = 0.0;
                short[][] band = bands.get(x);
                double avg = 0;
                for (int i = 0; i < lines; i++)
                {
                    for (int j = 0; j < samples; j++)
                    {
                        sum += band[i][j];
                    }
                }
                avg = sum / (samples * lines);
                stats.setAverage(x, avg);
                for (int i = 0; i < lines; i++)
                {
                    for (int j = 0; j < samples; j++)
                    {
                        nXVariance += (band[i][j] - avg) * (band[i][j] - avg);
                    }
                }
                stats.setVariance(x, nXVariance / samples / lines);
            }
        }
    }

    /**
     * 获得某波段的统计平均值，波段编号从1开始
     *
     * @param bandIndex
     * @return
     */
    public double getAverage(int bandIndex)
    {
        getStats();
        return stats.getAverages().get(bandIndex - 1);
    }

    /**
     * 获得某波段的统计方差，波段编号从1开始
     *
     * @param bandIndex
     * @return
     */
    public double getVariance(int bandIndex)
    {
        getStats();
        return stats.getVariances().get(bandIndex - 1);
    }

    public int getBandCount()
    {
        return bandCount;
    }

    public void setBandCount(int bandCount)
    {
        this.bandCount = bandCount;
    }

    public int getSamples()
    {
        return samples;
    }

    public void setSamples(int samples)
    {
        this.samples = samples;
    }

    public int getLines()
    {
        return lines;
    }

    public void setLines(int lines)
    {
        this.lines = lines;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getDeltaLon()
    {
        return deltaLon;
    }

    public void setDeltaLon(double deltaLon)
    {
        this.deltaLon = deltaLon;
    }

    public double getDeltaLat()
    {
        return deltaLat;
    }

    public void setDeltaLat(double deltaLat)
    {
        this.deltaLat = deltaLat;
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

    public ArrayList<short[][]> getBands()
    {
        return bands;
    }
}
