package graphics.utils;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

public class AWTEventAdapter implements AWTEventListener
{
    @Override
    public void eventDispatched(AWTEvent event)
    {
        if (event.getClass() == KeyEvent.class)
        {
            KeyEvent e = (KeyEvent) event;
            if (e.getID() == KeyEvent.KEY_PRESSED)
            {
                keyPressed(e);
            }
        }
    }

    public void keyPressed(KeyEvent e)
    {

    }
}
