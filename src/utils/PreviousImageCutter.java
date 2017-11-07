package utils;

import java.io.IOException;

/**
 * 图像切割工具<br>
 * 暂时弃用，不需要阅读
 * @author David
 *
 */
@Deprecated
public class PreviousImageCutter
{

	public static void main(String[] args) throws IOException, InterruptedException
	{
		int width = 300; //切割出的子图片的宽度
		int height = 300; //切割出的子图片的高度
		String filename = "F:\\HJ\\TEST-OUT-";
		String image = "C:\\Users\\Administrator\\Desktop\\2015-7-4";
		cut(width, height, filename, image);
	}

	/**
	 * 将名称为image的图片裁剪为width*height分辨率的文件并存放在filename路径下
	 * @param width
	 * @param height
	 * @param outputFilePath
	 * @param inputPath
	 * @throws IOException
	 */
	public static void cut(int width, int height, String inputPath, String outputFilePath) throws IOException
	{
		ByteImageFile image = new ByteImageFile(inputPath);//创建ByteImage对象
		short[][] b1 = image.convertBand1ToShorts();//获取band1并转化为short[][]类型
		short[][] b2 = image.convertBand2ToShorts();//获取band2并转化为short[][]类型
		short[][] b3 = image.convertBand3ToShorts();//获取band3并转化为short[][]类型
		short[][] b4 = image.convertBand4ToShorts();//获取band4并转化为short[][]类型
		int samples = image.getSamples();//获取源图片的宽度
		int lines = image.getLines();//获取源图片的高度
		int x = 0;//当前处理到的横坐标
		int y = 0;//当前处理到的纵坐标
		int cnt = 0;//生成的图片计数
		while (y + height <= lines)//控制纵坐标范围
		{
			x = 0;
			while (x + width <= samples)//控制横坐标范围
			{
				boolean flag = false;//写入文件标记 若图片全为黑色则false（不写入文件）反之为true（写入文件）
				int i;
				int j;
				short[][] output1 = new short[height][width];//暂存要输出的子图片的band1信息
				short[][] output2 = new short[height][width];//暂存要输出的子图片的band2信息
				short[][] output3 = new short[height][width];//暂存要输出的子图片的band3信息
				short[][] output4 = new short[height][width];//暂存要输出的子图片的band4信息
				for (i = 0; i < 300; i++)
				{
					for (j = 0; j < 300; j++)
					{
						output1[i][j] = b1[i + y][j + x];
						output2[i][j] = b2[i + y][j + x];
						output3[i][j] = b3[i + y][j + x];
						output4[i][j] = b4[i + y][j + x];
						if (!(output1[i][j] == 0 && output2[i][j] == 0 && output3[i][j] == 0 && output4[i][j] == 0))
						{
							flag = true;
						}
					}
				}
				if (flag)
				{
					String fName = outputFilePath + "_" + cnt;
					ShortImageFile.addBandToFile(output1, fName);
					ShortImageFile.addBandToFile(output2, fName);
					ShortImageFile.addBandToFile(output3, fName);
					ShortImageFile.addBandToFile(output4, fName);
					HdrGenerator.generate(image, fName + ".hdr", y, x, 4);
					cnt++;
				}
				x += 300;
				flag = false;
			}
			y += 300;
		}
	}
}
