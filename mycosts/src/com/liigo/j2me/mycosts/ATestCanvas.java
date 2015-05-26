package com.liigo.j2me.mycosts;

//import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import com.liigo.j2me.framework.MyCanvas;

public class ATestCanvas extends MyCanvas
{
    private static ATestCanvas canvas = new ATestCanvas();
    public static ATestCanvas getInstance()
    {
        return canvas;
    }

    private ATestCanvas()
    {

    }
    public void paint(Graphics g)
    {
        clear(g);

        g.setColor(255, 0, 0);
        g.drawLine(10,10, 80, 80);
    }
}