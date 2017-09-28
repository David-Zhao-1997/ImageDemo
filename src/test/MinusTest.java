package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.ByteImage;
import utils.HdrGenerator;
import utils.ShortImage;

public class MinusTest
{
	public static void main(String[] args) throws IOException
	{
		ByteImage image = new ByteImage("C:\\Users\\Administrator\\Desktop\\cut\\2");
		String outfilename = "C:\\Users\\Administrator\\Desktop\\cut\\TEST-OUT-";
		File stat = new File("C:\\Users\\Administrator\\Desktop\\cut\\statictics.txt");
		ArrayList<Double> avg = new ArrayList<Double>();
		short[][] b3 = image.getBandInShorts(3);
		short[][] b4 = image.getBandInShorts(4);
		int samples = image.getSamples();
		int lines = image.getLines();
		int x = 0;
		int y = 0;
		int cnt = 0;
		while (y + 300 <= lines)
		{
			x = 0;
			while (x + 300 <= samples)
			{
				boolean flag = false;
				int avgcount = 0;
				long sum = 0;
				int i;
				int j;
				short[][] output3 = new short[300][300];
				short[][] output4 = new short[300][300];
				short[][] difference = new short[300][300];
				for (i = 0; i < 300; i++)
				{
					for (j = 0; j < 300; j++)
					{
						output3[i][j] = b3[i + y][j + x];
						output4[i][j] = b4[i + y][j + x];
						if (!(output3[i][j] == 0 && output4[i][j] == 0))
						{
							difference[i][j] = (short) (output4[i][j] - output3[i][j] + 255);
							flag = true;
							sum += difference[i][j];
							avgcount++;
						}
						else
						{
							difference[i][j] = 0;
						}
					}
				}
				if (flag)
				{
					avg.add((double) sum / (double) avgcount);
					String filename = outfilename + cnt;
					ShortImage.addBandToFile(difference, outfilename + cnt);
					HdrGenerator.generate(image, filename + ".hdr", y, x, 1);
					cnt++;
				}
				x += 300;
				flag = false;
			}
			y += 300;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(stat));
		int i = 0;
		for (Double avg0 : avg)
		{
			writer.write(outfilename + i + ": " + avg0);
			writer.newLine();
			i++;
		}
		writer.flush();
		writer.close();
	}
}
