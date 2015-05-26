package com.liigo.j2me.mycosts;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import com.liigo.j2me.framework.Application;
import com.liigo.util.MyLog;

public class MyCostsMidlet extends MIDlet
{
    MainForm mainForm = MainForm.getInstance();

    public MyCostsMidlet()
    {
        MyLog.log("MyCostsMidlet" + "OK");
        Application.getInstance().setMidlet(this);
    }

    void log(String msg)
    {
        MyLog.log(msg);
    }
    public void startApp()
    {
        log("start app by liigo");
        //Application.getInstance().show(mainForm);
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