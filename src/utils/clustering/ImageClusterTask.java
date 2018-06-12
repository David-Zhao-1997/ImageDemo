package utils.clustering;

import graphics.utils.UniversalMessage;
import utils.imaging.ShortSatImage;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

public class ImageClusterTask implements Callable<ClusterInfo> {

	private DataPixel[][] imageBands;
	private int groupCount;
	private int iterateTime;

	private int width;
	private int height;
	private int bandCount;

	//for algorithms
	private StatsPixel[] clusterAvgs;
	private StatsPixel[] clusterSums;
	private Random random = new Random();

	//for UI
	private UniversalMessage<Integer> iterateMessage;

	public ImageClusterTask(ShortSatImage sourceImage, int groupCount, int iterateTime, int[] bandNos) {
		this.groupCount = groupCount;
		this.iterateTime = iterateTime;
		//用于提取原数据的指定波段
		ArrayList<short[][]> sourceBandList = new ArrayList<>();
		for (int bandNo : bandNos) {
			sourceBandList.add(sourceImage.getBand(bandNo));
		}
		//初始化imageBands
		height = sourceBandList.get(0).length;
		width = sourceBandList.get(0)[0].length;
		bandCount = sourceBandList.size();
		imageBands = new DataPixel[height][width];
		//转存至imageBands
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				imageBands[row][col] = new DataPixel(bandCount);
			}
		}
		for (int i = 0; i < bandCount; i++) {
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
//					System.out.println("imageBand:");
//					System.out.println(imageBands[row][col]);
//					System.out.println("sourceBandList:");
//					System.out.println(sourceBandList);
					imageBands[row][col].setData(i, sourceBandList.get(i)[row][col]);
				}
			}
		}
		//初始化算法用量
		clusterAvgs = new StatsPixel[groupCount];
		clusterSums = new StatsPixel[groupCount];
	}

	public void setMessage(UniversalMessage<Integer> iterateMessage) {
		this.iterateMessage = iterateMessage;
	}

	@Override
	public ClusterInfo call() throws Exception {
		initRandomCenters();
		for (int iter = 0; iter < iterateTime; iter++) {
			cluster();
			recalculateCenters();
			if (iter % 50 == 0) {
				iterateMessage.setData(iter);
				synchronized (iterateMessage) {
					iterateMessage.notifyAll();
				}
			}
		}
		cluster();
		System.out.printf("最终迭代次数%d\n", iterateTime);
		printCenters();
		int[][] groups = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				groups[row][col] = imageBands[row][col].getGroup();
			}
		}
		return new ClusterInfo(groups, groupCount);
	}

	private void printCenters() {
		System.out.println("Centers:");
		for (int g = 0; g < groupCount; g++) {
			System.out.println(clusterAvgs[g].toString());
		}
	}

	/*算法*/

	//随机初始化各个中心
	private void initRandomCenters() {
		//为每一个聚类生成随机中心点
		int randY, randX;
		for (int i = 0; i < groupCount; i++) {
			DataPixel clusterAvg = new DataPixel(bandCount);
			DataPixel clusterSum = new DataPixel(bandCount);

			randY = random.nextInt(height);
			randX = random.nextInt(width);

			//将本随机中心点设置为第i类
			clusterAvg.setGroup(i);
			//获取随机中心点的实际数据
			for (int j = 0; j < bandCount; j++) {
				clusterAvg.setData(j, imageBands[randY][randX].getData(j));
			}
			//将此随机中心点加入中心点集
			clusterAvgs[i] = clusterAvg.convert();

			//将此随机中心点加入总和中
			for (int j = 0; j < bandCount; j++) {
				clusterSum.increaseData(j, clusterAvg.getData(j));
			}
//            clusterSum.setGroupCount(0);
			clusterSum.setGroupCount(1);
			//将此总和加入总和集中
			clusterSums[i] = clusterSum.convert();
		}
	}

	private int findMinDistanceIndex(double[] distances) {
		if (distances.length < 1) {
			return -1;
		}
		int minimumIndex = 0;
		double minimum = distances[minimumIndex];
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] < minimum) {
				minimum = distances[i];
				minimumIndex = i;
			} else if (distances[i] == minimum) {
				if (random.nextBoolean()) {
					minimumIndex = i;
				}
			}
		}
		return minimumIndex;
	}

	private void recalculateCenters() {
		for (int g = 0; g < groupCount; g++) {
			//计算新中心点
			for (int b = 0; b < bandCount; b++) {
				clusterAvgs[g].setData(b, (short) calcClusterAvg(g, b));
			}
			//重置Sum
			for (int b = 0; b < bandCount; b++) {
				clusterSums[g].setData(b, clusterAvgs[g].getData(b));
			}
//            clusterSums[g].setGroupCount(0);
			clusterSums[g].setGroupCount(1);
		}
	}

	private void cluster() {
		int minIndex;
		//存储"当前点"距离各个中心点的距离
		double distances[] = new double[groupCount];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				for (int i = 0; i < groupCount; i++) {
					//(为每一个点)填入本点到每个中心点的距离
					distances[i] = imageBands[row][col].distanceTo(clusterAvgs[i]);
				}
				//寻找距离最小的中心点并将该点分类进其中
				minIndex = findMinDistanceIndex(distances);
				imageBands[row][col].setGroup(minIndex);
				//加入计算和中
				for (int b = 0; b < bandCount; b++) {
					clusterSums[minIndex].increaseData(b, imageBands[row][col].getData(b));
				}
				clusterSums[minIndex].increaseGroupCount();
			}
		}
	}

	private double calcClusterAvg(int groupNo, int band) {
		return (clusterSums[groupNo].getData(band)) / (clusterSums[groupNo].getGroupCount());
	}


	/*/算法*/

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public int getIterateTime() {
		return iterateTime;
	}

	public void setIterateTime(int iterateTime) {
		this.iterateTime = iterateTime;
	}

}

class DataPixel {

	private short[] bandPixel;
	private int clusterGroup;

	public DataPixel(int bandCount) {
		bandPixel = new short[bandCount];
	}

	public DataPixel(short[] bandPixel) {
		this.bandPixel = bandPixel;
	}

	public double distanceTo(StatsPixel that) {
		if (this.bandPixel.length != that.getLength()) {
			return -1;
		}
		double distance = 0;
		for (int i = 0; i < this.bandPixel.length; i++) {
			distance += sq(this.bandPixel[i] - that.getData(i));
		}
		return distance;
	}

	public StatsPixel convert() {
		long[] statsPixels = new long[bandPixel.length];
		for (int i = 0; i < bandPixel.length; i++) {
			statsPixels[i] = bandPixel[i];
		}
		StatsPixel statsPixel = new StatsPixel(statsPixels);
		statsPixel.setGroup(clusterGroup);
		return statsPixel;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("{");
		s.append("[");
		for (int i = 0; i < bandPixel.length; i++) {
			s.append(bandPixel[i]);
			if (i + 1 < bandPixel.length) {
				s.append(",");
			}
		}
		s.append("],").append(clusterGroup).append("}");
		return s.toString();
	}

	private double sq(long value) {
		return value * value;
	}

	public short getData(int band) {
		assert bandPixel[band] > -1;
		return bandPixel[band];
	}

	public int getGroup() {
		return clusterGroup;
	}

	public void setGroup(int clusterGroup) {
		this.clusterGroup = clusterGroup;
	}

	public void setData(int band, short data) {
		this.bandPixel[band] = data;
	}

	public void increaseData(int band, short increment) {
		bandPixel[band] += increment;
	}

	public int getGroupCount() {
		return clusterGroup;
	}

	public void setGroupCount(int count) {
		this.clusterGroup = count;
	}

	public void increaseGroupCount(int increment) {
		this.clusterGroup += increment;
	}

	public void increaseGroupCount() {
		this.clusterGroup += 1;
	}
}

/**
 * 专门为统计数据使用的对象，因short长度不够
 */
class StatsPixel {

	private long[] bandPixel;
	private int clusterGroup;

	public StatsPixel(int bandCount) {
		bandPixel = new long[bandCount];
	}

	public StatsPixel(long[] bandPixel) {
		this.bandPixel = bandPixel;
	}

//	public double distanceTo(DataPixel that) {
//		if (this.bandPixel.length != that.bandPixel.length)
//			return -1;
//		double distance = 0;
//		for (int i = 0; i < this.bandPixel.length; i++) {
//			distance += sq(this.bandPixel[i] - that.bandPixel[i]);
//		}
//		return distance;
//	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("Stats:{");
		s.append("[");
		for (int i = 0; i < bandPixel.length; i++) {
			s.append(bandPixel[i]);
			if (i + 1 < bandPixel.length) {
				s.append(",");
			}
		}
		s.append("],").append(clusterGroup).append("}");
		return s.toString();
	}

	private double sq(int value) {
		return value * value;
	}

	public long getData(int band) {
		assert bandPixel[band] > -1;
		return bandPixel[band];
	}

	public int getGroup() {
		return clusterGroup;
	}

	public void setGroup(int clusterGroup) {
		this.clusterGroup = clusterGroup;
	}

	public void setData(int band, long data) {
		this.bandPixel[band] = data;
	}

	public void increaseData(int band, short increment) {
		bandPixel[band] += increment;
	}

	public int getGroupCount() {
		return clusterGroup;
	}

	public void setGroupCount(int count) {
		this.clusterGroup = count;
	}

	public void increaseGroupCount(int increment) {
		this.clusterGroup += increment;
	}

	public void increaseGroupCount() {
		this.clusterGroup += 1;
	}

	public int getLength() {
		return bandPixel.length;
	}
}
