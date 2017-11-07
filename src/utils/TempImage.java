package utils;

import java.util.ArrayList;

public class TempImage
{
    protected int samples;//列序号
    protected int lines;//行序号
    protected double Lon;
    protected double Lat;
    protected double deltaLon;
    protected double deltaLat;
    protected ArrayList<byte[][]> imageList;

    public TempImage(ShortImageFile shortImageFile)
    {
        this.samples = shortImageFile.getSamples();
        this.lines = shortImageFile.getLines();
        this.deltaLat = shortImageFile.getDeltaLat();
        this.deltaLon = shortImageFile.getDeltaLon();
        this.Lat = shortImageFile.getLat();
        this.Lon = shortImageFile.getLon();
    }
}
