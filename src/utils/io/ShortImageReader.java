package utils.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import utils.imaging.SatImageFileHdr;
import utils.imaging.ShortSatImage;
import utils.interfaces.SatImageReader;
import utils.tools.DataTypeUtils;

public class ShortImageReader implements SatImageReader
{

    private ShortSatImage image;
    private String filepath;

    public ShortImageReader(String filepath)
    {
        this.filepath = filepath;
    }

    @Override
    public short[][] getBand(int bandIndex) throws IOException
    {
        return getImage().getBand(bandIndex);
    }

    private void readImage() throws IOException
    {
        if (image == null)
        {
            SatImageFileHdr hdr = new SatImageFileHdr(filepath);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
            ArrayList<short[][]> bands = new ArrayList<>();
            int bandCount = hdr.getBandCount();
            int lines = hdr.getLines();
            int samples = hdr.getSamples();
            for (int i = 0; i < bandCount; i++)
            {
                byte[][] bBand = new byte[lines][samples * 2];
                short[][] sBand = new short[lines][samples];
                for (int j = 0; j < lines; j++)
                {
                    inputStream.read(bBand[j]);
                    sBand[j] = DataTypeUtils.toShortArray(bBand[j]);
                }
                bands.add(sBand);
            }
            this.image = new ShortSatImage(hdr, bands);
        }
    }

    @Override
    public ShortSatImage getImage() throws IOException
    {
        readImage();
        return this.image;
    }
}
