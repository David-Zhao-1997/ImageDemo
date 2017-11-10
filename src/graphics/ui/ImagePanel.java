package graphics.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import utils.imaging.ShortSatImage;

/**
 * <p>
 * ImagePanel一般类，可以用ByteImage, ShortImage或者自定义的BufferedImage实例化
 * </p>
 *
 * @author Inku
 */
public class ImagePanel extends JPanel
{

    private BufferedImage bufferedImage;
    private Image scaledImage;
    //使用field存储图像大小是为了尽可能少调用BufferedImage的方法
    private int imageWidth;
    private int imageHeight;
    int bandR, bandB, bandG;

    private ShortSatImage image;

    /**
     * <p>
     * 构造函数需要传入BufferedImage，也可以传入null但是需要使用setBufferedImage()
     * </p>
     *
     * @param bufferedImage
     */
    public ImagePanel(BufferedImage bufferedImage)
    {
        this.bufferedImage = bufferedImage;
        imageHeight = 0;
        imageWidth = 0;
        if (bufferedImage != null)
        {
            imageHeight = bufferedImage.getHeight();
            imageWidth = bufferedImage.getWidth();
            scaledImage = bufferedImage.getScaledInstance(-1, -1, Image.SCALE_FAST);
        }
        setBounds(0, 0, imageWidth, imageHeight);
    }

    /**
     * <p>使用一个EnviImage子类实例初始化ImagePanel</p>
     *
     * @param image 一个EnviImage子类的实例
     * @param bandR 红色通道的波段号
     * @param bandG 绿色通道的波段号
     * @param bandB 蓝色通道的波段号
     */
    public ImagePanel(ShortSatImage image, int bandR, int bandG, int bandB) throws IOException
    {
        this.image = image;
        imageWidth = image.getSamples();
        imageHeight = image.getLines();
        this.bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        this.bandR = bandR;
        this.bandG = bandG;
        this.bandB = bandB;

        //generate buffered image content
        short[][] red = image.getBand(bandR);
        short[][] green = image.getBand(bandG);
        short[][] blue = image.getBand(bandB);
        for (int w = 0; w < imageWidth; w++)
        {
            for (int h = 0; h < imageHeight; h++)
            {
                // 0xRRGGBB <- 24bits  R|G|B  24|16|8
                int rgb = ((int) (red[h][w] & 0xff) << 16) | ((int) (green[h][w] & 0xff) << 8) | ((int) (blue[h][w] & 0xff));
                // 0xAARRGGBB  as  0xffRRGGBB
                bufferedImage.setRGB(w, h, rgb | 0xff000000);
            }
        }
        setBounds(0, 0, imageWidth, imageHeight);
        scaledImage = bufferedImage.getScaledInstance(-1, -1, Image.SCALE_FAST);
    }

    /**
     * <p>等价于ImagePanel((BufferedImage)null)</p>
     *
     * @see ImagePanel#ImagePanel(BufferedImage)
     */
    public ImagePanel()
    {
        this((BufferedImage) null);
    }

    /**
     * <p>传入长宽将内置对象缩放后保存至scaledImage</p>
     * <p>需要按比例缩放时可以传入一个负值</p>
     *
     * @param width  缩放后的宽度 负值为自动
     * @param height 缩放后的高度 负值为自动
     */
    public void scaleImage(int width, int height)
    {
        scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_FAST);
        this.setSize(width, height);
        this.repaint();
    }

	/*接口*/

    /**
     * <p>用于给Swing调用绘制UI</p>
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(scaledImage, 0, 0, this);
    }


    /*Getters and Setters*/
    public BufferedImage getBufferedImage()
    {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage)
    {
        this.bufferedImage = bufferedImage;
    }

    public int getImageWidth()
    {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight()
    {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public Image getScaledImage()
    {
        return scaledImage;
    }

    public void setScaledImage(Image scaledImage)
    {
        this.scaledImage = scaledImage;
    }
}
