package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 继承自EnviImage, 表示以Byte存储的图像文件, 并提供若干方法
 * @author David
 * @author COMMENT_Inku
 */
public class ByteImage extends EnviImage
{

	public ByteImage(String filename) throws IOException
	{
		super(filename);
	}

	private byte[][] band1;
	private byte[][] band2;
	private byte[][] band3;
	private byte[][] band4;
	private short[][] b1;
	private short[][] b2;
	private short[][] b3;
	private short[][] b4;
	Object[] band = { band1, band2, band3, band4 };
	Object[] b = { b1, b2, b3, b4 };

	/**
	 * 请不要调用本方法 <br>
	 * This method accepts an <code>int</code> and converts the corresponding band into
	 * <code>byte[][]</code>
	 * @param x starts from 1
	 * @return
	 * @throws IOException
	 */
	public byte[][] getBandInBytes(int x) throws IOException
	{
		byte[][] band = null;
		switch (x)
		{
			case 1:
				band = band1;
				break;
			case 2:
				band = band2;
				break;
			case 3:
				band = band3;
				break;
			case 4:
				band = band4;
				break;
		}
		bis = new BufferedInputStream(new FileInputStream(path));
		for (int i = 1; i < x; i++)
			bis.skip(samples * lines);
		if (band == null)
		{
			band = new byte[lines][samples];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band[i]);
		}
		bis.close();
		return band;
	}

	/**
	 * 请不要调用本方法 <br>
	 * This method accepts an <code>int</code> and converts the corresponding band into
	 * <code>short[][]</code>
	 * @param x starts from 1
	 * @return
	 * @throws IOException
	 */
	public short[][] getBandInShorts(int x) throws IOException
	{
		short[][] b = null;
		byte[][] band = null;
		switch (x)
		{
			case 1:
				b = b1;
				band = band1;
				break;
			case 2:
				b = b2;
				band = band2;
				break;
			case 3:
				b = b3;
				band = band3;
				break;
			case 4:
				b = b4;
				band = band4;
				break;
		}
		if (b == null)
		{
			band = getBandInBytes(x);
			b = new short[lines][samples];
			for (int i = 0; i < lines; i++)
			{
				for (int j = 0; j < samples; j++)
				{
					b[i][j] = (short) (band[i][j] & 0xff);
				}
			}
		}
		return b;
	}

	/**
	 * 获取band1数组
	 * @return 包含band1数据的二维数组byte[lines][samples]
	 * @throws IOException
	 */
	public byte[][] getBand1InBytes() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		if (band1 == null)
		{
			band1 = new byte[lines][samples];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band1[i]);
		}
		bis.close();
		return band1;
	}

	/**
	 * 获取band2数组
	 * @return 包含band2数据的二维数组byte[lines][samples]
	 * @throws IOException
	 */
	public byte[][] getBand2InBytes() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		bis.skip(samples * lines);
		if (band2 == null)
		{
			band2 = new byte[lines][samples];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band2[i]);
		}
		bis.close();
		return band2;
	}

	/**
	 * 获取band3数组
	 * @return 包含band3数据的二维数组byte[lines][samples]
	 * @throws IOException
	 */
	public byte[][] getBand3InBytes() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		bis.skip(samples * lines);
		bis.skip(samples * lines);
		if (band3 == null)
		{
			band3 = new byte[lines][samples];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band3[i]);
		}
		bis.close();
		return band3;
	}

	/**
	 * 获取band4数组
	 * @return 包含band4数据的二维数组byte[lines][samples]
	 * @throws IOException
	 */
	public byte[][] getBand4InBytes() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		bis.skip(samples * lines);
		bis.skip(samples * lines);
		bis.skip(samples * lines);
		if (band4 == null)
		{
			band4 = new byte[lines][samples];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band4[i]);
		}
		bis.close();
		return band4;
	}

	public short[][] convertBand1ToShorts() throws IOException
	{
		if (b1 == null)
		{
			getBand1InBytes();
			b1 = new short[lines][samples];
			for (int i = 0; i < lines; i++)
			{
				for (int j = 0; j < samples; j++)
				{
					b1[i][j] = (short) (band1[i][j] & 0xff);
				}
			}
		}
		return b1;
	}

	public short[][] convertBand2ToShorts() throws IOException
	{
		if (b2 == null)
		{
			getBand2InBytes();
			b2 = new short[lines][samples];
			for (int i = 0; i < lines; i++)
			{
				for (int j = 0; j < samples; j++)
				{
					b2[i][j] = (short) (band2[i][j] & 0xff);
				}
			}
		}
		return b2;
	}

	public short[][] convertBand3ToShorts() throws IOException
	{
		if (b3 == null)
		{
			getBand3InBytes();
			b3 = new short[lines][samples];
			for (int i = 0; i < lines; i++)
			{
				for (int j = 0; j < samples; j++)
				{
					b3[i][j] = (short) (band3[i][j] & 0xff);
				}
			}
		}
		return b3;
	}

	public short[][] convertBand4ToShorts() throws IOException
	{
		if (b4 == null)
		{
			getBand4InBytes();
			b4 = new short[lines][samples];
			for (int i = 0; i < lines; i++)
			{
				for (int j = 0; j < samples; j++)
				{
					b4[i][j] = (short) (band4[i][j] & 0xff);
				}
			}
		}
		return b4;
	}

	@Deprecated
	public static void addBandtoFile(byte[][] band, String path) throws IOException
	{
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path, true), 1024 * 1024);
		for (int i = 0; i < band.length; i++)
		{
			out.write(band[i]);
		}
		out.flush();
		out.close();
	}

}
