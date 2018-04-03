package graphics.ui;

import utils.imaging.SatImageFileHdr;
import utils.imaging.ShortSatImage;
import utils.interfaces.SatImageReader;
import utils.io.ShortImageReader;
import utils.io.ZiYuan3Reader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSelectorWindow extends JFrame {

	//Components
	private JPanel contentPane;
	private JTextField satFileField;
	private JTextField hdrFileField;
	private JLabel hdrLabel;
	private JButton selectSatFileButton;
	private JButton selectHdrFileButton;
	private JButton loadHdrButton;
	private JButton loadImageButton;
	private JComboBox rCombo;
	private JComboBox gCombo;
	private JComboBox bCombo;
	private ButtonGroup fileTypeGroup;
	private JRadioButton ziYuan3TypeRadio;
	private JRadioButton shortImageRadio;

	//Parameters
	private boolean isHdrLoaded;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FileSelectorWindow frame = new FileSelectorWindow();
			frame.setTitle("Select Satellite File");
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FileSelectorWindow() {
		initUI();
		initListeners();
	}


	private void initUI() {
		setResizable(false);
		setBounds(100, 100, 559, 431);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		satFileField = new JTextField();
		satFileField.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		satFileField.setBounds(27, 54, 360, 34);
		contentPane.add(satFileField);
		satFileField.setColumns(10);

		hdrFileField = new JTextField();
		hdrFileField.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		hdrFileField.setBounds(27, 130, 360, 34);
		contentPane.add(hdrFileField);
		hdrFileField.setColumns(10);

		JLabel fileLabel = new JLabel("Satellite File");
		fileLabel.setBounds(27, 29, 132, 18);
		contentPane.add(fileLabel);

		hdrLabel = new JLabel("HDR File");
		hdrLabel.setBounds(27, 99, 72, 18);
		contentPane.add(hdrLabel);

		selectSatFileButton = new JButton("...");
		selectSatFileButton.setBounds(401, 54, 80, 34);
		contentPane.add(selectSatFileButton);

		selectHdrFileButton = new JButton("...");
		selectHdrFileButton.setBounds(401, 130, 80, 34);
		contentPane.add(selectHdrFileButton);

		fileTypeGroup = new ButtonGroup();

		ziYuan3TypeRadio = new JRadioButton("ZiYuan3");
		fileTypeGroup.add(ziYuan3TypeRadio);
		ziYuan3TypeRadio.setBounds(27, 190, 157, 27);
		contentPane.add(ziYuan3TypeRadio);

		shortImageRadio = new JRadioButton("ShortImage");
		fileTypeGroup.add(shortImageRadio);
		shortImageRadio.setBounds(187, 190, 157, 27);
		shortImageRadio.setSelected(true);
		contentPane.add(shortImageRadio);

		loadHdrButton = new JButton("Load HDR");
		loadHdrButton.setBounds(46, 344, 113, 27);
		contentPane.add(loadHdrButton);

		loadImageButton = new JButton("Load Image");
		loadImageButton.setBounds(259, 344, 113, 27);
		contentPane.add(loadImageButton);

		rCombo = new JComboBox();
		rCombo.setBounds(27, 267, 72, 24);
		contentPane.add(rCombo);

		gCombo = new JComboBox();
		gCombo.setBounds(122, 267, 72, 24);
		contentPane.add(gCombo);

		bCombo = new JComboBox();
		bCombo.setBounds(218, 267, 72, 24);
		contentPane.add(bCombo);

		JLabel lblRBand = new JLabel("R Band");
		lblRBand.setBounds(27, 245, 72, 18);
		contentPane.add(lblRBand);

		JLabel lblGBand = new JLabel("G Band");
		lblGBand.setBounds(122, 245, 72, 18);
		contentPane.add(lblGBand);

		JLabel lblBBand = new JLabel("B Band");
		lblBBand.setBounds(218, 245, 72, 18);
		contentPane.add(lblBBand);
	}

	private void initListeners() {
		selectSatFileButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser(satFileField.getText());
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.showDialog(FileSelectorWindow.this, "Open");
			satFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			Logger.getGlobal().log(Level.INFO, "Satellite file path set by user: " + satFileField.getText());
			if (hdrFileField.getText().trim().equals("")) {
				//if the other field is empty, try auto fill
				File assumeHdrFile = new File(satFileField.getText() + ".HDR");
				if(assumeHdrFile.exists()) {
					Logger.getGlobal().log(Level.INFO, "Hdr file exists, auto-filled Hdr path");
					clearBandCount();
					hdrFileField.setText(assumeHdrFile.getAbsolutePath());
				}
			}
			tryAutoLoadHdr();
		});
		selectHdrFileButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser(hdrFileField.getText());
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.showDialog(FileSelectorWindow.this, "Open");
			hdrFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			Logger.getGlobal().log(Level.INFO, "Hdr file path set by user: " + hdrFileField.getText());
			clearBandCount();
			if (satFileField.getText().trim().equals("")) {
				//if the other field is empty, try auto fill
				File assumeSatFile = new File(hdrFileField.getText().replace(".HDR",""));
				if(assumeSatFile.exists()) {
					Logger.getGlobal().log(Level.INFO, "Satellite image file exists, auto-filled image path");
					satFileField.setText(assumeSatFile.getAbsolutePath());
				}
			}
			tryAutoLoadHdr();
		});
		loadHdrButton.addActionListener(e -> {
			loadHdrFromField();
		});
		loadImageButton.addActionListener(e -> {
			loadImageFromField();
		});
	}

	private void tryAutoLoadHdr() {
		Logger.getGlobal().log(Level.INFO, "Auto-loading Hdr file");
		if(new File(hdrFileField.getText()).exists())
			//Auto load hdr if file exists
			loadHdrFromField();
		else
			Logger.getGlobal().log(Level.INFO, "Hdr file not exist");
	}

	private void loadImageFromField(){
		loadImageButton.setEnabled(false);
		new ImageLoadingThread().start();
	}

	private void loadHdrFromField(){
		loadHdrButton.setEnabled(false);
		try {
			SatImageFileHdr imageFileHdr = new SatImageFileHdr(hdrFileField.getText());
			int bandCount = imageFileHdr.readHdr().getBandCount();
			setBandCount(bandCount);
			isHdrLoaded = true;
			Logger.getGlobal().log(Level.INFO, "Hdr loaded successfully");
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(FileSelectorWindow.this,
					"I/O Error loading HDR file, please check the hdr path",
					"Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} finally {
			loadHdrButton.setEnabled(true);
		}
	}

	private void setBandCount(int bandCount){
		Logger.getGlobal().log(Level.INFO, "Setting band-count as " + bandCount);
		clearBandCount();
		for (int i = 0; i < bandCount; i++) {
			rCombo.addItem(i + 1);
			gCombo.addItem(i + 1);
			bCombo.addItem(i + 1);
		}
	}

	private void clearBandCount(){
		rCombo.removeAllItems();
		gCombo.removeAllItems();
		bCombo.removeAllItems();
		isHdrLoaded = false;
	}

	private class ImageLoadingThread extends Thread{
		@Override
		public void run() {
			Logger.getGlobal().log(Level.INFO, "ImageLoadingThread has started");
			if(!isHdrLoaded){
				JOptionPane.showMessageDialog(FileSelectorWindow.this,
						"HDR file not loaded. Please load hdr file first!",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			SatImageReader reader = null;
			if(ziYuan3TypeRadio.isSelected()){
				reader = new ZiYuan3Reader(satFileField.getText());
				Logger.getGlobal().log(Level.INFO, "Initializing ZiYuan3Reader");
			} else if(shortImageRadio.isSelected()){
				reader = new ShortImageReader(satFileField.getText());
				Logger.getGlobal().log(Level.INFO, "Initializing ShortImageReader");
			} else {
				JOptionPane.showMessageDialog(FileSelectorWindow.this,
						"Please specify the image type!",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(reader!=null){
				loadImageButton.setEnabled(false);
				Logger.getGlobal().log(Level.INFO, "Started image loading");
				long timer = System.currentTimeMillis();
				try {
					ShortSatImage image = reader.getImage();
					int r = (int) rCombo.getSelectedItem();
					int g = (int) gCombo.getSelectedItem();
					int b = (int) bCombo.getSelectedItem();
					Logger.getGlobal().log(Level.INFO, "Image loading completed, Initializing image window");
					timer = System.currentTimeMillis() - timer;
					Logger.getGlobal().log(Level.INFO, "Image loading took " + timer + "ms");
					ImageDisplayWindow window = new ImageDisplayWindow(image,r,g,b);
					window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					window.setVisible(true);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(FileSelectorWindow.this,
							"I/O Error reading image. Please check file path.",
							"Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} finally {
					loadImageButton.setEnabled(true);
				}
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
