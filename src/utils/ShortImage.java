package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShortImage extends EnviImage
{
	private byte[][] band1;
	private byte[][] band2;
	private byte[][] band3;
	private byte[][] band4;
	private short[][] b1;
	private short[][] b2;
	private short[][] b3;
	private short[][] b4;

	public ShortImage(String filename) throws IOException
	{
		super(filename);
	}

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
			b = new short[lines][samples];
		}
		if (band == null)
		{
			band = new byte[lines][samples * 2];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band[i]);
			b[i] = DatatypesUtils.toShortArray(band[i]);
		}
		bis.close();
		return b;
	}

	public short[][] getBand1InShorts() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		if (b1 == null)
		{
			b1 = new short[lines][samples];
		}
		if (band1 == null)
		{
			band1 = new byte[lines][samples * 2];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band1[i]);
			b1[i] = DatatypesUtils.toShortArray(band1[i]);
		}
		bis.close();
		return b1;
	}

	public short[][] getBand2InShorts() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		if (b2 == null)
		{
			b2 = new short[lines][samples];
		}
		if (band2 == null)
		{
			band2 = new byte[lines][samples * 2];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band2[i]);
			b2[i] = DatatypesUtils.toShortArray(band2[i]);
		}
		bis.close();
		return b2;
	}

	public short[][] getBand3InShorts() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		if (b3 == null)
		{
			b3 = new short[lines][samples];
		}
		if (band3 == null)
		{
			band3 = new byte[lines][samples * 2];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band3[i]);
			b3[i] = DatatypesUtils.toShortArray(band3[i]);
		}
		bis.close();
		return b3;
	}

	public short[][] getBand4InShorts() throws IOException
	{
		bis = new BufferedInputStream(new FileInputStream(path));
		if (b4 == null)
		{
			b4 = new short[lines][samples];
		}
		if (band4 == null)
		{
			band4 = new byte[lines][samples * 2];
		}
		for (int i = 0; i < lines; i++)
		{
			bis.read(band4[i]);
			b4[i] = DatatypesUtils.toShortArray(band4[i]);
		}
		bis.close();
		return b4;
	}

	/**
	 * 向指定文件中添加波段 文件存在将进行追加
	 * @param band short[][]
	 * @param path String
	 * @throws IOException
	 */
	public static void addBandToFile(short[][] band, String path) throws IOException
	{
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path, true), 1024 * 1024);
		for (int i = 0; i < band.length; i++)
		{
			out.write(DatatypesUtils.toByteArray(band[i]));
		}
		out.flush();
		out.close();
	}

	/**
	 * 一次性将一个或多个波段写入文件 若文件存在将进行覆盖
	 * @param path 传入保存的目标文件名称
	 * @param s 按顺序传入波段数组(可变长参数)</br><b>saveBandsToFile(path,band1,band2,band3,...)</b>有几个写几个 
	 * @throws IOException
	 */
	public static void saveBandsToFile(String path, short[][]... s) throws IOException
	{
		if (s.length == 0)
		{
			throw new RuntimeException("At least pass one band as an parameter 至少传入一个波段数组");
		}
		File file = new File(path);
		if (file.exists())
		{
			file.delete();
		}
		for (short[][] ts : s)
		{
			addBandToFile(ts, path);
		}
	}
}
