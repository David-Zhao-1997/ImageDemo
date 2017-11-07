package utils;

import java.io.IOException;

public class SingleBandImageFile extends EnviImageFile
{
    /**
     * 用于读取图像文件的工具类
     *
     * @param filename 传入文件路径
     * @throws IOException
     */
    public SingleBandImageFile(String filename) throws IOException
    {
        super(filename);
    }


}
