package test;

import java.io.IOException;

import utils.ByteImageFile;
import utils.HdrGenerator;

public class Test6
{
	public static void main(String[] args) throws IOException
	{
		ByteImageFile byteImage = new ByteImageFile("C:\\Users\\Administrator\\Desktop\\2015-7-4");
		System.out.println(byteImage.getLat());
		System.out.println(byteImage.getLon());
		System.out.println(byteImage.getDeltaLat());
		System.out.println(byteImage.getDeltaLon());
		
		HdrGenerator.generate(byteImage, "C:\\Users\\Administrator\\Desktop\\text55.HDR", 300, 300, 4);
	}
}