package test;

import java.io.IOException;

import utils.ImageCutter;

public class T0512 {
	public static void main(String[] args) {
		try {
			ImageCutter.cut(300, 300, "H:\\HJ\\new\\20160714-ROI", "H:\\HJ\\new\\20160714-test\\");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
