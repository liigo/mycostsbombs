package com.liigo.j2me.framework;


import javax.microedition.lcdui.*;
import com.liigo.util.MyLog;

/**
 * ��Form��ģ��ʵ��MessageBox
 *
 * ��MessageBox����������:
 * 1��setType(TYPE_OK)         ֻ��[ȷ��]��ť���û����º�رա�
 * 2��setType(TYPE_YES_OR_NO)  ��[��]��[��]������ť���û�ѡ���رա�
 * 3��setType(TYPE_AUTO_CLOSE) ֻ��[ȷ��]��ť����ʱʱ��(setTimeout(n))�����û�ѡ���رա�
 *
 * ��������ͨ��setListener()�ṩMessageBoxNotifyable�ӿ��Ի�ȡMessageBox���ر�֪ͨ��
 *
 * @author liigo
 */
//֮���Բ�ʹ��Alert������Ϊ������ʾ�͹رջ��Ʋ���Ϥ������ɳ������̻��ҡ�

public class MessageBox extends MyForm implements CommandListener
{
    public static final int TYPE_OK = 0;
    public static final int TYPE_YES_OR_NO = 1;
    public static final int TYPE_AUTO_CLOSE = 2;

    static Command okCommand  = new Command("ȷ��", Command.SCREEN, 1);
    static Command yesCommand = new Command("��", Command.OK, 1);
    static Command noCommand  = new Command("��", Command.CANCEL, 2);


    int type = TYPE_OK;
    String text = new String("");
    long timeout = 0;
    boolean waiting = false;
    boolean returnValue = false; //only use when type==TYPE_YES_OR_NO
    MessageBoxNotifyable listener = null;
    String listenerKey = null;


    /*
    //private static MessageBox msg = new MessageBox();

    public static MessageBox getInstance()
    {
        return MessageBox.getInstance("", MessageBox.TYPE_OK, 0);
    }
    public static MessageBox getInstance(String text)
    {
        return MessageBox.getInstance(text, MessageBox.TYPE_OK, 0);
    }
    public static MessageBox getInstance(String text, int type)
    {
        return MessageBox.getInstance(text, type, 0);
    }
    public static MessageBox getInstance(String text, int type, int timeout)
    {
        MessageBox msg = new MessageBox();
        msg.setText(text);
        msg.setType(type);
        msg.setTimeout(timeout);
        return msg;
    }
    */

    public MessageBox()
    {
        this("", MessageBox.TYPE_OK, 0);
    }

    public MessageBox(String text)
    {
        this(text, MessageBox.TYPE_OK, 0);
    }

    public MessageBox(String text, int type)
    {
        this(text, type, 0);
    }

    public MessageBox(String text, int type, int timeout)
    {
        init();
        setText(text);
        setType(type);
        setTimeout(timeout);
    }

    private void init()
    {
        reset();
    }

    public void setListener(MessageBoxNotifyable listener)
    {
        setListener(listener, null);
    }
    public void setListener(MessageBoxNotifyable listener, String key)
    {
        this.listener = listener;
        this.listenerKey = key;
    }

    private void reset()
    {
        waiting = false;
        returnValue = false;
        listener = null;
        listenerKey = null;

        for(int i=size()-1; i>=0; i--)
            delete(i);

        setTitle(null);

        removeCommand(okCommand);
        removeCommand(yesCommand);
        removeCommand(noCommand);
    }

    private void beforeShow()
    {
        append(getText());

        setCommandListener(this);

        if(getType() == TYPE_OK)
        {
            if(getTitle() == null) setTitle("��Ϣ: ");
            addCommand(okCommand);
        }
        else if(getType() == TYPE_YES_OR_NO)
        {
            if(getTitle() == null) setTitle("��ѡ��: ");
            addCommand(yesCommand);
            addCommand(noCommand);
        }
        else if(getType() == TYPE_AUTO_CLOSE)
        {
            if(getTitle() == null) setTitle("��Ϣ...");
            addCommand(okCommand);
        }

        returnValue = true;
    }

    public void commandAction(Command c, Displayable d)
    {
        returnValue = true;
        if(c == okCommand)
        {
        }
        else if(c == yesCommand)
        {

        }
        else if(c == noCommand)
        {
            returnValue = false;
        }
        waiting = false;

        if(!(getTimeout() > 0))
            close();
    }

    public boolean show()
    {
        beforeShow();
        Application.getInstance().show(this);
        waiting = true;

        if(getTimeout() > 0)
        {
            new Waiting().start();
        }

        return true;
    }

    class Waiting extends Thread
    {
        public void run()
        {
            long startTime = System.currentTimeMillis();
            long endTime = 0;
            while(waiting)
            {
                endTime = System.currentTimeMillis();
                if(endTime - startTime > getTimeout())
                    break;
                try
                {
                    Thread.sleep(200);
                }
                catch(Exception e)
                {
                }
            }
            close();
        }
    }

    public boolean close()
    {
        Application.getInstance().close();

        if(listener != null)
            listener.MessageBoxNotify(listenerKey, returnValue);
        return true;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int value)
    {
        type = value;
    }

    public void setType(int value, long timeout)
    {
        setType(value);
        setTimeout(timeout);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String value)
    {
        text = value;
    }

    public void appendText(String value)
    {
        text += value;
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(long value)
    {
        timeout = value;
        if(value > 0)
        {
            setType(MessageBox.TYPE_AUTO_CLOSE);
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("MessageBox: ");
        switch(getType())
        {
            case TYPE_OK:
                sb.append("[Ok] ");
                break;
            case TYPE_YES_OR_NO:
                sb.append("[YesOrNo] ");
                break;
            case TYPE_AUTO_CLOSE:
                sb.append("[AutoClose(");
                sb.append(getTimeout());
                sb.append(")] ");
                break;
            default:
                sb.append("[?] ");
        }
        sb.append(getText());
        return sb.toString();
    }
}