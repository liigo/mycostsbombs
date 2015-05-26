package com.liigo.j2me.framework;

import java.util.Hashtable;
import java.util.Stack;
import java.util.EmptyStackException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Alert;
import com.liigo.j2me.framework.*;
import com.liigo.util.MyLog;


/**
 * 请使用 Application.getInstance() 获取本类的唯一实例。
 * 请在 MIDlet 初始化时第一时间设置本类唯一实例的 midlet 成员。
 *
 * @author liigo
 */
public class Application
{
    MIDlet midlet = null;
    Display display = null;
    Object obj = null;
    Hashtable datas = new Hashtable();
    Stack displays = new Stack();

    private static Application application = new Application();
    public static Application getInstance()
    {
        return application;
    }

    //private!
    private Application()
    {
    }

    public boolean show(Displayable display)
    {
        return show(display, false);
    }

    /**
     * 显示指定窗口
     * 参数autoClose指定该display是否会自动关闭, 通常用于Alert.
     */
    public boolean show(Displayable display, boolean notSave)
    {
        try
        {
            getInstance().getDisplay().setCurrent(display);
            MyLog.log_thisline("[show] "); MyLog.log(display);

            if(notSave)
                return true;
            try
            {
                if(display != displays.peek())  //同一个窗体不需要连续记录多次
                    displays.push(display);
            }
            catch(EmptyStackException ese)
            {
                displays.push(display);
            }

            return true;
        }
        catch(Exception e)
        {
            MyLog.error(display, "show()", e);
            return false;
        }
    }

    public Displayable getCurrentDisplayable()
    {
        return (Displayable)displays.peek();
    }

    public boolean close()
    {
        try
        {
           Object display = displays.pop();
            if(displays.empty())
            {
                displays.push(display);
                return false;
            }
            MyLog.log_thisline("[close] "); MyLog.log(display);

            Displayable d = (Displayable)displays.peek();
            if(d instanceof MyForm)
                return ((MyForm)d).show();
            else if(d instanceof MyCanvas)
                return ((MyCanvas)d).show();
            else
                return show(d, true);
        }
        catch(EmptyStackException ese)
        {
            MyLog.error(this, "[close error]: no Displayable to close", ese);
            return false;
        }
        catch(Exception e)
        {
            MyLog.error(this, "[close error]", e);
            return false;
        }
    }

    public MIDlet getMidlet()
    {
        return midlet;
    }

    public void setMidlet(MIDlet value)
    {
        midlet = value;
        display = Display.getDisplay(midlet);
    }

    public Display getDisplay()
    {
        return display;
    }

    public void setDisplay(Display value)
    {
        display = value;
    }

    public Object getObj()
    {
        return obj;
    }

    public void setObj(Object value)
    {
        obj = value;
    }

    public Hashtable getDatas()
    {
        return datas;
    }

    public void setDatas(Hashtable value)
    {
        datas = value;
    }

    public void addData(String key, Object value)
    {
        if(datas == null)
            datas = new Hashtable();
        datas.put(key, value);
    }

    public Object getData(String key)
    {
        if(key == null) return null;
        if(datas == null) return null;
        return datas.get(key);
    }

    public static void test(boolean b)
    {
        MyLog.log(b ? "[test OK]" : "[test ERROR!] ------------ ");
    }

    public static void myAssert(boolean b)
    {
        if(b == false)
            MyLog.error("assert error!");
    }

    public static void main (String[] args)
    {
        MyLog.log("test");
        Application app1 = Application.getInstance();
        Application app2 = Application.getInstance();
        myAssert(app1 == app2);

        MyLog.log("end");
    }
}