package test;

import utils.ShortImage;

public class CountTest
{
	public static void main(String[] args) throws Exception
	{
		//		ByteImage byteImage = new ByteImage("C:\\Users\\Administrator\\Desktop\\region");
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
		//		ShortImage.saveBandsToFile("C:\\Users\\Administrator\\Desktop\\countTest\\count", band);



		ShortImage shortImage = new ShortImage("C:\\Users\\Administrator\\Desktop\\countTest\\count");
		short[][] img = shortImage.getBand1InShorts();
		System.out.println(img);
//		Algorithms.connectedDomainCount(img);
	}
}
