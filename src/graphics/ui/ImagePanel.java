package graphics.ui;

import utils.ByteImage;
import utils.EnviImage;
import utils.ShortImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 * ImagePanel一般类，可以用ByteImage, ShortImage或者自定义的BufferedImage实例化
 * </p>
 * <p>
 * 施工中
 * </p>
 * <p>
 * 本类未进行测试，若有BUG请当面殴打Inku
 * </p>
 *
 * @author Inku
 */
public class ImagePanel extends JPanel {

	private BufferedImage bufferedImage;
	//使用field存储图像大小是为了尽可能少调用BufferedImage的方法
	private int imageWidth;
	private int imageHeight;
	int bandR, bandB, bandG;

	private EnviImage enviImage;

	/**
	 * <p>
	 * 构造函数需要传入BufferedImage，也可以传入null但是需要使用setBufferedImage()
	 * </p>
	 *
	 * @param bufferedImage
	 */
	public ImagePanel(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		imageHeight = 0;
		imageWidth = 0;
		if (bufferedImage != null) {
			imageHeight = bufferedImage.getHeight();
			imageWidth = bufferedImage.getWidth();
		}
		setBounds(0, 0, imageWidth, imageHeight);
	}

	/**
	 * <p>使用一个EnviImage子类实例初始化ImagePanel</p>
	 *
	 * @param enviImage 一个EnviImage子类的实例
	 * @param bandR     红色通道的波段号
	 * @param bandG     绿色通道的波段号
	 * @param bandB     蓝色通道的波段号
	 */
	public ImagePanel(EnviImage enviImage, int bandR, int bandG, int bandB) throws IOException {
		this.enviImage = enviImage;
		imageWidth = enviImage.getSamples();
		imageHeight = enviImage.getLines();
		this.bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		this.bandR = bandR;
		this.bandG = bandG;
		this.bandB = bandB;

		//generate buffered image content
		if (enviImage instanceof ByteImage) {
			ByteImage image = (ByteImage) enviImage;
			short[][] red = image.getBandInShorts(bandR);
			short[][] green = image.getBandInShorts(bandG);
			short[][] blue = image.getBandInShorts(bandB);
			for (int w = 0; w < imageWidth; w++) {
				for (int h = 0; h < imageHeight; h++) {
					// 0xRRGGBB <- 24bits  R|G|B  24|16|8
					int rgb = ((int) (red[h][w] & 0xff) << 16) | ((int) (green[h][w] & 0xff) << 8) | ((int) (blue[h][w] & 0xff));
					// 0xAARRGGBB  as  0xffRRGGBB
					bufferedImage.setRGB(w, h, rgb | 0xff000000);
				}
			}
		} else if (enviImage instanceof ShortImage) {
			ShortImage image = (ShortImage) enviImage;
			short[][] red = image.getBandInShorts(bandR);
			short[][] green = image.getBandInShorts(bandG);
			short[][] blue = image.getBandInShorts(bandB);
			for (int w = 0; w < imageWidth; w++) {

				for (int h = 0; h < imageHeight; h++) {
					// 0xRRGGBB <- 24bits  R|G|B  24|16|8
					int rgb = ((int) (red[h][w] & 0xff) << 16) | ((int) (green[h][w] & 0xff) << 8) | ((int) (blue[h][w] & 0xff));
					// 0xAARRGGBB  as  0xffRRGGBB
					bufferedImage.setRGB(w, h, rgb | 0xff000000);
				}
			}
		}
		setBounds(0, 0, imageWidth, imageHeight);
	}

	/**
	 * <p>等价于ImagePanel((BufferedImage)null)</p>
	 *
	 * @see ImagePanel#ImagePanel(BufferedImage)
	 */
	public ImagePanel() {
		this((BufferedImage) null);
	}




	/*接口*/

	/**
	 * <p>用于给Swing调用绘制UI</p>
	 *
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bufferedImage, 0, 0, this);
	}


	/*Getters and Setters*/
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}
