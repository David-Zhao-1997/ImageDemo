package graphics.ui;

import graphics.utils.AWTEventAdapter;
import utils.functional.ShortImageReader;
import utils.imaging.ShortSatImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

public class ImageDisplayWindow extends JFrame {

	private class ToolbarWindow extends JFrame{

		//Components
		private JPanel tool_jPanel;
		private JButton resetButton;

		//Members

		//Parameters

		public ToolbarWindow(){
			ToolbarWindow.this.initUI();
			ToolbarWindow.this.initListeners();
		}

		private void initUI(){
			//init frame
			this.setTitle("Tools");
			tool_jPanel = new JPanel();
			this.setDefaultCloseOperation(HIDE_ON_CLOSE);
			this.setContentPane(tool_jPanel);
			tool_jPanel.setLayout(null);
			ToolbarWindow.this.setBounds(100,600,300,450);
			ToolbarWindow.this.setAlwaysOnTop(true);

			//widget
			this.resetButton = new JButton("Reset");
			resetButton.setBounds(3, 3, 100, 20);
			this.add(resetButton);
		}

		private void initListeners(){
			resetButton.addActionListener((e) -> {
				ImageDisplayWindow.this.scalePercent = 100;
				ImageDisplayWindow.this.scaleImage();
				ImageDisplayWindow.this.imagePanel.setLocation(0, 0);
			});
		}

	}

	//Components
	private JPanel jPanel;
	private ImagePanel imagePanel;
	private ToolbarWindow toolbarFrame;

	//Members
	private ShortSatImage shortImage;

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

	//TODO 防止下标越界
	public ImageDisplayWindow(ShortSatImage shortImage, int bandR, int bandG, int bandB) {
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
	public ImageDisplayWindow(ShortSatImage image) {
		this(image, 1, 1, 1);
	}

	private void initUI() {
		//init frame
		this.setTitle("View image - F1 for help");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.jPanel = new JPanel();
		setContentPane(jPanel);
		this.jPanel.setLayout(null);
		this.setBounds(100, 100, imagePanel.getImageWidth(), imagePanel.getImageHeight());

		//widgets
		toolbarFrame = new ToolbarWindow();
		toolbarFrame.setVisible(true);

		//show image
		imagePanel.setLocation(0, 0);
		this.add(imagePanel);
	}

	private void initListeners() {

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
//				System.out.println("Key Event from " + e.getSource().toString());
				if (e.getKeyCode() == KeyEvent.VK_F1) {
					//F1 key
					String message = "Drag to scroll. Alt + mouse wheel to zoom. F2 to open toolkit.";
					JOptionPane.showMessageDialog(ImageDisplayWindow.this, message, "Help", JOptionPane.INFORMATION_MESSAGE);
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ImageDisplayWindow.this.dispose();
				} else if (e.getKeyCode() == KeyEvent.VK_F2){
					toolbarFrame.setVisible(true);
				}
			}
		},AWTEvent.KEY_EVENT_MASK);

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
//					int w, h;
					if (e.getUnitsToScroll() > 0) {
						//Zoom out
						if (scalePercent > 5) {
							scalePercent *= 0.8;
							scaleImage();
						}
					} else if (e.getUnitsToScroll() < 0) {
						//Zoom in
						if (scalePercent < 300) {
							if (scalePercent < 5) {
								scalePercent += 2;
							} else {
								scalePercent *= 1.2;
							}
							scaleImage();
						}
					}

				} else {
					imagePanel.setLocation(imagePanel.getX(), imagePanel.getY() - pixelsToScroll);
				}
			}
		});
		MouseAdapter imageMouseAdapter = new MouseAdapter() {
			int x;
			int y;

			@Override
			public void mousePressed(MouseEvent e) {
				int button = e.getButton();
				if(button == 1) {
					x = e.getXOnScreen();
					y = e.getYOnScreen();
				} else if(button == 3){
					int x = e.getX();
					int y = e.getY();
				}
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
		imagePanel.addMouseListener(imageMouseAdapter);
		imagePanel.addMouseMotionListener(imageMouseAdapter);
	}

	/**
	 * <p>读取scalePercent并自动缩放</p>
	 * <p>调用方法如：</p>
	 * <pre>{@code
	 * scalePercent *= 1.2;
	 * scaleImage();
	 * }</pre>
	 */
	private void scaleImage() {
		double rate = scalePercent * 0.01;
		setImageScale(rate);
	}

	/**
	 * <p>缩放至指定比例，但不改变scalePercent</p>
	 * <p><b>依赖于scalePercent的其他方法将不会得到通知</b></p>
	 * @param scale
	 */
	private void setImageScale(double scale) {
		imagePanel.scaleImage(
				(int) (imagePanel.getImageWidth() * scale),
				(int) (imagePanel.getImageHeight() * scale)
		);
	}

	@Override
	public void dispose() {
		toolbarFrame.dispose();
		super.dispose();
	}

	public int getScrollIncrement() {
		return scrollIncrement;
	}

	public void setScrollIncrement(int scrollIncrement) {
		this.scrollIncrement = scrollIncrement;
	}

	static {
		//将UI风格设置为操作系统默认风格
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		try {
			String file = "D:\\Documents\\宣墨白\\intell\\out\\out_diff_87";
			ShortSatImage shortImage = new ShortImageReader(file).getImage();
			ImageDisplayWindow frame = new ImageDisplayWindow(shortImage, 1,1,1);
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
