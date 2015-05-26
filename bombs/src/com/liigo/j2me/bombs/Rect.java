package com.liigo.j2me.bombs;

import com.liigo.j2me.framework.MyLog;

public class Rect
{
    public int left;
    public int right;
    public int top;
    public int bottom;

    public Rect()
    {
        this(0,0,0,0);
    }
    public Rect(int left, int top, int right, int bottom)
    {
        //MyLog.log("new Rect");
        this.left = left;
        this.top  = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getHeight()
    {
        return bottom - top;
    }
    public int getWidth()
    {
        return right - left;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Rect: ("); sb.append(left); sb.append(","); sb.append(top); sb.append(") ");
        sb.append(getWidth()); sb.append("*"); sb.append(getHeight());
        return sb.toString();
    }
}