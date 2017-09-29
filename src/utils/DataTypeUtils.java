package utils;

/**
 * 静态工具类，用于byte数组和short数组互相转换
 * @author COMMENT_Inku
 * @see DataTypeUtils#toByteArray(short[])
 * @see DataTypeUtils#toShortArray(byte[])
 */
public class DataTypeUtils
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
}
