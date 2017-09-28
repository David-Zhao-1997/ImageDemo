package test;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Test2
{
	private static DataInputStream dataInputStream;

	public static void main(String[] args) throws IOException
	{
		dataInputStream = new DataInputStream(new FileInputStream("C:\\Users\\Administrator\\Desktop\\shortTest"));
		int i = dataInputStream.available() / 2;
		for (int a = 0; a < i; a++)
		{
			int x = dataInputStream.readShort();
			if (x < 0)
			{
				System.out.println(x);
			}
		}
	}
}
