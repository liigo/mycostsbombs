package com.liigo.j2me.mycosts;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import com.liigo.j2me.framework.*;
import com.liigo.j2me.mycosts.CostDB;
import com.liigo.util.MyLog;

public class MainForm extends List implements CommandListener,MessageBoxNotifyable
{
    Command okCommand = new Command("ȷ��", Command.SCREEN, 1);
    Command exitCommand = new Command("�˳�", Command.EXIT, 1);
    CostDB costDB = new CostDB();

    private static MainForm form = new MainForm();
    public static MainForm getInstance()
    {
        return form;
    }

    private MainForm()
    {
        super("���˵�", List.IMPLICIT);
    }

    public void reset()
    {
        MyLog.log("MainForm.reset()");
        for(int i=size()-1; i>=0; i--) delete(i);

        if(!costDB.isOpen()) costDB.openDB();
        append("�������Ѽ�¼", null);
        append("���  (��"+costDB.getTotalCostString(CostDB.ALL_COSTS)+"Ԫ)", null);
        append("����: "+costDB.getTotalCostString(CostDB.DAY_COSTS)+"Ԫ", null);
        append("����: "+costDB.getTotalCostString(CostDB.WEEK_COSTS)+"Ԫ", null);
        append("����: "+costDB.getTotalCostString(CostDB.MONTH_COSTS)+"Ԫ", null);
        append("����: "+costDB.getTotalCostString(CostDB.YEAR_COSTS)+"Ԫ", null);
        append("����ͳ��", null);
        append("�������", null);
        append("������Ѽ�¼", null);
        append("����", null);
        append("����", null);
        if(costDB.isOpen()) costDB.closeDB();

        setCommandListener(this);

        removeCommand(okCommand);
        removeCommand(exitCommand);

        addCommand(okCommand);
        addCommand(exitCommand);
    }

    public void commandAction(Command c, Displayable d)
    {
        if(c == exitCommand)
        {
            getMidlet().exit();
            return;
        }
        if(c == List.SELECT_COMMAND || c == okCommand)
        {
            MyLog.log(String.valueOf(getSelectedIndex()));
            switch(getSelectedIndex())
            {
                case 0:
                    CostForm form = CostForm.getInstance();
                    form.setCost(null);
                    form.setSaveMode(CostForm.MODE_INSERT);
                    form.show();
                    break;
                case 1:
                    ListCostsForm.getInstance().show(); break;
                case 2:
                {
                    //todo: �г���������
                    break;
                }
                case 3:
                {
                    //todo: �г���������
                    break;
                }
                case 4:
                {
                    //todo: �г���������
                    break;
                }
                case 5:
                {
                    //todo: �г���������
                    break;
                }

                case 8:
                {
                    //���������������
                    MessageBox msg = new MessageBox("��ȷ��Ҫ����������Ѽ�¼��");
                    msg.setType(MessageBox.TYPE_YES_OR_NO);
                    msg.setListener(this, "confirmDeleteAll");
                    msg.show();
                    break;
                }
                case 9:
                {
                    MessageBox msg = new MessageBox("�������ѹ��� v1.0");
                    msg.setTitle("������");
                    msg.appendText("\n������������ڶԸ����ճ����ѽ���ͳ�ƺ͹���");
                    msg.appendText("\n������û�������ľ�����ǰ����մ�����ȡ�˼��ٿ�Ǯ����������ô��ѽ��Ǯ���ֿ��ˣ�");
                    msg.appendText("Ǯ������ȥ���أ����һ��ˣ����Ƕ��ˣ����Ǳ�͵�ˣ�Ҳ������ƺ�ȷʵ����ı��һ��˰�:)");
                    msg.appendText("\n��������ͷ��û���Ź��ţ�Ǯȥ������Ӱ���Ҳ��ţ�������˵�ˣ���ʹ�����Ź��ţ���Ͳ�������˽��Ǯ��");
                    msg.appendText("\n�����������һ�仰����˵���𣿴���������������෴�����㲻����Ů�İɣ���");
                    msg.appendText("\n������֮��������Ҫһ���������æ��������������ʰɣ�");
                    msg.appendText("�õ��Ե�ʱ��ͨ�����ỨǮ�����϶������⣩����Ǯ��ʱ��ͨ����û�е��������ԡ��ֻ������Ȼ�ǵ�һѡ���ˣ��ֻ�������������");
                    msg.appendText("\nׯ����(liigo)");
                    msg.setListener(this, "help");
                    msg.show();
                    break;
                }
                case 10:
                {
                    MessageBox msg = new MessageBox("�������ѹ��� v1.0");
                    msg.setTitle("���ڣ�");
                    //msg.appendText("\n");
                    msg.appendText("\n����: ׯ����");
                    msg.appendText("\nliigo@sina.com");
                    msg.appendText("\nwww.liigo.com");
                    msg.appendText("\n2005��11��");
                    msg.setListener(this, "about");
                    msg.show();
                    break;
                }
                default: break;
            }
        } // end if

    }

    public MyCostsMidlet getMidlet()
    {
        return (MyCostsMidlet)(getApplication().getMidlet());
    }
    public Application getApplication()
    {
        return Application.getInstance();
    }
    public boolean show()
    {
        reset();
        return getApplication().show(this);
    }

    public void MessageBoxNotify(String key, boolean b)
    {
        MyLog.log("MessageBox notify: " + key + ", " + b);
        if(b && key.equals("confirmDeleteAll"))
        {
            boolean bOk = DB.deleteDB("costs");
            MessageBox msg = new MessageBox(bOk?"������������Ѽ�¼��":"����ʧ�ܣ�");
            msg.setTimeout(3000);
            msg.show();
            reset();
        }
    }
}