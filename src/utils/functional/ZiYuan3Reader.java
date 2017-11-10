package utils.functional;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import utils.imaging.SatImageFileHdr;
import utils.imaging.ShortSatImage;
import utils.interfaces.SatImageReader;

/**
 * <p>用于直接从文件提取数据封装入ShortSatImage，或者获得short[][]</p>
 */
public class ZiYuan3Reader implements SatImageReader
{

    private ShortSatImage image;
    private String filepath;

    public ZiYuan3Reader(String filepath)
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
                byte[][] bBand = new byte[lines][samples];
                short[][] sBand = new short[lines][samples];
                for (int j = 0; j < lines; j++)
                {
                    inputStream.read(bBand[j]);
                    for (int k = 0; k < samples; k++)
                    {
                        sBand[j][k] = (short) (bBand[j][k] & 0xff);
                    }
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
        return image;
    }
}
