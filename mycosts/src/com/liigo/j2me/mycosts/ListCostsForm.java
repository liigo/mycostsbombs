package com.liigo.j2me.mycosts;

import javax.microedition.lcdui.*;
import java.util.Vector;
import java.util.Enumeration;
import com.liigo.j2me.framework.*;
import com.liigo.util.MyLog;


public class ListCostsForm extends List implements CommandListener,MessageBoxNotifyable
{
    private CostDB costDB = new CostDB();
    private Command cancelCommand = new Command("返回", Command.BACK, 1);
    private Command viewCommand = new Command("查看", Command.SCREEN, 1);
    private Command editCommand = new Command("修改", Command.SCREEN, 1);
    private Command deleteCommand = new Command("删除", Command.SCREEN, 1);


    private static ListCostsForm form = new ListCostsForm();
    public static ListCostsForm getInstance()
    {
        return form;
    }

    private ListCostsForm()
    {
        super("浏览消费记录", List.IMPLICIT);
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
        MyLog.log(c.getLabel() + "被选择");
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
            sb.append(/*"日期：" +*/ cost.getDateString() + "\n");
            sb.append("金额：" + cost.getMoneyString() + "元\n");
            sb.append("说明：" + cost.getMemo());

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
            MessageBox msg = new MessageBox("您确认要删除“" +cost.toString()+"”吗?");
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
                    append("--[空]--", null);
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
            MessageBox msg = new MessageBox(bOk?"已删除。":"删除失败! ");
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