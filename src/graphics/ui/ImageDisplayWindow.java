package graphics.ui;

import utils.ShortImage;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class ImageDisplayWindow extends JFrame {

	//Components
	private JPanel jPanel;
	private ImagePanel imagePanel;
	private JButton viewPosButton;

	//Members
	private ShortImage shortImage;

	//Parameters
	private int bandR, bandG, bandB;
	private int scrollIncrement = 2;

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

	/**
	 * 等价于ImageDisplayWindow(image, 1, 1, 1)
	 * @param image
	 */
	public ImageDisplayWindow(ShortImage image){
		this(image, 1, 1, 1);
	}

	private void initUI() {
		//init frame
		this.setTitle("View image - F1 for help");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.jPanel = new JPanel();
		setContentPane(jPanel);
		this.jPanel.setLayout(null);
		this.setBounds(100, 100, imagePanel.getImageWidth(), imagePanel.getImageHeight());

		//widgets
		this.viewPosButton = new JButton("View Pos");
		viewPosButton.setBounds(3, 3, 100, 20);
//		this.add(viewPosButton);

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
				x = e.getXOnScreen();
				y = e.getYOnScreen();
//				x = e.getX() + imagePanel.getX();
//				y = e.getY() + imagePanel.getY();
//				System.out.println("get xy " + x + " " + y);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int incrementX = e.getXOnScreen() - x;
				int incrementY = e.getYOnScreen() - y;
				x = e.getXOnScreen();
				y = e.getYOnScreen();
//				x = e.getX() + imagePanel.getX();
//				y = e.getY() + imagePanel.getY();
				imagePanel.setLocation(imagePanel.getX() + incrementX, imagePanel.getY() + incrementY);
//				System.out.println("Dragged get xy " + x + " " + y);
			}
		};
		imagePanel.addMouseListener(dragAdapter);
		imagePanel.addMouseMotionListener(dragAdapter);
		viewPosButton.addActionListener((e)->{
		});
	}

	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		try {
			String file = "D:\\Documents\\宣墨白\\intell\\out\\out-_6";
			ShortImage shortImage = new ShortImage(file);
			ImageDisplayWindow frame = new ImageDisplayWindow(shortImage, 1, 2, 3);
			frame.setVisible(true);
//			for(int i = 0; i < 49; i++) {
//				frame.redrawImagePanel();
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

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
