package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import utils.Pair;
import utils.ShortImage;

/**
 * 算法测试类<br>
 * 用于计算连通区域的个数
 * @see Algorithms#connectedDomainCount(short[][])
 */
public class Algorithms
{

	//	public static int rows = 4;
	//	public static int cols = 14;
	//
	//	static short[][] img = { { 0, 255, 255, 255, 255, 255, 0, 0, 0, 255, 255, 255, 255, 0 }, { 0, 0, 0, 0, 0, 255, 255, 0, 255, 255, 0, 0, 0, 0 }, { 0, 255, 255, 255, 0, 0, 255, 255, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	/**
	 * Run 团
	 */
	static final class Run
	{
		ArrayList<Integer> stRun = new ArrayList<>();//团的起点
		ArrayList<Integer> rowRun = new ArrayList<>();//团的行数
		ArrayList<Integer> enRun = new ArrayList<>();//团的终点
	}

	static final class FirstPassOutput
	{
		ArrayList<Integer> runLabels = new ArrayList<>();
		ArrayList<Pair<Integer, Integer>> equivalences = new ArrayList<>();
	}

	private static Run fillRunVectors(short[][] arr)
	{
		Run run = new Run();
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i][0] == 255)
			{
				run.stRun.add(0);
				run.rowRun.add(i);
			}
			for (int j = 1; j < arr[i].length; j++)
			{
				if (arr[i][j - 1] == 0 && arr[i][j] == 255)
				{
					run.stRun.add(j);
					run.rowRun.add(i);
				}
				else if (arr[i][j - 1] == 255 && arr[i][j] == 0)
				{
					run.enRun.add(j - 1);
				}
			}
			if (arr[i][arr[0].length - 1] == 255)
			{
				run.enRun.add(arr[0].length - 1);
			}
		}
		return run;
	}

	private static FirstPassOutput firstPass(Run run, int offset)
	{
		FirstPassOutput fpo = new FirstPassOutput();
		ArrayList<Pair<Integer, Integer>> equivalences = fpo.equivalences;
		ArrayList<Integer> runLabels = fpo.runLabels;
		ArrayList<Integer> stRun = run.stRun;
		ArrayList<Integer> enRun = run.enRun;
		ArrayList<Integer> rowRun = run.rowRun;
		int numberOfRuns = run.rowRun.size();
		for (int i = 0; i < numberOfRuns; i++)
		{
			runLabels.add(0);
		}
		int idxLabel = 1;
		int curRowIdx = 0;
		int firstRunOnCur = 0;
		int firstRunOnPre = 0;
		int lastRunOnPre = -1;
		for (int i = 0; i < numberOfRuns; i++)
		{
			//如果是当前行的第一个Run则更新上一行第一个Run和最后一个Run的序号
			if (rowRun.get(i) != curRowIdx)
			{
				curRowIdx = rowRun.get(i);
				firstRunOnPre = firstRunOnCur;
				lastRunOnPre = i - 1;
				firstRunOnCur = i;
			}
			//遍历上一行所有Run，判断是否有与当前Run重合的区域
			for (int j = firstRunOnPre; j <= lastRunOnPre; j++)
			{
				if (stRun.get(i) <= enRun.get(j) + offset && enRun.get(i) >= stRun.get(j) - offset && rowRun.get(i) == rowRun.get(j) + 1)//区域重合 并且 处于相邻的两行
				{
					if (runLabels.get(i) == 0)//没有被标号
					{
						runLabels.set(i, runLabels.get(j));
					}
					else //noinspection NumberEquality
						if (runLabels.get(i) != runLabels.get(j))//已经被标号
					{
						equivalences.add(Pair.create(runLabels.get(i), runLabels.get(j)));//保存等价对
					}
				}
			}
			if (runLabels.get(i) == 0)//没有与前一列的任何Run重合
			{
				runLabels.set(i, idxLabel++);
			}
		}
		return fpo;
	}

	private static int getRegionCount(FirstPassOutput fpo)
	{
		return Collections.max(fpo.runLabels) - fpo.equivalences.size();
	}

	/**
	 * 方法用于计算某图中连通区域的个数
	 * @param img 一个short Image（以short[][]形式）
	 * @return int 连通区域的个数
	 */
	public static int connectedDomainCount(short[][] img)
	{
//		System.out.println(img);
		Run r = fillRunVectors(img);
		//		System.out.println("stRun:" + r.stRun);
		//		System.out.println("enRun:" + r.enRun);
		//		System.out.println("rowRun:" + r.rowRun);
		FirstPassOutput fpo = firstPass(r, 1);
		//		System.out.println("runLabels:" + fpo.runLabels);
		//		System.out.println("equivalences:" + fpo.equivalences);
		//		System.out.println("regionCount:" + getRegionCount(fpo));
		return getRegionCount(fpo);
	}

	public static void main(String[] args) throws IOException
	{
		ShortImage shortImage = new ShortImage("C:\\Users\\Administrator\\Desktop\\countTest\\count");
		short[][] img = shortImage.getBand1InShorts();
		int count = connectedDomainCount(img);
		System.out.println(count);
	}
}
