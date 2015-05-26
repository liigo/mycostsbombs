package com.liigo.j2me.bombs;

import javax.microedition.lcdui.*;
import com.liigo.j2me.framework.*;

public class OptionsForm extends MyForm implements CommandListener
{
    Options options = null;
    BombsCanvas bombsCanvas = null;

    TextField xCountTextField = new TextField("�������: ", "", 2, TextField.NUMERIC);
    TextField yCountTextField = new TextField("�������: ", "", 2, TextField.NUMERIC);
    TextField bombCountTextField = new TextField("���׸���: ", "", 2, TextField.NUMERIC);
    TextField defaultOpenCountTextField = new TextField("�Զ��ҿ�����: ", "", 2, TextField.NUMERIC);

    Command saveCommand = new Command("����", Command.SCREEN, 1);
    Command cancleCommand = new Command("����", Command.CANCEL, 1);

    private static OptionsForm intance = new OptionsForm();
    static public OptionsForm getInstance()
    {
        return intance;
    }

    private OptionsForm()
    {
        init();
    }

    private void init()
    {
        setTitle("����");
        bombsCanvas = BombsCanvas.getInstance();
        options = bombsCanvas.getOptions();
        initValues();

        append(xCountTextField);
        append(yCountTextField);
        append(bombCountTextField);
        append(defaultOpenCountTextField);

        addCommand(saveCommand);
        addCommand(cancleCommand);

        setCommandListener(this);
    }

    private void initValues()
    {
        xCountTextField.setString(String.valueOf(options.getXCount()));
        yCountTextField.setString(String.valueOf(options.getYCount()));
        bombCountTextField.setString(String.valueOf(options.getBombCount()));
        defaultOpenCountTextField.setString(String.valueOf(options.getDefaultOpenCount()));
    }

    public void commandAction(Command c, Displayable d)
    {
        if(c == saveCommand)
        {
            save();
        }
        else if(c == cancleCommand)
        {
            close();
        }
    }

    private void save()
    {
        int xCount=0, yCount=0, bombCount=0, defaultOpenCount=0;
        try
        {
            xCount = Integer.valueOf(xCountTextField.getString()).intValue();
            yCount = Integer.valueOf(yCountTextField.getString()).intValue();
            bombCount = Integer.valueOf(bombCountTextField.getString()).intValue();
            defaultOpenCount = Integer.valueOf(defaultOpenCountTextField.getString()).intValue();
        }
        catch(NumberFormatException e)
        {
        }


        if(xCount * yCount <= 0)
        {
            MessageBox messageBox = new MessageBox("", MessageBox.TYPE_OK);
            messageBox.setText("�ܸ������������*����������������0��");
            messageBox.show();
            return;
        }
        if(bombCount<=0 || bombCount >= xCount*yCount)
        {
            MessageBox messageBox = new MessageBox("", MessageBox.TYPE_OK);
            messageBox.setText("���׸����������0��С���ܸ�����");
            messageBox.show();
            return;
        }

        int maxOpenCount = (xCount*yCount-bombCount)/2;
        if(defaultOpenCount<0 || defaultOpenCount>maxOpenCount)
        {
            MessageBox messageBox = new MessageBox("", MessageBox.TYPE_OK);
            messageBox.setText(defaultOpenCount<0 ? "�Զ��ҿ���������С���㣡"
                :"�Զ��ҿ��������ֻ��Ϊ������������һ�루"+maxOpenCount+"��");
            messageBox.show();
            return;
        }

        options.setXCount(xCount);
        options.setYCount(yCount);
        options.setBombCount(bombCount);
        options.setDefaultOpenCount(defaultOpenCount);

        if(options.saveOptionsToDB())
        {
            close();
            MessageBox messageBox = new MessageBox("", MessageBox.TYPE_AUTO_CLOSE, 3500);
            messageBox.setText("������Ϣ�Ѿ�����ɹ�������һ�ֿ�ʼ��Ч��");
            messageBox.show();
        }
        else
        {
            MessageBox msg = new MessageBox("������Ϣ����ʧ�ܣ�", MessageBox.TYPE_OK);
            msg.show();
        }
    }
}