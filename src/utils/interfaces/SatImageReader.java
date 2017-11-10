package utils.interfaces;

import java.io.IOException;

import utils.imaging.ShortSatImage;

public interface SatImageReader
{

    /**
     * <p>单独读出文件的某一波段，若有缓存则直接返回</p>
     *
     * @param bandIndex 波段编号，从1开始
     * @return 单波段的ShortSatImage
     */
    short[][] getBand(int bandIndex) throws IOException;

    ShortSatImage getImage() throws IOException;

}
