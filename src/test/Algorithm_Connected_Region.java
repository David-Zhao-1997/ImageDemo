package test;

import utils.ShortImageFile;

import static test.Algorithms.Find_Max_In_Array;
import static test.Algorithms.FirstPassOutput;
import static test.Algorithms.Just_Print_Start_End;
import static test.Algorithms.Output_Data;
import static test.Algorithms.binaryProcess;
import static test.Algorithms.fillRunVectors;
import static test.Algorithms.firstPass;
import static test.Algorithms.getThreshold;
import static test.Algorithms.replaceSameLabel;

/**
 * Created by henry_fordham on 2017/10/27.
 */
public class Algorithm_Connected_Region
{
    public static void main(String[] args) throws Exception
    {
        ShortImageFile img = new ShortImageFile("C:\\Users\\Administrator\\Desktop\\cut\\TEST-OUT-87");
        short[][] arr = img.getBand1InShorts();

        int threshold = getThreshold(img, 220, 250);

//        System.out.println(threshold);
        short[][] img_short = binaryProcess(img, threshold);


//        System.out.println(img_short[1][1]);
//        connectedDomainCount(img_short);


        FirstPassOutput fpo = firstPass(fillRunVectors(img_short), 1);
        System.out.println(fpo.runLabels);
        System.out.println(fpo.equivalences);
        System.out.println("fpo1:" + Find_Max_In_Array(fpo.runLabels) + " size:" + fpo.runLabels.size());

        FirstPassOutput fpo2 = replaceSameLabel(fpo);

        System.out.println(fpo2.runLabels);//对应的等价序列的等价块序号
        System.out.println(fpo2.equivalences);
        System.out.println("fpo2:" + Find_Max_In_Array(fpo2.runLabels) + " size:" + fpo2.runLabels.size());

        Output_Data out = Just_Print_Start_End(img, threshold, fpo2);
        for (int i = 0; i < out.save_Data.length; i++)
        {
            for (int j = 0; j < out.save_Data[i].length; j++)
            {
                System.out.print(out.save_Data[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println(out.data_out_vector.get(1));
    }
}
