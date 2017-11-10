package utils.tools;

import utils.functional.ShortImageWriter;
import utils.functional.ZiYuan3Reader;
import utils.imaging.ShortSatImage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 图像切割工具
 *
 * @author David
 */
@SuppressWarnings("ALL")
public class ImageCutter
{

	public static void main(String[] args) throws IOException
	{
//		int width = 300; // 切割出的子图片的宽度
//		int height = 300; // 切割出的子图片的高度
//		String inputFilePath = "C:\\res";
//		String outputFilePath = "D:\\HJ\\out";
//		minusAndCut(width, height, inputFilePath, outputFilePath);

		int width = 300; // 切割出的子图片的宽度
		int height = 300; // 切割出的子图片的高度
		String inputFilePath = "D:\\Documents\\宣墨白\\intell\\111";
//		String outputFilePath = "D:\\Documents\\宣墨白\\intell\\out\\out";
		String outputFilePath = "D:\\Documents\\宣墨白\\intell\\out\\out_diff";
		ShortSatImage image = new ZiYuan3Reader(inputFilePath).getImage();
		ShortSatImage diff = minusAndCut(image);
		image = null;
		cut(300, 300, diff, outputFilePath, 255);
//		cut(width, height, image, outputFilePath);
	}

	/**
	 * 等价于cut(width,height,image,outputFilePath,0)
	 * @param width
	 * @param height
	 * @param image
	 * @param outputFilePath
	 * @throws IOException
	 * @see ImageCutter#cut(int, int, ShortSatImage, String, int)
	 */
	public static void cut(int width, int height, ShortSatImage image, String outputFilePath) throws IOException
	{
		cut(width,height,image,outputFilePath,0);
	}


	/**
	 * 将名称为image的图片裁剪为width*height分辨率的文件并存放在filename路径下
	 * <br>
	 * HDR的生成是自动的
	 *
	 * @param width          裁剪的子图像的宽度
	 * @param height         裁剪的子图像的高度
	 * @param outputFilePath 输出文件的路径
	 * @param image          需要被裁剪的图片
	 * @param blankValue     默认为0，需要定义其他空白值时传入，如255
	 * @throws IOException
	 */
	public static void cut(int width, int height, ShortSatImage image, String outputFilePath, int blankValue) throws IOException
	{
//		System.out.println("Width Height " + width + " " + height);
		//使用Reader获取原图封装为Image后获取ArrayList
		ArrayList<short[][]> bands = image.getBands();

		//控制变量
		int samples = image.getSamples();//列数
		int lines = image.getLines();//行数
//		System.out.println("samples:" + samples);
		int x = 0, y = 0;//当前处理到的x, y坐标
		int count = 0;//当前处理到的图片编号

		while (y + height <= lines)//控制行
		{
			x = 0;
			while (x + width <= samples)//控制列
			{
				//对每个小图的处理
//				System.out.println("小图处理开始");
				boolean isBlank = true;//默认假设空白
				int i, j;
				ArrayList<short[][]> output = new ArrayList<>();
				for (short[][] band : bands)//对每个band分别处理
				{
					short[][] tempBand = new short[height][width];
					for (i = 0; i < height; i++)
					{
						for (j = 0; j < width; j++)
						{
//							System.out.println("i,j " + i + " " + j);
//							System.out.println("x,y " + x + " " + y);
							tempBand[i][j] = band[i + y][j + x];
							if (!(tempBand[i][j] == blankValue))
							{
								isBlank = false;
							}
						}//for width
					}//for height
					output.add(tempBand);
				}// each band
				if (!isBlank)
				{
//					System.out.println("处理输出：x:" + x + " y:" + y);
					String filename = outputFilePath + "_" + count;
					//calculate geographic
					double lon = image.getLon() + x * image.getDeltaLon();
					double lat = image.getLat() - y * image.getDeltaLat();
					ShortSatImage outImage = new ShortSatImage(image.getBandCount(), width, height, x, y,
							lon, lat, image.getDeltaLon(), image.getDeltaLat(), output);
					ShortImageWriter out = new ShortImageWriter(outImage);
					out.write(filename);
					count++;
//					System.out.println("Image original: " + image.getLon() + " " + image.getLat() + " " + image.getDeltaLon() + " " + image.getDeltaLat());
//					System.out.println("HDR Info: " + lon + " " + lat + " " + image.getDeltaLon() + " " + image.getDeltaLat());
				}
				x += width;
			}//while samples
			y += height;
		}//while lines

//		ByteImageFile image = new ByteImageFile(inputFilePath);// 创建ByteImage对象
//		short[][] b1 = image.convertBand1ToShorts();// 获取band1并转化为short[][]类型
//		short[][] b2 = image.convertBand2ToShorts();// 获取band2并转化为short[][]类型
//		short[][] b3 = image.convertBand3ToShorts();// 获取band3并转化为short[][]类型
//		short[][] b4 = image.convertBand4ToShorts();// 获取band4并转化为short[][]类型
//		int samples = image.getSamples();// 获取源图片的宽度
//		int lines = image.getLines();// 获取源图片的高度
//		int x = 0;// 当前处理到的横坐标
//		int y = 0;// 当前处理到的纵坐标
//		int cnt = 0;// 生成的图片计数
//		while (y + height <= lines)// 控制纵坐标范围
//		{
//			x = 0;
//			while (x + width <= samples)// 控制横坐标范围
//			{
//				boolean flag = false;// 写入文件标记 若图片全为黑色则false（不写入文件）反之为true（写入文件）
//				int i;
//				int j;
//				short[][] output1 = new short[height][width];// 暂存要输出的子图片的band1信息
//				short[][] output2 = new short[height][width];// 暂存要输出的子图片的band2信息
//				short[][] output3 = new short[height][width];// 暂存要输出的子图片的band3信息
//				short[][] output4 = new short[height][width];// 暂存要输出的子图片的band4信息
//				for (i = 0; i < height; i++)
//				{
//					for (j = 0; j < width; j++)
//					{
//						output1[i][j] = b1[i + y][j + x];
//						output2[i][j] = b2[i + y][j + x];
//						output3[i][j] = b3[i + y][j + x];
//						output4[i][j] = b4[i + y][j + x];
//						if (!(output1[i][j] == 0 && output2[i][j] == 0 && output3[i][j] == 0 && output4[i][j] == 0))
//						{
//							flag = true;
//						}
//					}
//				}
//				if (flag)
//				{
//					String fName = outputFilePath + "_" + cnt;
//					ShortImageFile.addBandToFile(output1, fName);
//					ShortImageFile.addBandToFile(output2, fName);
//					ShortImageFile.addBandToFile(output3, fName);
//					ShortImageFile.addBandToFile(output4, fName);
//					HdrGenerator.generate(image, fName + ".hdr", y, x, 4, height, width);
//					cnt++;
//				}
//				x += width;
//				flag = false;
//			}
//			y += height;
//		}
	}

	//TODO(Inku) 待 Adapt to 大重构 使用前请根据目前的代码规则重构

	/**
	 * 将名称为image的图片裁剪为width*height分辨率的文件并在进行(band4-band3+255)处理后将文件存放在filename路径下
	 * <br>
	 * HDR的生成是自动的
	 *
	 * @param width          裁剪的子图像的宽度
	 * @param height         裁剪的子图像的高度
	 * @throws IOException
	 */
	public static ShortSatImage minusAndCut(ShortSatImage image) throws IOException
	{
		short[][] band3 = image.getBand(3);
		short[][] band4 = image.getBand(4);
		int lines = image.getLines(), samples = image.getSamples();
		short[][] diff = new short[lines][samples];
		for (int i = 0; i < lines; i++)
		{
			for (int j = 0; j < samples; j++)
			{
				diff[i][j] = (short) (band4[i][j] - band3[i][j] + 255);
			}
		}
		ArrayList<short[][]> tempImage = new ArrayList<>();
		tempImage.add(diff);
		ShortSatImage diffImage = new ShortSatImage(image, tempImage);
		diffImage.setBandCount(1);
		return diffImage;
//		cut(width, height, diffImage, outputFilePath, 255);
//		File file = new File(outputFilePath);
//		String parentPath = file.getParent();
//		File stat = new File(parentPath + File.separator + "statistics.txt");
//		ArrayList<Double> avg = new ArrayList<Double>();
//		ByteImageFile sourceImage = new ByteImageFile(inputFilePath);// 创建ByteImage对象
//		short[][] b3 = sourceImage.convertBand3ToShorts();// 获取band3并转化为short[][]类型
//		short[][] b4 = sourceImage.convertBand4ToShorts();// 获取band4并转化为short[][]类型
//		int samples = sourceImage.getSamples();// 获取源图片的宽度
//		int lines = sourceImage.getLines();// 获取源图片的高度
//		int x = 0;// 当前处理到的横坐标
//		int y = 0;// 当前处理到的纵坐标
//		int cnt = 0;// 生成的图片计数
//		while (y + height <= lines)// 控制纵坐标范围
//		{
//			x = 0;
//			while (x + width <= samples)// 控制横坐标范围
//			{
//				boolean flag = false;// 写入文件标记 若图片全为黑色则false（不写入文件）反之为true（写入文件）
//				int avgCount = 0;//子图像中两波段不全为黑的像素数量
//				long sum = 0;//子图像中两波段不全为黑的像素的运算结果之和
//				int i;
//				int j;
//				short[][] output3 = new short[height][width];// 暂存要输出的子图片的band3信息
//				short[][] output4 = new short[height][width];// 暂存要输出的子图片的band4信息
//				short[][] diff = new short[height][width];//存储运算结果
//				for (i = 0; i < 300; i++)//控制子图像的纵坐标范围
//				{
//					for (j = 0; j < 300; j++)//控制子图像的横坐标范围
//					{
//						output3[i][j] = b3[i + y][j + x];
//						output4[i][j] = b4[i + y][j + x];
//						if (!(output3[i][j] == 0 && output4[i][j] == 0))//如果像素点两个波段不全黑
//						{
//							diff[i][j] = (short) (output4[i][j] - output3[i][j] + 255);//计算结果
//							flag = true;//写入文件标记
//							sum += diff[i][j];
//							avgCount++;
//						}
//						else
//						{
//							diff[i][j] = 0;
//						}
//					}
//				}
//				if (flag)
//				{
//					avg.add((double) sum / (double) avgCount);
//					String filename = outputFilePath + File.separator + file.getName() + "_" + cnt;
//					ShortImageFile.addBandToFile(diff, filename);
//					HdrGenerator.generate(sourceImage, filename + ".hdr", y, x, 1);
//					cnt++;
//				}
//				x += 300;
//				flag = false;
//			}//while x
//			y += 300;
//		}//while y
//		BufferedWriter writer = new BufferedWriter(new FileWriter(stat));
//		int i = 0;
//		for (Double avg0 : avg)
//		{
//			writer.write(i + ": " + avg0);
//			writer.newLine();
//			i++;
//		}
//		writer.flush();
//		writer.close();
	}
}
