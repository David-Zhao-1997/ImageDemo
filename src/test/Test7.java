package test;
import java.io.IOException;

import utils.ByteImageFile;
import utils.ShortImageFile;

public class Test7
{
	public static void main(String[] args) throws IOException
	{
//		byte[][] b1 = new ByteImageUtils("C:\\Users\\Administrator\\Desktop\\region").getBand1InBytes();
//		byte[][] b2 = new ByteImageUtils("C:\\Users\\Administrator\\Desktop\\region").getBand2InBytes();
//		byte[][] b3 = new ByteImageUtils("C:\\Users\\Administrator\\Desktop\\region").getBand3InBytes();
//		byte[][] b4 = new ByteImageUtils("C:\\Users\\Administrator\\Desktop\\region").getBand4InBytes();
//		ByteImageUtils.addBandtoFile(b1, "C:\\Users\\Administrator\\Desktop\\test4");
//		ByteImageUtils.addBandtoFile(b2, "C:\\Users\\Administrator\\Desktop\\test4");
//		ByteImageUtils.addBandtoFile(b3, "C:\\Users\\Administrator\\Desktop\\test4");
//		ByteImageUtils.addBandtoFile(b4, "C:\\Users\\Administrator\\Desktop\\test4");
//		
		
		short[][] b1 = new ByteImageFile("C:\\Users\\Administrator\\Desktop\\2015-7-4").convertBand1ToShorts();
		short[][] b2 = new ByteImageFile("C:\\Users\\Administrator\\Desktop\\2015-7-4").convertBand2ToShorts();
		short[][] b3 = new ByteImageFile("C:\\Users\\Administrator\\Desktop\\2015-7-4").convertBand3ToShorts();
		short[][] b4 = new ByteImageFile("C:\\Users\\Administrator\\Desktop\\2015-7-4").convertBand4ToShorts();
		ShortImageFile.addBandToFile(b1, "C:\\Users\\Administrator\\Desktop\\test4");
		ShortImageFile.addBandToFile(b2, "C:\\Users\\Administrator\\Desktop\\test4");
		ShortImageFile.addBandToFile(b3, "C:\\Users\\Administrator\\Desktop\\test4");
		ShortImageFile.addBandToFile(b4, "C:\\Users\\Administrator\\Desktop\\test4");
	}
}
