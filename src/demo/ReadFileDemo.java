package demo;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.ByteImage;

public class ReadFileDemo
{
	private static BufferedOutputStream bos;

	public static void main(String[] args) throws IOException
	{
		String path = "C:\\Users\\Administrator\\Desktop\\region";
		ByteImage hdrUtils = new ByteImage(path);
		int sample = hdrUtils.getSamples();
		int lines = hdrUtils.getLines();
		long startTime = System.currentTimeMillis();
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path), 1024 * 1024);
		int count = 0;
		double sum = 0;
		//		byte[] band1 = new byte[sample * lines];
		//		byte[] band2 = new byte[sample * lines];
		byte[] band3 = new byte[sample * lines];
		byte[] band4 = new byte[sample * lines];
		byte[] out = new byte[sample * lines];

		bis.skip(sample * lines);
		bis.skip(sample * lines);
		count = bis.read(band3);
		bis.read(band4);
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < count; i++)
		{

			int b3 = band3[i] & 0xff;
			int b4 = band4[i] & 0xff;
			int value = b4 - b3 + 100;
			out[i] = (byte) value;
			sum += value;
			if (value > max)
			{
				max = value;
			}
			if (value < min)
			{
				min = value;
			}
		}

		bos = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\0429"), 1024 * 1024);
		bos.write(out);
		bos.flush();

		System.out.println("max:" + max);
		System.out.println("min:" + min);
		System.out.println("avg:" + sum * 1.0 / count);
		long endTime = System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
		System.out.println("count:" + count);
		System.out.println(band3.length);
		bis.close();
	}
}
