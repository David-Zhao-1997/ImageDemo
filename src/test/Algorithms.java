package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import utils.Pair;
import utils.ShortImage;

/**
 * 算法测试类<br>
 * 用于计算连通区域的个数
 *
 * @see Algorithms#connectedDomainCount(short[][])
 */
@SuppressWarnings("All")
public class Algorithms
{

    //	public static int rows = 4;
    //	public static int cols = 14;
    //
    //	static short[][] img = { { 0, 255, 255, 255, 255, 255, 0, 0, 0, 255, 255, 255, 255, 0 }, { 0, 0, 0, 0, 0, 255, 255, 0, 255, 255, 0, 0, 0, 0 }, { 0, 255, 255, 255, 0, 0, 255, 255, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    /**
     * Run 团
     */
    public static final class Run
    {
        ArrayList<Integer> stRun = new ArrayList<>();//团的起点
        ArrayList<Integer> rowRun = new ArrayList<>();//团的行数
        ArrayList<Integer> enRun = new ArrayList<>();//团的终点
    }

    public static final class FirstPassOutput
    {
        ArrayList<Integer> runLabels = new ArrayList<>();
        HashSet<Pair<Integer, Integer>> equivalences = new HashSet<>();
    }


    public static class Output_Data
    {
        int[][] save_Data;
        Vector<Vector> data_out_vector = new Vector<>();

        /**
         * @author Henry
         * 封装输出信息
         */
        public Output_Data()
        {
            for (int i = 0; i < 305; i++)
            {
                data_out_vector.add(new Vector<Pair<Integer, Integer>>());
            }
        }
    }

    public static Run fillRunVectors(short[][] arr)
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

    public static FirstPassOutput firstPass(Run run, int offset)
    {
        FirstPassOutput fpo = new FirstPassOutput();
        HashSet<Pair<Integer, Integer>> equivalences = fpo.equivalences;
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
                    else if (runLabels.get(i) != runLabels.get(j))//已经被标号 //noinspection NumberEquality
                    {
//                        System.out.println(runLabels.get(i)+"---"+runLabels.get(j));
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
     *
     * @param img 一个short Image（以short[][]形式）
     * @return int 连通区域的个数
     */
    public static int connectedDomainCount(short[][] img)
    {
//		System.out.println(img);
        Run r = fillRunVectors(img);
//        		System.out.println("stRun:" + r.stRun);
//        		System.out.println("enRun:" + r.enRun);
//        		System.out.println("rowRun:" + r.rowRun);
        FirstPassOutput fpo = firstPass(r, 1);
//        		System.out.println("runLabels:" + fpo.runLabels);
//        		System.out.println("equivalences:" + fpo.equivalences);
//        		System.out.println("regionCount:" + getRegionCount(fpo));
        return getRegionCount(fpo);
    }

    public static short[][] binaryProcess(ShortImage img, int threshold) throws IOException
    {
        short[][] image = img.getBand1InShorts();
        int samples = img.getSamples();
        int lines = img.getLines();
        for (int i = 0; i < lines; i++)
        {
            for (int j = 0; j < samples; j++)
            {
                if (image[i][j] < threshold)
                {
                    image[i][j] = 0;
                }
                else
                {
                    image[i][j] = 255;
                }
            }
        }
        Run r = fillRunVectors(image);
//        System.out.println("stRun:" + r.stRun);
//        System.out.println("enRun:" + r.enRun);
//        System.out.println("rowRun:" + r.rowRun);

//        ShortImage.saveBandsToFile("C:\\1619", image);
        return image;
    }

    //获取最大值的下标
    private static int getMaxIndex(int[] arr)
    {
        int maxIndex = 0;   //获取到的最大值的角标
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] > arr[maxIndex])
            {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    //获取最小值的下标
    private static int getMinIndex(double[] arr)
    {
        int minIndex = 0;
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] < arr[minIndex])
            {
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * 方法用于自动推测阈值（不保证准确）
     *
     * @param img 经过cutAndMinus处理过的ShortImage对象<br>减小min或增大max会增加预测范围 但会增加性能开支
     * @param min 阈值推测的最低值
     * @param max 阈值推测的最大值
     * @return int 推测出的阈值
     * @throws IOException
     */
    public static int getThreshold(ShortImage img, int min, int max) throws IOException
    {
        double[] ratios = new double[max - min + 3];
        int[] counts = new int[max - min + 3];
        int pre = 1;
        for (int i = min - 2; i <= max; i++)
        {
            short[][] image = binaryProcess(img, i);
            int count = connectedDomainCount(image);
            counts[i - min + 2] = count;
            ratios[i - min + 2] = (double) count / pre;
//            System.out.println(i + "---" + count + "---" + ratios[i - min + 2]);
            pre = count;
        }
        int maxCountIndex = getMaxIndex(counts);
//        System.out.println(maxCountIndex);
        ratios = Arrays.copyOfRange(ratios, maxCountIndex, ratios.length);
        int minIndex = getMinIndex(ratios);
//        System.out.println(minIndex);
        return min + maxCountIndex + minIndex - 1;
    }

    /**
     * @param arrayList
     * @return
     * @author Henry
     */
    public static int Find_Max_In_Array(ArrayList<Integer> arrayList)
    {
        int Max_Integer = -99999;
        for (int i = 0; i < arrayList.size(); i++)
        {
            if (arrayList.get(i) > Max_Integer)
            {
                Max_Integer = arrayList.get(i);
            }
        }
        return Max_Integer;
    }

    /**
     * 合并等价对,变成一个vector<int>,其中数据全部等价
     *
     * @param fpo FirstPassOutput类的对象
     * @return
     * @author Henry
     * @see FirstPassOutput
     */
    public static FirstPassOutput replaceSameLabel(FirstPassOutput fpo)
    {
        int maxLabel = Find_Max_In_Array(fpo.runLabels);
        Vector<Vector> eqTab = new Vector();
        for (int i = 0; i < maxLabel; i++)
        {
            Vector<Boolean> in = new Vector<Boolean>();
            for (int j = 0; j < maxLabel; j++)
            {
                in.add(j, false);
            }
            eqTab.add(i, in);
        }

        for (Iterator it = fpo.equivalences.iterator(); it.hasNext(); )
        {
            Pair pair = (Pair) it.next();
            Integer first = (Integer) pair.getFirst();
            Integer second = (Integer) pair.getSecond();
            eqTab.get(first - 1).set(second - 1, true);
            eqTab.get(second - 1).set(first - 1, true);
        }


        Vector<Integer> labelFlag = new Vector<>();
        for (int i = 0; i < maxLabel; i++)
        {
            labelFlag.add(i, 0);
        }

        Vector<Vector> equaList = new Vector();//储存等价链
        Vector<Integer> tempList = new Vector<>();

        int cnt = 1;
        for (int i = 1; i <= maxLabel; i++)
        {
            if (!labelFlag.get(i - 1).equals(0))
            {
                continue;
            }
            labelFlag.set(i - 1, equaList.size() + 1);//equaList.size()就是现有等价链的条数
            tempList.add(i);
            for (int j = 0; j < tempList.size(); j++)
            {
                for (int k = 0; k != eqTab.get(tempList.get(j) - 1).size(); k++)
                {
                    if (eqTab.get(tempList.get(j) - 1).get(k).equals(true) && labelFlag.get(k).equals(0))
                    {
                        tempList.add(k + 1);
                        labelFlag.set(k, equaList.size() + 1);
                    }
                }
            }

            Vector<Integer> newtemp = new Vector<>();
            for (int m = 0; m < tempList.size(); m++)
            {
                newtemp.add(tempList.get(m));
            }
            equaList.add(newtemp);
            tempList.clear();
        }
        for (int i = 0; i != fpo.runLabels.size(); i++)
        {
            Integer in = labelFlag.get(fpo.runLabels.get(i) - 1);
            fpo.runLabels.set(i, in);
        }
        return fpo;
    }

    public static Output_Data Just_Print_Start_End(ShortImage img, int threshold, FirstPassOutput fpo2) throws IOException
    {
        Output_Data output_data = new Output_Data();
        Vector<Vector> new_v = new Vector<>();
        short[][] image = img.getBand1InShorts();
        int samples = img.getSamples();
        int lines = img.getLines();
        for (int i = 0; i < lines; i++)
        {
            for (int j = 0; j < samples; j++)
            {
                if (image[i][j] < threshold)
                {
                    image[i][j] = 0;
                }
                else
                {
                    image[i][j] = 255;
                }
            }
        }

        Run r = fillRunVectors(image);
        int max_x = Find_Max_In_Array(r.stRun);
        int max_y = Find_Max_In_Array(r.enRun);
        int max_in = Integer.max(max_x, max_y);
        int max_row = Find_Max_In_Array(r.rowRun);
//        System.out.println(max_x+" "+max_y+" "+max_row);

        int[][] save_Data = new int[max_row + 5][max_in + 5];

        for (int i = 0; i < r.rowRun.size(); i++)
        {
            int start = r.stRun.get(i);
            int end = r.enRun.get(i);
            int row = r.rowRun.get(i);
            int f = fpo2.runLabels.get(i);
            for (int j = start; j <= end; j++)
            {
//                System.out.println(row+" "+j);
                save_Data[row][j] = f;
                Pair<Integer, Integer> inin = Pair.create(row, j);
                output_data.data_out_vector.get(f).add(inin);
            }
        }

        System.out.println("stRun:" + r.stRun);
        System.out.println("enRun:" + r.enRun);
        System.out.println("rowRun:" + r.rowRun);
        output_data.save_Data = save_Data;
        return output_data;
    }

    public static double calculateCosSimilarity(ShortImage img1, ShortImage img2) throws IOException
    {
//        System.out.println(img1.getAvg(1));
//        System.out.println(img1.getAvg(2));
//        System.out.println(img1.getAvg(3));
//        System.out.println(img1.getAvg(4));
//        System.out.println(img2.getAvg(1));
//        System.out.println(img2.getAvg(2));
//        System.out.println(img2.getAvg(3));
//        System.out.println(img2.getAvg(4));
        double dotProduct = 0;
        for (int i = 1; i <= 4; i++)
        {
            dotProduct += (img1.getAvg(i) * img2.getAvg(i));
        }
        System.out.println(dotProduct);
        double divisor = Math.sqrt(img1.getAvg(1) * img1.getAvg(1) + img1.getAvg(2) * img1.getAvg(2) + img1.getAvg(3) * img1.getAvg(3) + img1.getAvg(4) * img1.getAvg(4)) * Math.sqrt(img2.getAvg(1)*img2.getAvg(1) + img2.getAvg(2)*img2.getAvg(2) + img2.getAvg(3)*img2.getAvg(3) + img2.getAvg(4)*img2.getAvg(4));
        System.out.println(divisor);
        return dotProduct / divisor;
    }


    public static void main(String[] args) throws IOException
    {
        ShortImage shortImage1 = new ShortImage("C:\\4BandsOut\\4-Bands-_88");
        ShortImage shortImage2 = new ShortImage("C:\\4BandsOut\\4-Bands-_89");
        double similarity = calculateCosSimilarity(shortImage1, shortImage2);
        System.out.println(similarity*100000-99900);
//        short[][] img = shortImage.getBand1InShorts();
//        short[][] img1 = thresholding(shortImage,232);
//        int count1 = connectedDomainCount(img1);
//        System.out.println(count1);
//        short[][] img2 = thresholding(shortImage,235);
//        int count2 = connectedDomainCount(img2);
//        System.out.println(count2);
//        short[][] img3 = thresholding(shortImage,240);
//        int count3 = connectedDomainCount(img3);
//        System.out.println(count3);
//        ShortImage shortImage = new ShortImage("C:\\Users\\Administrator\\Desktop\\countTest\\count");
//        ShortImage shortImage = new ShortImage("C:\\Users\\Administrator\\Desktop\\cut\\TEST-OUT-87");
//        int threshold = getThreshold(shortImage, 220, 250);
//        System.out.println(threshold);
//        ShortImage shortImage = new ShortImage("C:\\Users\\Administrator\\Desktop\\cut\\TEST-OUT-260");
//        for (int i = 5; i < 468; i++)
//        {
//            ShortImage sImg = new ShortImage("C:\\Users\\Administrator\\Desktop\\cut\\TEST-OUT-" + i);
//            try
//            {
//                int threshold = getThreshold(sImg, 0, 300);
//                System.out.print("" + i+"---");
//                System.out.print("" + sImg.getMax());
//                System.out.print(" " + sImg.getMin());
//                System.out.print(" " + sImg.getAvg());
//                System.out.print(" " + sImg.getVariance());
//                System.out.println(" " + threshold);
//            }
//            catch (NoSuchElementException e)
//            {
//                //检测不到浒苔
////                System.out.println();
//            }
//            System.out.println();
    }

}
