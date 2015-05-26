package com.liigo.j2me.bombs;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import com.liigo.j2me.framework.Application;
import com.liigo.j2me.framework.MyLog;

public class BombsMidlet extends MIDlet
{
    BombsCanvas mainForm = BombsCanvas.getInstance();

    public BombsMidlet()
    {
        Application.getInstance().setMidlet(this);
    }

    void log(String msg)
    {
        MyLog.log(msg);
    }
    public void startApp()
    {
        log("start app by liigo");
        mainForm.newGame();
        mainForm.show();
    }

    public void pauseApp()
    {
        log("pause app");
    }

    public void destroyApp(boolean unconditional)
    {
        log("destroy app");
    }

    public void exit()
    {
        destroyApp(false);
        notifyDestroyed();
    }
}