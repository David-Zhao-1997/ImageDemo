package utils.io;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import utils.imaging.SatImageFileHdr;
import utils.imaging.ShortSatImage;
import utils.interfaces.SatImageWriter;
import utils.tools.DataTypeUtils;

@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class ShortImageWriter implements SatImageWriter
{

    private static final String FILE_TYPE = "ENVI";
    private static final String DESCRIPTION = "description";
    public static final int BYTE_ORDER = 1;

    private String filepath;
    private ShortSatImage image;

    public ShortImageWriter(ShortSatImage image)
    {
        this.image = image;
    }

    private void writeBands() throws IOException
    {
        int bandCount = image.getBandCount();
        int lines = image.getLines();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filepath, true), 0x100000);
        for (int i = 0; i < bandCount; i++)
        {
            short[][] tempBand = image.getBand(i + 1);
            for (int j = 0; j < lines; j++)
            {
                out.write(DataTypeUtils.toByteArray(tempBand[j]));
            }
        }
        out.flush();
        out.close();
    }

    private void writeHdr() throws IOException
    {
        StringBuilder content = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[EEE LLL dd HH:mm:ss yyyy]", Locale.US);
        content.append(FILE_TYPE + "\n");
        content.append(DESCRIPTION + "=" + " {\n");
        content.append(" " + simpleDateFormat.format(new Date()) + "}\n");
        content.append("samples = " + image.getSamples() + "\n");
        content.append("lines = " + image.getLines() + "\n");
        content.append("bands = " + image.getBandCount() + "\n");
        content.append("header offset = 0\n");
        content.append("file type = ENVI Standard\n");
        content.append("data type = " + SatImageFileHdr.SHORT_DATA + "\n");
        content.append("interleave = bsq\n");
        content.append("sensor type = unknown\n");
        content.append("byte order = ").append(BYTE_ORDER).append("\n");
        content.append("map info = {");
        content.append("Geographic Lat/Lon, 1.0000, 1.0000, ");
        //Lat, Lon
        content.append(image.getLon() + ", " + image.getLat() + ", ");
        //Delta
        content.append(image.getDeltaLon() + ", " + image.getDeltaLat() + ", ");
        content.append("WGS-84, units=Degrees");
        content.append("}");
        content.append("\n");
        BufferedWriter out = new BufferedWriter(new FileWriter(filepath + ".HDR"));
        out.write(content.toString());
        out.flush();
        out.close();
    }

    @Override
    public void write(String filepath) throws IOException
    {
        this.filepath = filepath;
        writeBands();
        writeHdr();
    }
}
