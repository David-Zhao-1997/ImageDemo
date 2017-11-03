package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 用于静态调用
 * @see HdrGenerator#generate(EnviImage, String, int, int, int)
 */
public class HdrGenerator
{
	/**
	 * Inku曾经写过的垃圾代码，如果调用可能会被群殴<br>
	 * Copies the original HDR and pastes
	 * @param samplePath 文件路径及名称
	 * @param filename 目标文件路径及名称
	 */
	@Deprecated
	public static void generateFake(String samplePath, String filename)
	{
		try
		{
			Files.copy(Paths.get(samplePath), Paths.get(filename));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 为Envi文件生成对应的HDR文件
	 * @param enviImage 表示图片的EnviImage及其子类对象
	 * @param filename 文件路径及名称
	 * @param ystart 垂直坐标的起始值
	 * @param xstart 水平坐标的起始值
	 * @param bands 文件包含的波段数量
	 */
	public static void generate(EnviImage enviImage, String filename, int ystart, int xstart, int bands)
	{
		String s1 = "ENVI\ndescription = {\nFile Resize Result, x resize factor: 1.000000, y resize factor: 1.000000.\n[Tue Apr 25 18:45:30 2017]}\n";
		String s2 = "samples = 300\nlines = 300\nbands= " + bands + "\n";
		String s3 = "header offset = 0\nfile type = ENVI Standard\ndata type = 2\ninterleave = bsq\nsensor type = Unknown\nbyte order = 1\n";
		String s4 = "x start= " + (xstart + 1) + "\ny start= " + (ystart + 1) + "\n";
		String s5 = "map info = {Geographic Lat/Lon, 1.0000, 1.0000," + (enviImage.getLon() + xstart * enviImage.getDeltaLon() + ",") + (enviImage.getLat() - ystart * enviImage.getDeltaLat() + ",") + enviImage.getDeltaLon() + "," + enviImage.getDeltaLat() + ",WGS-84, units=Degrees}";
		try
		{
			BufferedWriter bos = new BufferedWriter(new FileWriter(filename));
			bos.write(s1 + s2 + s3 + s4 + s5);
			bos.flush();
			bos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 重载提供自定义lines samples的generate()
	 * @param enviImage
	 * @param filename
	 * @param ystart
	 * @param xstart
	 * @param bands
	 * @param lines
	 * @param samples
	 */
	public static void generate(EnviImage enviImage, String filename, int ystart, int xstart, int bands, int lines, int samples)
	{
		String s1 = "ENVI\ndescription = {\nFile Resize Result, x resize factor: 1.000000, y resize factor: 1.000000.\n[Tue Apr 25 18:45:30 2017]}\n";
		String s2 = "samples = " + samples + "\nlines = " + lines + "\nbands= " + bands + "\n";
		String s3 = "header offset = 0\nfile type = ENVI Standard\ndata type = 2\ninterleave = bsq\nsensor type = Unknown\nbyte order = 1\n";
		String s4 = "x start= " + (xstart + 1) + "\ny start= " + (ystart + 1) + "\n";
		String s5 = "map info = {Geographic Lat/Lon, 1.0000, 1.0000," + (enviImage.getLon() + xstart * enviImage.getDeltaLon() + ",") + (enviImage.getLat() - ystart * enviImage.getDeltaLat() + ",") + enviImage.getDeltaLon() + "," + enviImage.getDeltaLat() + ",WGS-84, units=Degrees}";
		try
		{
			BufferedWriter bos = new BufferedWriter(new FileWriter(filename));
			bos.write(s1 + s2 + s3 + s4 + s5);
			bos.flush();
			bos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
