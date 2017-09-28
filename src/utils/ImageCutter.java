package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图像切割工具
 * 
 * @author David
 *
 */
public class ImageCutter
{

	public static void main(String[] args) throws IOException, InterruptedException
	{
		int width = 300; // 切割出的子图片的宽度
		int height = 300; // 切割出的子图片的高度
		String inputFilePath = "C:\\res";
		String outputFilePath = "D:\\HJ\\out";
		cutAndMinus(width, height, inputFilePath, outputFilePath);
	}

	/**
	 * 将名称为image的图片裁剪为width*height分辨率的文件并存放在filename路径下
	 * <br>
	 * HDR的生成是自动的
	 * @param width
	 *            裁剪的子图像的宽度
	 * @param height
	 *            裁剪的子图像的高度
	 * @param outputFilePath
	 *            输出文件的路径
	 * @param inputFilePath
	 *            需要被裁剪文件的路径
	 * @throws IOException
	 */
	public static void cut(int width, int height, String inputFilePath, String outputFilePath) throws IOException
	{
		ByteImage image = new ByteImage(inputFilePath);// 创建ByteImage对象
		short[][] b1 = image.convertBand1ToShorts();// 获取band1并转化为short[][]类型
		short[][] b2 = image.convertBand2ToShorts();// 获取band2并转化为short[][]类型
		short[][] b3 = image.convertBand3ToShorts();// 获取band3并转化为short[][]类型
		short[][] b4 = image.convertBand4ToShorts();// 获取band4并转化为short[][]类型
		int samples = image.getSamples();// 获取源图片的宽度
		int lines = image.getLines();// 获取源图片的高度
		int x = 0;// 当前处理到的横坐标
		int y = 0;// 当前处理到的纵坐标
		int cnt = 0;// 生成的图片计数
		while (y + height <= lines)// 控制纵坐标范围
		{
			x = 0;
			while (x + width <= samples)// 控制横坐标范围
			{
				boolean flag = false;// 写入文件标记 若图片全为黑色则false（不写入文件）反之为true（写入文件）
				int i;
				int j;
				short[][] output1 = new short[height][width];// 暂存要输出的子图片的band1信息
				short[][] output2 = new short[height][width];// 暂存要输出的子图片的band2信息
				short[][] output3 = new short[height][width];// 暂存要输出的子图片的band3信息
				short[][] output4 = new short[height][width];// 暂存要输出的子图片的band4信息
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
					ShortImage.addBandToFile(output1, fName);
					ShortImage.addBandToFile(output2, fName);
					ShortImage.addBandToFile(output3, fName);
					ShortImage.addBandToFile(output4, fName);
					HdrGenerator.generate(image, fName + ".hdr", y, x, 4);
					cnt++;
				}
				x += 300;
				flag = false;
			}
			y += 300;
		}
	}

	/**
	 * 将名称为image的图片裁剪为width*height分辨率的文件并在进行(band4-band3+255)处理后将文件存放在filename路径下
	 * <br>
	 * HDR的生成是自动的
	 * @param width
	 *            裁剪的子图像的宽度
	 * @param height
	 *            裁剪的子图像的高度
	 * @param outputFilePath
	 *            输出文件的路径
	 * @param inputFilePath
	 *            需要被裁剪文件的路径
	 * @throws IOException
	 */
	public static void cutAndMinus(int width, int height, String inputFilePath, String outputFilePath) throws IOException
	{
		File file = new File(outputFilePath);
		String parentPath = file.getParent();
		File stat = new File(parentPath + File.separator + "statistics.txt");
		ArrayList<Double> avg = new ArrayList<Double>();
		ByteImage sourceImage = new ByteImage(inputFilePath);// 创建ByteImage对象
		short[][] b3 = sourceImage.convertBand3ToShorts();// 获取band3并转化为short[][]类型
		short[][] b4 = sourceImage.convertBand4ToShorts();// 获取band4并转化为short[][]类型
		int samples = sourceImage.getSamples();// 获取源图片的宽度
		int lines = sourceImage.getLines();// 获取源图片的高度
		int x = 0;// 当前处理到的横坐标
		int y = 0;// 当前处理到的纵坐标
		int cnt = 0;// 生成的图片计数
		while (y + height <= lines)// 控制纵坐标范围
		{
			x = 0;
			while (x + width <= samples)// 控制横坐标范围
			{
				boolean flag = false;// 写入文件标记 若图片全为黑色则false（不写入文件）反之为true（写入文件）
				int avgCount = 0;//子图像中两波段不全为黑的像素数量
				long sum = 0;//子图像中两波段不全为黑的像素的运算结果之和
				int i;
				int j;
				short[][] output3 = new short[height][width];// 暂存要输出的子图片的band3信息
				short[][] output4 = new short[height][width];// 暂存要输出的子图片的band4信息
				short[][] diff = new short[height][width];//存储运算结果
				for (i = 0; i < 300; i++)//控制子图像的纵坐标范围
				{
					for (j = 0; j < 300; j++)//控制子图像的横坐标范围
					{
						output3[i][j] = b3[i + y][j + x];
						output4[i][j] = b4[i + y][j + x];
						if (!(output3[i][j] == 0 && output4[i][j] == 0))//如果像素点两个波段不全黑
						{
							diff[i][j] = (short) (output4[i][j] - output3[i][j] + 255);//计算结果
							flag = true;//写入文件标记
							sum += diff[i][j];
							avgCount++;
						}
						else
						{
							diff[i][j] = 0;
						}
					}
				}
				if (flag)
				{
					avg.add((double) sum / (double) avgCount);
					String filename = outputFilePath + File.separator + file.getName() + "_" + cnt;
					ShortImage.addBandToFile(diff, filename);
					HdrGenerator.generate(sourceImage, filename + ".hdr", y, x, 1);
					cnt++;
				}
				x += 300;
				flag = false;
			}//while x
			y += 300;
		}//while y
		BufferedWriter writer = new BufferedWriter(new FileWriter(stat));
		int i = 0;
		for (Double avg0 : avg)
		{
			writer.write(i + ": " + avg0);
			writer.newLine();
			i++;
		}
		writer.flush();
		writer.close();
	}
}
