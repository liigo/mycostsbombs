package com.liigo.j2me.mycosts;

import javax.microedition.lcdui.*;
import java.util.Vector;
import java.util.Enumeration;
import com.liigo.j2me.framework.*;
import com.liigo.util.MyLog;


public class ListCostsForm extends List implements CommandListener,MessageBoxNotifyable
{
    private CostDB costDB = new CostDB();
    private Command cancelCommand = new Command("����", Command.BACK, 1);
    private Command viewCommand = new Command("�鿴", Command.SCREEN, 1);
    private Command editCommand = new Command("�޸�", Command.SCREEN, 1);
    private Command deleteCommand = new Command("ɾ��", Command.SCREEN, 1);


    private static ListCostsForm form = new ListCostsForm();
    public static ListCostsForm getInstance()
    {
        return form;
    }

    private ListCostsForm()
    {
        super("������Ѽ�¼", List.IMPLICIT);
        init();
    }

    private void init()
    {
        setCommandListener(this);
        addCommand(cancelCommand);
        addCommand(viewCommand);
        addCommand(editCommand);
        addCommand(deleteCommand);

    }

    public void commandAction(Command c, Displayable d)
    {
        MyLog.log(c.getLabel() + "��ѡ��");
        Cost cost = null;
        try
        {
            cost = (Cost) v.elementAt(getSelectedIndex());
        }
        catch(Exception e)
        {
            cost = null;
        }

        if(c == cancelCommand)
        {
            MainForm.getInstance().reset();
            Application.getInstance().close();
        }

        if(cost == null) return;

        if(c == viewCommand || c == List.SELECT_COMMAND)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(/*"���ڣ�" +*/ cost.getDateString() + "\n");
            sb.append("��" + cost.getMoneyString() + "Ԫ\n");
            sb.append("˵����" + cost.getMemo());

            MessageBox msg = new MessageBox();
            msg.setText(sb.toString());
            msg.show();
        }
        else if(c == editCommand)
        {
            CostForm form = CostForm.getInstance();
            form.setCost(cost);
            form.setSaveMode(form.MODE_EDIT);
            form.show();
        }
        else if(c == deleteCommand)
        {
            MessageBox msg = new MessageBox("��ȷ��Ҫɾ����" +cost.toString()+"����?");
            msg.setType(MessageBox.TYPE_YES_OR_NO);
            msg.setListener(this, "delete?");
            msg.show();
        }
    }

    static Vector v = null;
    static Cost tempCost = new Cost();
    private void getAllCosts()
    {
        costDB.openDB();
        v = costDB.getObjects(tempCost);
        costDB.closeDB();
        MyLog.log("allCosts: " + v.size());
    }

    public boolean show()
    {
        for(int i=size()-1; i>=0; i--) delete(i);

        getAllCosts();
        if(v != null)
        {
            Cost cost = null;
            Enumeration e = v.elements();
            while(e.hasMoreElements())
            {
                cost = (Cost) e.nextElement();
                MyLog.log("enum cost: "+cost);
                if(cost != null)
                    append(cost.toString(), null);
                else
                    append("--[��]--", null);
            }
        }
        return Application.getInstance().show(this);
    }

    private void deleteCost()
    {
        Cost cost = (Cost) v.elementAt(getSelectedIndex());
        Application.myAssert(cost != null);
        if(cost == null) return;

        if(costDB.openDB())
        {
            boolean bOk = costDB.deleteRecord(cost.getId());
            costDB.closeDB();
            MessageBox msg = new MessageBox(bOk?"��ɾ����":"ɾ��ʧ��! ");
            msg.setListener(this, "deleteResult");
            msg.show();
        }
        else
        {
            MyLog.error("costDB.openDB()");
        }
    }

    public void MessageBoxNotify(String key, boolean b)
    {
        MyLog.log("MessageBox notify: " + key + " " + b);

        if(key.equals("delete?"))
        {
            if(b) deleteCost();
        }
        else if(key.equals("deleteResult"))
        {
            show();
        }
    }
}