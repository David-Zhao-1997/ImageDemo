package graphics.ui;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import graphics.utils.AWTEventAdapter;
import utils.io.ZiYuan3Reader;
import utils.imaging.ShortSatImage;

/**
 * 用于显示图像的窗口，自带一个工具框
 * <p>请在创建实例后自行调用{@link #setVisible(boolean)}</p>
 */
public class ImageDisplayWindow extends JFrame {

	private static final int MAX_ZOOM_PERCENT = 150;
	//Components
	private JPanel jPanel;
	private ImagePanel imagePanel;
	public ToolbarWindow toolbarWindow;

	//Members
	private ShortSatImage shortImage;

	//Parameters
	private int bandR, bandG, bandB;
	private int scrollIncrement = 2;
	//	private double scalingIncrement = 0.2;
//	private double maxScale = 3.0;
//	private double minScale = 0.1;
	private int scalePercent = 100;

	@Deprecated
	public ImageDisplayWindow(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
		initUI();
		initListeners();
	}

	//TODO 防止下标越界
	public ImageDisplayWindow(ShortSatImage shortImage, int bandR, int bandG, int bandB) {
		long timer = System.currentTimeMillis();
		this.shortImage = shortImage;
		this.bandR = bandR;
		this.bandG = bandG;
		this.bandB = bandB;
		try {
			this.imagePanel = new ImagePanel(shortImage, bandR, bandG, bandB);
			initUI();
			initListeners();
			timer = System.currentTimeMillis() - timer;
			Logger.getGlobal().log(Level.INFO, "Image window loaded, took " + timer + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ImageDisplayWindow.this, e.getMessage(), e.toString(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 等价于ImageDisplayWindow(image, 1, 1, 1)
	 *
	 * @param image
	 */
	public ImageDisplayWindow(ShortSatImage image) {
		this(image, 1, 1, 1);
	}

	private void initUI() {
		//init frame
		this.setTitle("View image - F1 for help");
		this.jPanel = new JPanel();
		setContentPane(jPanel);
		this.jPanel.setLayout(null);
		this.setBounds(100, 100, 1280, 720);

		//widgets
		toolbarWindow = new ToolbarWindow();
//		toolbarWindow.setVisible(true);

		//show image
		imagePanel.setLocation(0, 0);
		this.add(imagePanel);
	}

	private void initListeners() {

		//Hotkeys
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				System.out.println("Key Event from " + e.getSource().toString());
				if (e.getKeyCode() == KeyEvent.VK_F1) {
					//F1 key
					String message = "Drag to scroll. Alt + mouse wheel to zoom. F2 to open toolkit.";
					JOptionPane.showMessageDialog(ImageDisplayWindow.this, message, "Help", JOptionPane.INFORMATION_MESSAGE);
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ImageDisplayWindow.this.dispose();
				}
				else if (e.getKeyCode() == KeyEvent.VK_F2) {
					toolbarWindow.setVisible(true);
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

		// Mouse Wheel Functions
		imagePanel.addMouseWheelListener(new MouseAdapter() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int pixelsToScroll = e.getUnitsToScroll() * getScrollIncrement();
				if (e.isShiftDown()) {
					//roll horizontally
					imagePanel.setLocation(imagePanel.getX() - pixelsToScroll, imagePanel.getY());
				}
				else if (e.isAltDown()) {
					//Scale
					//get current displaying image size
//					int w, h;
					if (e.getUnitsToScroll() > 0) {
						//Zoom out
						if (scalePercent > 5) {
							scaleImage((int) (scalePercent * 0.8));
						}
					}
					else if (e.getUnitsToScroll() < 0) {
						//Zoom in
						if (scalePercent < MAX_ZOOM_PERCENT) {
							if (scalePercent < 5) {
								scaleImage(scalePercent + 2);
							}
							else {
								scaleImage((int) (scalePercent * 1.2));
							}
						}
					}

				}
				else {
					imagePanel.setLocation(imagePanel.getX(), imagePanel.getY() - pixelsToScroll);
				}
			}
		});
		MouseAdapter imageDragMouseAdapter = new MouseAdapter() {
			boolean flag = false;
			int x;
			int y;

			@Override
			public void mousePressed(MouseEvent e) {
				int button = e.getButton();
				if (button == MouseEvent.BUTTON1) {
					flag = true;
					x = e.getXOnScreen();
					y = e.getYOnScreen();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				flag = false;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (flag) {
					int incrementX = e.getXOnScreen() - x;
					int incrementY = e.getYOnScreen() - y;
					x = e.getXOnScreen();
					y = e.getYOnScreen();
					imagePanel.setLocation(imagePanel.getX() + incrementX, imagePanel.getY() + incrementY);
				}
			}
		};
		MouseAdapter imageClickMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				//Calculates real value of x and y
				double realX = x / ((double) scalePercent / 100);
				double realY = y / ((double) scalePercent / 100);
				System.out.println("get coord:");
				System.out.println("x:" + x);
				System.out.println("y:" + y);
				System.out.println("realx:" + realX);
				System.out.println("realy:" + realY);
				toolbarWindow.showSpecifiedCoordination(realX, realY, imagePanel.getImage());
			}
		};
		imagePanel.addMouseListener(imageDragMouseAdapter);
		imagePanel.addMouseMotionListener(imageDragMouseAdapter);
		imagePanel.addMouseListener(imageClickMouseAdapter);
	}

	/**
	 * <p>自动缩放</p>
	 * <p>调用方法如：</p>
	 * <pre>{@code
	 * scaleImage(scalePercent + 10)}</pre>
	 */
	private void scaleImage(int newPercent) {
		setImageScale(scalePercent, newPercent);
		scalePercent = newPercent;
		toolbarWindow.refreshMessage();
	}

	/**
	 * <b>不要在任何缩放需求时直接调用本方法</b>
	 * <p>{@link #scaleImage(int)}</p>
	 * <p>缩放至指定比例，但不改变scalePercent</p>
	 * <p><b>依赖于scalePercent的其他方法将不会得到通知</b></p>
	 */
	private void setImageScale(int percent, int newPercent) {
		//读取窗口大小
		int windowX = this.getWidth();
		int windowY = this.getHeight();
		//计算窗口中心位置
		int windowCenterX = windowX / 2;
		int windowCenterY = windowY / 2;
		//计算Panel中心位置
		int panelCenterX = windowCenterX - imagePanel.getX();
		int panelCenterY = windowCenterY - imagePanel.getY();
		//用percent计算Panel中心位置对应的实际图像xy
		int imageCenterX = (int) (panelCenterX / (double) (percent * 0.01));
		int imageCenterY = (int) (panelCenterY / (double) (percent * 0.01));
		//计算缩放后Panel的中心xy
		int newPanelCenterX = (int) (imageCenterX * (double) (newPercent * 0.01));
		int newPanelCenterY = (int) (imageCenterY * (double) (newPercent * 0.01));
		//计算缩放后Panel的xy (winCenter - panelCenter)
		int newPanelX = windowCenterX - newPanelCenterX;
		int newPanelY = windowCenterY - newPanelCenterY;
		//缩放
		double scale = newPercent * 0.01;
		imagePanel.scaleImage(
				(int) (imagePanel.getImageWidth() * scale),
				(int) (imagePanel.getImageHeight() * scale)
		);
		//调整位置
		imagePanel.setLocation(newPanelX, newPanelY);
	}

	@Override
	public void dispose() {
		toolbarWindow.dispose();
		super.dispose();
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		toolbarWindow.setVisible(b);
	}

	public int getScrollIncrement() {
		return scrollIncrement;
	}

	public void setScrollIncrement(int scrollIncrement) {
		this.scrollIncrement = scrollIncrement;
	}

	static {
		//尝试将UI风格设置为操作系统默认风格
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private class ToolbarWindow extends JFrame {

		//Components
		private JPanel tool_jPanel;
		private JButton resetButton;
		private JTextArea infoArea;
		private JCheckBox alwaysOnTopCheckBox;

		//Members
		String coordinationInfo;
		StringBuilder rawDataInfo;
		String scaleInfo;

		//Parameters

		public ToolbarWindow() {
			ToolbarWindow.this.initUI();
			ToolbarWindow.this.initListeners();
		}

		private void initUI() {
			//init frame
			this.setTitle("Tools");
			this.setResizable(false);
			tool_jPanel = new JPanel();
			this.setDefaultCloseOperation(HIDE_ON_CLOSE);
			this.setContentPane(tool_jPanel);
			tool_jPanel.setLayout(null);
			ToolbarWindow.this.setBounds(100, 600, 300, 450);
			ToolbarWindow.this.setAlwaysOnTop(true);

			//widget
			this.resetButton = new JButton("Reset");
			resetButton.setBounds(3, 3, 100, 20);
			this.add(resetButton);

			this.alwaysOnTopCheckBox = new JCheckBox("Always on Top");
			alwaysOnTopCheckBox.setBounds(123, 3, 200, 20);
			alwaysOnTopCheckBox.setSelected(true);
			this.add(alwaysOnTopCheckBox);

			this.infoArea = new JTextArea();
			this.infoArea.setEditable(false);
			this.infoArea.setBounds(3, 25, 250, 75);

			coordinationInfo = "X: Y: ";
			rawDataInfo = new StringBuilder();
			refreshMessage();
			this.add(infoArea);

		}

		@Deprecated
		public void setMessage(String message) {
			this.infoArea.setText(message);
		}

		/**
		 * 设置XY和VALUE信息，image可以传入null
		 *
		 * @param x     x coordinate
		 * @param y     y coordinate
		 * @param image for value displaying
		 */
		public void showSpecifiedCoordination(double x, double y, ShortSatImage image) {
			setCoordinationInfo((int) x, (int) y);
			rawDataInfo = new StringBuilder();
			rawDataInfo.append("values: ");
			if (image != null) {
				int x_ = (int) x;
				int y_ = (int) y;
				//从image获取数据
				ArrayList<short[][]> bands = image.getBands();
				rawDataInfo.append("[");
				int bandc = image.getBandCount();
				for (int i = 0; i < bandc; i++) {
					//获取第i波段的x y
					rawDataInfo.append(String.valueOf(bands.get(i)[y_][x_]));
					if (i + 1 < bandc) {
						rawDataInfo.append(", ");
					}
				}
				rawDataInfo.append("]");
			}
			refreshMessage();
		}

		private void refreshMessage() {
			scaleInfo = Integer.toString(ImageDisplayWindow.this.scalePercent) + "%";
			infoArea.setText(
					coordinationInfo + "\n" +
							rawDataInfo + "\n" +
							scaleInfo
			);
		}

		private void setCoordinationInfo(int x, int y) {
//			this.infoArea.setText("X: " + x + "\nY: " + y + "\n");
			coordinationInfo = "X: " + x + "\nY: " + y;
		}

		private void initListeners() {
			resetButton.addActionListener((e) -> {
				ImageDisplayWindow.this.scaleImage(100);
				ImageDisplayWindow.this.imagePanel.setLocation(0, 0);
			});
			alwaysOnTopCheckBox.addActionListener(e -> {
				ToolbarWindow.this.setAlwaysOnTop(alwaysOnTopCheckBox.isSelected());
			});
		}


	}

	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		try {
			String file = "D:\\Documents\\宣墨白\\intell\\111";
//			String file = "C:\\4BandsOut\\4-Bands-_87";
//			ShortSatImage shortImage = new ShortImageReader(file).getImage();
			ShortSatImage shortImage = new ZiYuan3Reader(file).getImage();
			ImageDisplayWindow frame = new ImageDisplayWindow(shortImage, 1, 2, 3);
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
//			for(int i = 0; i < 49; i++) {
//				frame.redrawImagePanel();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.print("main ImageDisplayWindow Process time (ms):");
		System.out.println(end - start);
	}

}
