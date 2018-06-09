package graphics.ui;

import graphics.utils.UniversalMessage;
import utils.clustering.ClusterInfo;
import utils.clustering.ImageClusterTask;
import utils.imaging.ShortSatImage;
import utils.interfaces.SatImageReader;
import utils.io.ShortImageReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageClusterLauncher extends JFrame {

	private JPanel contentPane;
	private JTextField fileField;
	private JLabel label;
	private JButton pickFileButton;
	private JButton loadButton;
	private JProgressBar progressBar;
	private JTextField iterTimeField;
	private JLabel label_1;
	private JLabel progressLabel;
	private JSlider slider;
	private JLabel sliderLabel;
	private JLabel label_2;

	UniversalMessage<Integer> message = new UniversalMessage<>();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageClusterLauncher frame = new ImageClusterLauncher();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ImageClusterLauncher() {
		initUI();
		initListeners();
	}

	private void initUI() {
		setTitle("\u6D52\u82D4\u805A\u7C7B\u7B97\u6CD5\u6F14\u793A");
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 672, 318);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		fileField = new JTextField();
		fileField.setBounds(45, 44, 487, 24);
		contentPane.add(fileField);
		fileField.setColumns(10);

		label = new JLabel("\u536B\u661F\u56FE\u6587\u4EF6\u8DEF\u5F84");
		label.setFont(new Font("微软雅黑 Light", Font.PLAIN, 16));
		label.setBounds(43, 13, 220, 24);
		contentPane.add(label);

		pickFileButton = new JButton("...");
		pickFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		pickFileButton.setBounds(570, 43, 48, 27);
		contentPane.add(pickFileButton);

		loadButton = new JButton("\u5F00\u59CB\u805A\u7C7B");
		loadButton.setBounds(43, 227, 220, 43);
		contentPane.add(loadButton);

		progressBar = new JProgressBar();
		progressBar.setBounds(45, 182, 573, 24);
		contentPane.add(progressBar);

		iterTimeField = new JTextField("7500");
		iterTimeField.setBounds(120, 112, 86, 24);
		contentPane.add(iterTimeField);
		iterTimeField.setColumns(10);

		label_1 = new JLabel("\u8FED\u4EE3\u6B21\u6570");
		label_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 16));
		label_1.setBounds(45, 112, 75, 21);
		contentPane.add(label_1);

		progressLabel = new JLabel("\u8FDB\u5EA6:");
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setBounds(234, 151, 180, 18);
		contentPane.add(progressLabel);

		slider = new JSlider();
		slider.setBounds(342, 112, 200, 26);
		slider.setMaximum(6);
		slider.setMinimum(2);
		slider.setValue(4);
		contentPane.add(slider);

		sliderLabel = new JLabel("4");
		sliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sliderLabel.setBounds(405, 81, 72, 18);
		contentPane.add(sliderLabel);

		label_2 = new JLabel("\u805A\u7C7B\u6570\u91CF");
		label_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 16));
		label_2.setBounds(259, 113, 72, 18);
		contentPane.add(label_2);
	}

	private void initListeners() {
		pickFileButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser(fileField.getText());
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.showDialog(ImageClusterLauncher.this, "打开卫星图");
			fileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
		});
		slider.addChangeListener(e -> {
			sliderLabel.setText(String.valueOf(slider.getValue()));
		});
		loadButton.addActionListener(e -> {
			File file = new File(fileField.getText());
			int iterCount = Integer.parseInt(iterTimeField.getText());
			progressBar.setMinimum(0);
			progressBar.setMaximum(iterCount);
			if (!file.exists()) {
				JOptionPane.showMessageDialog(ImageClusterLauncher.this,
						"文件不存在", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				new Thread(new LoadImageProcess(
						file.getAbsolutePath(),
						iterCount,
						slider.getValue(),
						message
				)).start();
				new Thread(new MessageHandler(message, iterCount)).start();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(ImageClusterLauncher.this,
						"迭代次数不合法", "错误", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		});
	}

	class MessageHandler implements Runnable{

		private final UniversalMessage<Integer> message;
		private final int iterCount;

		public MessageHandler(UniversalMessage<Integer> message, int iterCount) {
			this.message = message;
			this.iterCount = iterCount;
		}

		@Override
		public void run() {
			try {
				do {
					synchronized (message) {
						message.wait();
					}
					progressBar.setValue(message.getData());
					progressLabel.setText((message.getData()*100 / iterCount) + "%");
				} while (message.getData() < iterCount);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	class LoadImageProcess implements Runnable {

		private final int groupCount;
		private final String filepath;
		private final int iterateTime;
		private UniversalMessage<Integer> message;

		public LoadImageProcess(String filepath, int iterateTime, int groupCount, UniversalMessage<Integer> message) {
			this.filepath = filepath;
			this.iterateTime = iterateTime;
			this.groupCount = groupCount;
			this.message = message;
		}

		@Override
		public void run() {
			try {
				SatImageReader reader = new ShortImageReader(filepath);
				ShortSatImage image = reader.getImage();
				ImageClusterTask task = new ImageClusterTask(image, groupCount, iterateTime, new int[]{3, 3, 4});
				task.setMessage(message);
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
				ImageIO.write(imageout, "jpg", new File("clusterout.jpg"));
				ImageDisplayWindow window = new ImageDisplayWindow(new ImagePanel(imageout));
				window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				window.setVisible(true);
				ImageClusterLauncher.this.setVisible(false);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(ImageClusterLauncher.this,
						"IO错误", "错误", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	static {
		// 尝试将UI风格设置为操作系统默认风格
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
