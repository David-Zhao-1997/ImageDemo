package graphics.ui;

import utils.ByteImage;
import utils.ShortImage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ImageDisplayWindow extends JFrame {

	private JPanel jPanel;
//	private JButton exitButton;
	private ImagePanel imagePanel;
	ShortImage shortImage;
	int bandR, bandG, bandB;

	public ImageDisplayWindow(ImagePanel imagePanel){
		try {
			if(imagePanel.getImageWidth()>=1850||imagePanel.getImageHeight()>1065){
				throw new ImageTooLargeException();
			}
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			setBounds(100, 100, 500, 600);
			this.jPanel = new JPanel();
			setContentPane(jPanel);
			this.jPanel.setLayout(null);

			//show image
			this.imagePanel = imagePanel;
			imagePanel.setLocation(0, 0);
			this.add(imagePanel);
		} catch (ImageTooLargeException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ImageDisplayWindow.this, e.getMessage(), e.toString(), JOptionPane.ERROR_MESSAGE);
		}

	}

	public ImageDisplayWindow(ShortImage shortImage, int bandR, int bandG, int bandB){
		this.shortImage = shortImage;
		this.bandR = bandR;
		this.bandG = bandG;
		this.bandB = bandB;
		try {
			ImagePanel imagePanel = new ImagePanel(shortImage, bandR, bandG, bandB);
			if(imagePanel.getImageWidth()>=1850||imagePanel.getImageHeight()>1065){
				throw new ImageTooLargeException();
			}

			//init frame
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			setBounds(100, 100, 500, 600);
			this.jPanel = new JPanel();
			setContentPane(jPanel);
			this.jPanel.setLayout(null);

			//show image
			this.imagePanel = imagePanel;
			imagePanel.setLocation(0, 0);
			this.add(imagePanel);
		} catch (IOException | ImageTooLargeException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ImageDisplayWindow.this, e.getMessage(), e.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 *
	 */
	public void fuck(){
		imagePanel = null;
		try {
			ImagePanel imagePanel = new ImagePanel(shortImage, bandR, bandG, bandB);
			if(imagePanel.getImageWidth()>=1850||imagePanel.getImageHeight()>1065){
				throw new ImageTooLargeException();
			}

			//init frame
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			setBounds(100, 100, 500, 600);
			this.jPanel = new JPanel();
			setContentPane(jPanel);
			this.jPanel.setLayout(null);

			//show image
			this.imagePanel = imagePanel;
			imagePanel.setLocation(0, 0);
			this.add(imagePanel);
		} catch (IOException | ImageTooLargeException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ImageDisplayWindow.this, e.getMessage(), e.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}


	public static void main(String args[]){
		long start = System.currentTimeMillis();
		try {
			String file = "C:\\Users\\Inku\\Desktop\\out\\out-_13";
			ShortImage shortImage = new ShortImage(file);
			ImageDisplayWindow frame = new ImageDisplayWindow(shortImage, 1, 2, 3);
			frame.setVisible(true);
//			for(int i = 0; i < 49; i++) {
//				frame.fuck();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}

	private class ImageTooLargeException extends Exception{

		public ImageTooLargeException(){
			super("The image passed to the UI handler is to large for displaying.");
		}

	}

}
