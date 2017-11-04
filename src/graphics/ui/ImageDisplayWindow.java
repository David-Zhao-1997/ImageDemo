package graphics.ui;

import utils.ByteImage;
import utils.ShortImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ImageDisplayWindow extends JFrame {

	private JPanel jPanel;
	//	private JButton exitButton;
	private ImagePanel imagePanel;
	ShortImage shortImage;
	int bandR, bandG, bandB;

	int scrollIncrement = 2;

	public ImageDisplayWindow(ImagePanel imagePanel) {
		try {
			if (imagePanel.getImageWidth() >= 1850 || imagePanel.getImageHeight() > 1065) {
				throw new ImageTooLargeException();
			}
			this.imagePanel = imagePanel;
			initUI();
			initActionListeners();
		} catch (ImageTooLargeException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ImageDisplayWindow.this, e.getMessage(), e.toString(), JOptionPane.ERROR_MESSAGE);
		}

	}


	public ImageDisplayWindow(ShortImage shortImage, int bandR, int bandG, int bandB) {
		this.shortImage = shortImage;
		this.bandR = bandR;
		this.bandG = bandG;
		this.bandB = bandB;
		try {
			this.imagePanel = new ImagePanel(shortImage, bandR, bandG, bandB);
			if (imagePanel.getImageWidth() >= 1850 || imagePanel.getImageHeight() > 1065) {
				throw new ImageTooLargeException();
			}
			initUI();
			initActionListeners();

		} catch (IOException | ImageTooLargeException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ImageDisplayWindow.this, e.getMessage(), e.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initUI() {
		//init frame
		this.setTitle("View image - F1 for help");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.jPanel = new JPanel();
		setContentPane(jPanel);
		this.jPanel.setLayout(null);
		this.setBounds(100, 100, imagePanel.getImageWidth(), imagePanel.getImageHeight());

		//show image
		this.imagePanel = imagePanel;
		imagePanel.setLocation(0, 0);
		this.add(imagePanel);
	}

	private void initActionListeners() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 112){
					//F1 key
					String message = "Drag to scroll.";
					JOptionPane.showMessageDialog(ImageDisplayWindow.this, message, "Help", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		imagePanel.addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
//				System.out.println(e.paramString());
				int pixelsToScroll = e.getUnitsToScroll() * getScrollIncrement();
				if (e.isShiftDown()) {
					//roll horizontally
					imagePanel.setLocation(imagePanel.getX() - pixelsToScroll, imagePanel.getY());
				} else {
					imagePanel.setLocation(imagePanel.getX(), imagePanel.getY() - pixelsToScroll);
				}
			}
		});
		MouseAdapter dragAdapter = new MouseAdapter() {
			int x;
			int y;
			@Override
			public void mousePressed(MouseEvent e) {
				//获取imagePanel的绝对坐标，加上鼠标的相对坐标
				x = e.getX() + imagePanel.getX();
				y = e.getY() + imagePanel.getY();
				System.out.println("get xy " + x + " " + y);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int incrementX = e.getX() + imagePanel.getX() - x;
				int incrementY = e.getY() + imagePanel.getY() - y;
				x = e.getX() + imagePanel.getX();
				y = e.getY() + imagePanel.getY();
				imagePanel.setLocation(imagePanel.getX() + incrementX, imagePanel.getY() + incrementY);
				System.out.println("Dragged get xy " + x + " " + y);
			}
		};
		imagePanel.addMouseListener(dragAdapter);
		imagePanel.addMouseMotionListener(dragAdapter);
	}

	/**
	 *
	 */
	public void fuck() {
		imagePanel = null;
		try {
			ImagePanel imagePanel = new ImagePanel(shortImage, bandR, bandG, bandB);
			if (imagePanel.getImageWidth() >= 1850 || imagePanel.getImageHeight() > 1065) {
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


	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		try {
			String file = "D:\\Documents\\宣墨白\\intell\\out\\out-_6";
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
		System.out.print("main ImageDisplayWindow Process time:");
		System.out.println(end - start);
	}

	public int getScrollIncrement() {
		return scrollIncrement;
	}

	public void setScrollIncrement(int scrollIncrement) {
		this.scrollIncrement = scrollIncrement;
	}

	private class ImageTooLargeException extends Exception {

		public ImageTooLargeException() {
			super("The image passed to the UI handler is to large for displaying.");
		}

	}

}
