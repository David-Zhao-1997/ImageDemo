package test;
import java.io.IOException;

import utils.ByteImageFile;
import utils.ShortImageFile;

public class Test8
{
	public static void main(String[] args) throws IOException
	{
		ByteImageFile byteImageUtils = new ByteImageFile("C:\\Users\\Administrator\\Desktop\\2015-7-4");
		short[][] b3 = byteImageUtils.convertBand3ToShorts();
		short[][] output = new short[5000][5000];
		for(int i =0;i<5000;i++)
		{
			for(int j =0;j<5000;j++)
			{
				output[i][j] = b3[i][j];
				
			}
		}
		ShortImageFile.addBandToFile(output, "C:\\Users\\Administrator\\Desktop\\Test5");
	}
}
