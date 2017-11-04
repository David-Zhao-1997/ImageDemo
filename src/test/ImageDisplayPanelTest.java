package test;

import java.io.IOException;

import graphics.ui.ImageDisplayWindow;
import utils.ShortImage;

public class ImageDisplayPanelTest
{
    public static void main(String[] args) throws IOException
    {
        ShortImage shortImage = new ShortImage("C:\\Users\\Administrator\\Desktop\\cut\\TEST-OUT-87");
        new ImageDisplayWindow(shortImage).setVisible(true);
    }
}
