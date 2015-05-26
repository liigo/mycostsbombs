package com.liigo.j2me.bombs;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import com.liigo.j2me.framework.MyLog;

public class Cell
{
    boolean isBomb = false;
    boolean isOpen = false;
    boolean isFlag = false;
    int aroundBombCount = 0;

    public Cell()
    {
        //MyLog.log("new Cell()");
        init();
    }

    public Cell(boolean isBomb, boolean isOpen, boolean isFlag, int arroundBombCount)
    {
        this.isBomb = isBomb;
        this.isOpen = isOpen;
        this.isFlag = isFlag;
        this.aroundBombCount = aroundBombCount;
    }

    private void init()
    {
        isBomb = false;
        isOpen = false;
        isFlag = false;
        aroundBombCount = 0;
    }

    public void openIt()
    {
        if(isOpen()) return;

        setIsOpen(true);
        setIsFlag(false);
    }

    public void flagIt(boolean b)
    {
        if(isOpen()) return;
        setIsFlag(b);
    }

    static Font smallFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    public void paint(Graphics g, Rect cellRect, boolean focus)
    {
        //MyLog.log("cell paint()");
        int color = 0xffffff;
        if(isOpen()) color = isBomb() ? 0xff0000 : 0x00ff00; //red : green
        else if(isFlag()) color = 0xffff00; //yellow
        else color = 0xcccccc; //gray

        g.setStrokeStyle(g.SOLID);

        //»­±³¾°
        g.setColor(color);
        fillRect(g, cellRect);

        g.setFont(smallFont);

        if(isOpen())
        {
             if(isBomb())
             {
                //µØÀ×±¬Õ¨
                g.setColor(0xffffff); //white
                paintStringInRect(g, cellRect, "@");
             }
             else
             {
                //»­ÎÄ×Ö
                g.setColor(0x0000ff); //blue
                StringBuffer sb = new StringBuffer();
                sb.append(aroundBombCount);
                paintStringInRect(g, cellRect, sb.toString());
             }
        }
        else if(isFlag())
        {
            //»­±ê¼Ç
            g.setColor(0x0000ff); //blue
            paintStringInRect(g, cellRect, "#");
        }

        //»­½¹µã¿ò
        if(focus)
        {
            g.setStrokeStyle(g.DOTTED);
            g.setColor(0xff0000); //red
            g.drawRoundRect(cellRect.left+1, cellRect.top+1, cellRect.getWidth()-3, cellRect.getHeight()-3,
                            2,2);
        }

    }

    public static void paintStringInRect(Graphics g, Rect rect, String string)
    {
        int fontHeight = g.getFont().getHeight();
        g.drawString(string,
                     rect.left+rect.getWidth()/2, rect.top+rect.getHeight()/2 + fontHeight/2,
                     g.HCENTER|g.BOTTOM);
    }

    private void fillRect(Graphics g, Rect rect)
    {
        g.fillRect(rect.left, rect.top, rect.getWidth(), rect.getHeight());
    }

    public boolean isBomb()
    {
        return isBomb;
    }

    public void setIsBomb(boolean value)
    {
        isBomb = value;
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public void setIsOpen(boolean value)
    {
        isOpen = value;
    }

    public boolean isFlag()
    {
        return isFlag;
    }

    public void setIsFlag(boolean value)
    {
        isFlag = value;
    }

    public int getAroundBombCount()
    {
        return aroundBombCount;
    }

    public void setAroundBombCount(int value)
    {
        aroundBombCount = value;
    }

    /*
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Cell: ");
        if(isBomb()) sb.append("[isBomb]");
        if(isOpen()) sb.append("[isOpened]");
        if(isFlag()) sb.append("[isFlaged]");
        sb.append("["); sb.append(aroundBombCount); sb.append("]");
        return sb.toString();
    }
    */
}