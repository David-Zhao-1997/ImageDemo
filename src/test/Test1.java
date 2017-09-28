package test;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.ByteImage;

public class Test1
{
	public static short[] toShortArray(byte[] src)
	{

		int count = src.length >> 1;
		short[] dest = new short[count];
		for (int i = 0; i < count; i++)
		{
			dest[i] = (short) (src[i * 2] << 8 | src[2 * i + 1] & 0xff);
		}
		return dest;
	}

	public static byte[] toByteArray(short[] src)
	{

		int count = src.length;
		byte[] dest = new byte[count << 1];
		for (int i = 0; i < count; i++)
		{
			dest[i * 2] = (byte) (src[i] >> 8);
			dest[i * 2 + 1] = (byte) (src[i] >> 0);
		}
		return dest;
	}

	public static void main(String[] args)
	{
		try
		{
			ByteImage imageUtils = new ByteImage("C:\\Users\\Administrator\\Desktop\\region");
			int samples = imageUtils.getSamples();
			int lines = imageUtils.getLines();
			short[][] b3 = new short[lines][samples];
			byte[][] band3 = imageUtils.getBand3InBytes();//[lines][samples]
			for (int i = 0; i < lines; i++)
			{
				for (int j = 0; j < samples; j++)
				{
					b3[i][j] = (short) (band3[i][j] & 0xff);
				}
			}

			//			DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\shortTest"));
			//			for (int i = 0; i < lines; i++)
			//			{
			//				for (int j = 0; j < samples; j++)
			//				{
			//					dataOutputStream.writeShort(b3[i][j]);
			//				}
			//			}
			//			dataOutputStream.flush();
			//			dataOutputStream.close();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\shortTest"));
			for (int i = 0; i < lines; i++)
			{
				bufferedOutputStream.write(toByteArray(b3[i]));
			}
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
