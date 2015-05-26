package com.liigo.j2me.mycosts;

import javax.microedition.lcdui.*;
import java.util.Date;
import com.liigo.j2me.framework.*;
import com.liigo.j2me.mycosts.Cost;
import com.liigo.util.MyLog;

public class CostForm extends MyForm implements CommandListener, MessageBoxNotifyable
{
    public static int MODE_INSERT = 0;
    public static int MODE_EDIT   = 1;

    int mode;
    Cost cost = null;

    private DB db = new DB("costs");

    private Command saveCommand = new Command("����", Command.SCREEN, 1);
    private Command cancelCommand = new Command("ȡ��", Command.CANCEL, 1);

    private TextField moneyField = new TextField("���(Ԫ)��", "", 10, TextField.NUMERIC);
    private TextField memoField = new TextField("˵����", "", 100, TextField.ANY);
    private ChoiceGroup typeChoiceGroup = new ChoiceGroup("���ͣ�", Choice.EXCLUSIVE);
    private DateField dateField = new DateField("�������ڣ�", DateField.DATE);

    private Date tempDate = new Date();

    private static CostForm form = new CostForm();
    public static CostForm getInstance()
    {
        return form;
    }

    private CostForm()
    {
        init();
    }

    public void setSaveMode(int mode)
    {
        this.mode = mode;
    }
    public int getSaveMode()
    {
        return mode;
    }

    public void setCost(Cost cost)
    {
        this.cost = cost;
    }
    public Cost getCost()
    {
        return cost;
    }

    private void init()
    {
        setSaveMode(this.MODE_INSERT);
        append(moneyField);
        append(memoField);
        //append(typeChoiceGroup);
        append(dateField);
        typeChoiceGroup.append("����1", null);
        typeChoiceGroup.append("����2", null);
        typeChoiceGroup.append("����N", null);

        setCommandListener(this);
        addCommand(saveCommand);
        addCommand(cancelCommand);
    }

    public boolean show()
    {
        beforeShow();
        return super.show();
    }

    private void beforeShow()
    {
        try
        {
            if(getSaveMode() == this.MODE_EDIT && cost != null)
            {
                MyLog.log("money: " + cost.getMoneyString());
                moneyField.setString(cost.getMoneyString());
                memoField.setString(cost.getMemo());
                typeChoiceGroup.setSelectedIndex(0, true);
                dateField.setDate(cost.getDate());
            }
            else
            {
                moneyField.setString("");
                memoField.setString("");
                typeChoiceGroup.setSelectedIndex(0, true);
                tempDate.setTime(System.currentTimeMillis());
                dateField.setDate(tempDate);
            }
        }
        catch(IllegalArgumentException e)
        {
            MyLog.error(this, "initValue()", e);
        }
    }

    public void commandAction(Command c, Displayable d)
    {
        if(c == saveCommand)
        {
            save();
        }
        else if(c == cancelCommand)
        {
            close();
        }
    }

    public void save()
    {
        if(db.openDB() == false)
            return;
        if(getSaveMode() == this.MODE_EDIT  && cost == null)
            return;

        if(cost == null)
            cost = new Cost();
        cost.setMoney(moneyField.getString());
        cost.setMemo(memoField.getString());
        cost.setType(0); //typeChoiceGroup.getSelectedIndex()); //todo
        cost.setDate(dateField.getDate());
        MyLog.log("saved date: " + dateField.getDate().toString());

        boolean bOk = false;
        if(getSaveMode() == this.MODE_INSERT)
        {
            cost.setId(db.getNextRecordID());
            bOk = db.addRecord(cost.encode()) != -1;
        }
        else if(getSaveMode() == this.MODE_EDIT)
        {
            bOk = db.setRecord(cost.getId(), cost.encode());
        }
        db.closeDB();

        MessageBox msg = new MessageBox(bOk ? "��¼�ѱ��档" : "����ʧ�ܣ�");
        if(bOk)
            msg.setType(MessageBox.TYPE_AUTO_CLOSE, 3000);
        else
            msg.setType(MessageBox.TYPE_OK);
        msg.setListener(this, bOk ? "saveOk" : "saveFailed");
        msg.show();
    }

    public void MessageBoxNotify(String key, boolean b)
    {
        if(key.equals("saveOk"))
        {
            //�������˵�����
            Application.getInstance().close();
            ListCostsForm.getInstance().show();
        }
    }
}