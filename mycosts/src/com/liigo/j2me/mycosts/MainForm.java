package com.liigo.j2me.mycosts;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import com.liigo.j2me.framework.*;
import com.liigo.j2me.mycosts.CostDB;
import com.liigo.util.MyLog;

public class MainForm extends List implements CommandListener,MessageBoxNotifyable
{
    Command okCommand = new Command("确定", Command.SCREEN, 1);
    Command exitCommand = new Command("退出", Command.EXIT, 1);
    CostDB costDB = new CostDB();

    private static MainForm form = new MainForm();
    public static MainForm getInstance()
    {
        return form;
    }

    private MainForm()
    {
        super("主菜单", List.IMPLICIT);
    }

    public void reset()
    {
        MyLog.log("MainForm.reset()");
        for(int i=size()-1; i>=0; i--) delete(i);

        if(!costDB.isOpen()) costDB.openDB();
        append("新增消费记录", null);
        append("浏览  (共"+costDB.getTotalCostString(CostDB.ALL_COSTS)+"元)", null);
        append("本日: "+costDB.getTotalCostString(CostDB.DAY_COSTS)+"元", null);
        append("本周: "+costDB.getTotalCostString(CostDB.WEEK_COSTS)+"元", null);
        append("本月: "+costDB.getTotalCostString(CostDB.MONTH_COSTS)+"元", null);
        append("本年: "+costDB.getTotalCostString(CostDB.YEAR_COSTS)+"元", null);
        append("分类统计", null);
        append("定义分类", null);
        append("清空消费记录", null);
        append("帮助", null);
        append("关于", null);
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
                    //todo: 列出本日消费
                    break;
                }
                case 3:
                {
                    //todo: 列出本周消费
                    break;
                }
                case 4:
                {
                    //todo: 列出本月消费
                    break;
                }
                case 5:
                {
                    //todo: 列出本年消费
                    break;
                }

                case 8:
                {
                    //清除所有消费数据
                    MessageBox msg = new MessageBox("您确认要清除所有消费记录吗？");
                    msg.setType(MessageBox.TYPE_YES_OR_NO);
                    msg.setListener(this, "confirmDeleteAll");
                    msg.show();
                    break;
                }
                case 9:
                {
                    MessageBox msg = new MessageBox("个人消费管理 v1.0");
                    msg.setTitle("帮助：");
                    msg.appendText("\n　　本软件用于对个人日常消费进行统计和管理。");
                    msg.appendText("\n　　有没有这样的经历？前几天刚从银行取了几百块钱，不记着怎么花呀，钱包又空了！");
                    msg.appendText("钱都哪里去了呢？是我花了，还是丢了，还是被偷了？也许可能似乎确实是真的被我花了吧:)");
                    msg.appendText("\n　　这年头，没老婆管着，钱去哪了连影都找不着！反过来说了，即使有老婆管着，你就不想留点私房钱？");
                    msg.appendText("\n　　上面最后一句话是我说的吗？答案与下面这个问题相反：“你不会是女的吧？”");
                    msg.appendText("\n　　总之，我们需要一个软件来帮忙。电脑软件不合适吧？");
                    msg.appendText("用电脑的时候通常不会花钱（网上订购除外），花钱的时候通常又没有电脑在身旁。手机软件当然是第一选择了，手机总是随身带着嘛。");
                    msg.appendText("\n庄晓立(liigo)");
                    msg.setListener(this, "help");
                    msg.show();
                    break;
                }
                case 10:
                {
                    MessageBox msg = new MessageBox("个人消费管理 v1.0");
                    msg.setTitle("关于：");
                    //msg.appendText("\n");
                    msg.appendText("\n作者: 庄晓立");
                    msg.appendText("\nliigo@sina.com");
                    msg.appendText("\nwww.liigo.com");
                    msg.appendText("\n2005年11月");
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
            MessageBox msg = new MessageBox(bOk?"已清除所有消费记录。":"操作失败！");
            msg.setTimeout(3000);
            msg.show();
            reset();
        }
    }
}