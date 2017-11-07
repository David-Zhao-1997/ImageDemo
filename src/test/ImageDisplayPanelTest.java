package test;

import java.io.IOException;

import graphics.ui.ImageDisplayWindow;
import utils.ShortImageFile;

public class ImageDisplayPanelTest
{
    public static void main(String[] args) throws IOException
    {
        ShortImageFile shortImage = new ShortImageFile("C:\\Users\\Administrator\\Desktop\\cut\\TEST-OUT-87");
        new ImageDisplayWindow(shortImage).setVisible(true);
    }
}
