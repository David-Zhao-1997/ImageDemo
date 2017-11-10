package test;


import utils.functional.ShortImageReader;
import utils.imaging.ShortSatImage;

public class CountTest
{
	public static void main(String[] args) throws Exception
	{
		//		ByteImageFile byteImage = new ByteImageFile("C:\\Users\\Administrator\\Desktop\\region");
		//		short[][] band = byteImage.convertBand1ToShorts();
		//		for(int i = 0;i<band.length;i++)
		//		{
		//			for(int j = 0;j<band[i].length;j++)
		//			{
		//				if (band[i][j]!=255)
		//				{
		//					band[i][j]=0;
		//				}
		//			}
		//		}
		//		ShortImageFile.saveBandsToFile("C:\\Users\\Administrator\\Desktop\\countTest\\count", band);



		ShortSatImage shortImage = new ShortImageReader("C:\\Users\\Administrator\\Desktop\\countTest\\count").getImage();
		short[][] img = shortImage.getBand(1);
		System.out.println(img);
//		Algorithms.connectedDomainCount(img);
	}
}
