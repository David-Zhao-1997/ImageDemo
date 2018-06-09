package test;

import graphics.ui.ImageDisplayWindow;
import graphics.ui.ImagePanel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.clustering.ClusterInfo;
import utils.clustering.ImageClusterTask;
import utils.imaging.ShortSatImage;
import utils.interfaces.SatImageReader;
import utils.io.ShortImageReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageClusterTest {

	public static void main(String[] args) throws IOException {
		SatImageReader reader = new ShortImageReader("4-Bands-_87");
		ShortSatImage image = reader.getImage();
		ImageClusterTask task = new ImageClusterTask(image, 4, 7500, new int[]{3, 3, 4});
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<ClusterInfo> future = service.submit(task);
		ClusterInfo clusterInfo = null;
		try {
			clusterInfo = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			service.shutdown();
		}
		BufferedImage imageout = clusterInfo.convertIntoBufferedImage(ClusterInfo.DEFAULT_COLORS);
		ImageIO.write(imageout, "jpg", new File("clusterout"));
		ImageDisplayWindow window = new ImageDisplayWindow(new ImagePanel(imageout));
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
