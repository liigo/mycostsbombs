package com.liigo.j2me.framework;

import javax.microedition.lcdui.Form;

public class MyForm extends Form
{
    public MyForm()
    {
        super("");
    }

    public MyForm(String title)
    {
        super(title);
        this.setTitle(title);
    }

    public Application getApplication()
    {
        return Application.getInstance();
    }

    public boolean show()
    {
        return getApplication().show(this);
    }

    public boolean close()
    {
        return getApplication().close();
    }
}