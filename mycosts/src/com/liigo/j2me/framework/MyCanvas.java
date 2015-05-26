package com.liigo.j2me.framework;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Display;
import com.liigo.util.MyLog;

public class MyCanvas extends Canvas
{
    int backcolor;

    public MyCanvas()
    {
        super();
        init();
    }

    public void paint(Graphics g)
    {
        clear(g);
    }

    public void clear(Graphics g)
    {
        g.setColor(backcolor);
        g.fillRect(0,0,getWidth(),getHeight());
    }

    private void init()
    {
        backcolor = 0x00ffffff;

// sencie MIDP 2.0
//        try
//        {
//            Display display = getApplication().getDisplay();
//            if(display != null)
//                backcolor = display.getColor(Display.C);
//        }
//        catch(Exception e)
//        {
//            MyLog.log(e.toString());
//        }

    }

    public void setBackColor(int color)
    {
        backcolor = color;
        repaint();
    }

    public Application getApplication()
    {
        return Application.getInstance();
    }

    public boolean show()
    {
        return getApplication().show(this);
    }

    public boolean close()
    {
        return getApplication().close();
    }
}