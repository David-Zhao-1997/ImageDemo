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
	//	private double scalingIncrement = 0.2;
//	private double maxScale = 3.0;
//	private double minScale = 0.1;
	private int scalePercent = 100;

	public ImageDisplayWindow(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
		initUI();
		initListeners();
	}

	public ImageDisplayWindow(ShortImage shortImage, int bandR, int bandG, int bandB) {
		this.shortImage = shortImage;
		this.bandR = bandR;
		this.bandG = bandG;
		this.bandB = bandB;
		try {
			this.imagePanel = new ImagePanel(shortImage, bandR, bandG, bandB);
			initUI();
			initListeners();

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ImageDisplayWindow.this, e.getMessage(), e.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 等价于ImageDisplayWindow(image, 1, 1, 1)
	 *
	 * @param image
	 */
	public ImageDisplayWindow(ShortImage image) {
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

	private void initListeners() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 112) {
					//F1 key
					String message = "Drag to scroll.";
					JOptionPane.showMessageDialog(ImageDisplayWindow.this, message, "Help", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		imagePanel.addMouseWheelListener(new MouseAdapter() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int pixelsToScroll = e.getUnitsToScroll() * getScrollIncrement();
				if (e.isShiftDown()) {
					//roll horizontally
					imagePanel.setLocation(imagePanel.getX() - pixelsToScroll, imagePanel.getY());
				} else if (e.isAltDown()) {
					//Scale
					//get current displaying image size
					int w, h;
					if (e.getUnitsToScroll() > 0) {
						//Zoom out
						if (scalePercent > 5) {
							scalePercent *= 0.8;
							double tScale = scalePercent * 0.01;
							w = (int) (imagePanel.getWidth() * tScale);
							h = (int) (imagePanel.getImageHeight() * tScale);
							imagePanel.scaleImage(w, h);
//							imagePanel.setSize(w, h);
							//setsize会导致问题
						}
					} else if (e.getUnitsToScroll() < 0) {
						//Zoom in
						if (scalePercent < 300) {
							if (scalePercent < 5) {
								scalePercent += 2;
							} else {
								scalePercent *= 1.2;
							}
							double tScale = scalePercent * 0.01;
							w = (int) (imagePanel.getWidth() * tScale);
							h = (int) (imagePanel.getImageHeight() * tScale);
							imagePanel.scaleImage(w, h);
//							System.out.println("scale in: " + scalePercent + "%, w-h:" + w + " " + h);
//							imagePanel.setSize(w, h);
							//setsize会导致问题
						}
					}

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
				imagePanel.setLocation(imagePanel.getX() + incrementX, imagePanel.getY() + incrementY);
			}
		};
		imagePanel.addMouseListener(dragAdapter);
		imagePanel.addMouseMotionListener(dragAdapter);
		viewPosButton.addActionListener((e) -> {
		});
	}


	public int getScrollIncrement() {
		return scrollIncrement;
	}

	public void setScrollIncrement(int scrollIncrement) {
		this.scrollIncrement = scrollIncrement;
	}

	static {
		//将UI风格设置为Windows默认风格
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
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
//				frame.redrawImagePanel();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.print("main ImageDisplayWindow Process time:");
		System.out.println(end - start);
	}
}
