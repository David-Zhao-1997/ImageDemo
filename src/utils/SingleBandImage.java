package utils;

import java.io.IOException;

public class SingleBandImage extends EnviImage
{
    /**
     * 用于读取图像文件的工具类
     *
     * @param filename 传入文件路径
     * @throws IOException
     */
    public SingleBandImage(String filename) throws IOException
    {
        super(filename);
    }


}
