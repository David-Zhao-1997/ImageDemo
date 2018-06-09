package utils.clustering;

import utils.imaging.ShortSatImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ClusterInfo {

	public static Color[] DEFAULT_COLORS;

	static {
		DEFAULT_COLORS = new Color[]{
				new Color(255, 0, 0),
				new Color(0, 255, 0),
				new Color(0, 0, 255),
				new Color(255, 165, 0),
				new Color(255, 94, 247),
				new Color(127, 127, 127),
		};
	}

	private int groupCount;
	private int[][] groups;
	private int width;
	private int height;

	public ClusterInfo(int[][] groups, int groupCount) {
		this.groups = groups;
		this.groupCount = groupCount;
		height = groups.length;
		width = groups[0].length;
	}

	/**
	 * 会自动扫描整个groups数组并取最大的组数
	 *
	 * @param groups
	 */
	public ClusterInfo(int[][] groups) {
		this.groups = groups;
		int max = 0;
		for (int[] line : groups) {
			for (int pixel : line) {
				if (pixel > max)
					max = pixel;
			}
		}
		groupCount = max + 1;
		height = groups.length;
		width = groups[0].length;
	}

	/**
	 * 传入原来的图像以初始化HDR信息，但将Bands替换为聚类结果
	 *
	 * @param origin 原来的图像
	 * @return
	 */
	public ShortSatImage convertIntoShortSatImage(ShortSatImage origin) {
		ArrayList<short[][]> bands = new ArrayList<>();
		for (int i = 0; i < groupCount; i++) {
			bands.add(new short[height][width]);
		}
		for (int i = 0; i < groupCount; i++) {
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					bands.get(getGroup(row, col))[row][col] = 255;
				}
			}
		}
		ShortSatImage shortSatImage = new ShortSatImage(origin, bands);
		shortSatImage.setBandCount(groupCount);
		return shortSatImage;
	}

	public BufferedImage convertIntoBufferedImage(Color[] colors) {
		if (colors.length < groupCount)
			throw new IllegalArgumentException("Insufficient color array length");
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				bufferedImage.setRGB(col, row, colors[groups[row][col]].getRGB());
			}
		}
		return bufferedImage;
	}

	public int getGroup(int row, int col) {
		return groups[row][col];
	}
}
