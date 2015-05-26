package com.liigo.j2me.bombs;

import com.liigo.j2me.framework.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class BombsCanvas extends MyCanvas implements CommandListener
{
    static Options options = new Options();
    int xCount, yCount;
    int bombCount; //总雷数
    int defaultOpenCount;
    int xMargin, yMargin, cx, cy;
    int xFocus, yFocus;
    boolean isGameOver, isWin;
    int bombLeftCount; //剩余地雷数 (总雷数-标记雷区数)
    static Font gaveOverFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
    Rect canvasRect = new Rect(0,0, getWidth(), getHeight());
    Vector cells = new Vector();

    //如果不用双缓冲区的话，格数较多时(>10*8)稍微有些迟滞（<0.5s）
    Image memImage = Image.createImage(getWidth(), getHeight());
    boolean paintBuffered = false;


    private static BombsCanvas instance = new BombsCanvas();

    Command newGameCommand = new Command("新局", Command.SCREEN, 1);
    Command setupCommand = new Command("设置", Command.SCREEN, 1);
    Command helpCommand = new Command("帮助", Command.SCREEN, 1);
    Command aboutCommand = new Command("关于", Command.SCREEN, 1);
    Command exitCommand = new Command("退出", Command.SCREEN, 1);
    Command infoCommand = new Command("", Command.CANCEL, 0); //显示剩余地雷数, 或游戏结束时用于开始新局


    public static BombsCanvas getInstance()
    {
        return instance;
    }


    private BombsCanvas()
    {
        init();
    }

    public void init()
    {
        options.loadOptionsFromDB();
        setBackColor(0xffffff);
        xMargin = yMargin = 2;

        addCommand(newGameCommand);
        addCommand(setupCommand);
        addCommand(helpCommand);
        addCommand(aboutCommand);
        addCommand(exitCommand);
        addCommand(infoCommand);
        setCommandListener(this);
    }

    public void setOptions()
    {
        xCount = options.getXCount();
        yCount = options.getYCount();
        bombCount = options.getBombCount();
        defaultOpenCount = options.getDefaultOpenCount();
    }

    public Options getOptions()
    {
        return options;
    }

    public void commandAction(Command c, Displayable d)
    {
        if(c == newGameCommand)
        {
            newGame();
        }
        else if(c == setupCommand)
        {
            OptionsForm.getInstance().show();
        }
        else if(c == helpCommand)
        {
            MessageBox msg = new MessageBox("", MessageBox.TYPE_OK);
            msg.appendText("名称：扫雷\n");
            msg.appendText("目的：根据游戏提供的相关信息，揭开所有的非雷区。\n");
            msg.appendText("操作：上下左右键（或2,8,4,6键）分别用于上下左右移动当前活动区域（以红色虚线框标识）；"
                          +"5键（或1键，或OK键）用于揭开当前活动区域；"
                          +"3键用于将当前活动区域标记为有雷区（再次按下3键将取消该标记）。\n");
            msg.appendText("说明：未揭开区域显示为灰色背景；"
                          +"已揭开的非雷区显示为绿色背景，同时以文字标明其周围（八个区域）的地雷总数（每个区域最多只有一个地雷）；");
            msg.appendText("被标记为有雷的区域以黄色背景显示；一旦有雷区被揭开（以红色背景显示），则游戏结束。祝您玩的开心！\n");
            msg.appendText("liigo");
            msg.show();
        }
        else if(c == aboutCommand)
        {
            MessageBox msg = new MessageBox("", MessageBox.TYPE_OK);
            msg.appendText("扫雷 v1.0\n");
            msg.appendText("庄晓立(liigo)\n");
            msg.appendText("QQ: 175199125\n");
            msg.appendText("liigo@sina.com\n");
            msg.appendText("www.liigo.com\n");
            msg.appendText("http://blog.csdn.net/liigo/\n");
            msg.show();
        }
        else if(c == exitCommand)
        {
            ((BombsMidlet)Application.getInstance().getMidlet()).exit();
        }
        else if(c == infoCommand)
        {
            if(isGameOver() || isWin()) newGame();
        }

    }

    public void newGame()
    {
        MyLog.log("newGame()");

        setOptions(); //应用最新的设置信息

        xFocus = yFocus = 0;
        isGameOver = false;
        isWin = false;
        bombLeftCount = bombCount;
        setInfoCommandLabel();
        paintBuffered = false;
        System.gc();

        int i=0;
        cells.removeAllElements();
        for(i=0; i<getCellCount(); i++)
        {
            cells.addElement(new Cell());
        }

        //随机设置雷区
        int count = 0;
        MyRandom random = new MyRandom();
        while(true)
        {
            int x = random.nextInt(getCellCount());
            //MyLog.log("random: "+x);
            Cell cell = (Cell)cells.elementAt(x);
            if(!cell.isBomb())
            {
                cell.setIsBomb(true);
                count++;
                if(count >= bombCount) break;
            }
        }

        calcAroundBombs();
        defaultOpenCells();

        repaint();
    }

    //计算各Cell周围的雷数
    private void calcAroundBombs()
    {
        Cell cell = null;
        for(int i=0; i<getCellCount(); i++)
        {
            cell = getCell(i);
            cell.setAroundBombCount(getCellAroundBombs(i));
        }
    }

    private int getCellAroundBombs(int index)
    {
        if(!isValidIndex(index)) return 0;

        int i = getXIndex(index);
        int j = getYIndex(index);

        int count = 0;

        Cell cell = null;
        cell = getCell(i-1, j-1); if(cell!=null && cell.isBomb()) count++; //左上
        cell = getCell(i,   j-1); if(cell!=null && cell.isBomb()) count++; //上
        cell = getCell(i+1, j-1); if(cell!=null && cell.isBomb()) count++; //右上
        cell = getCell(i-1, j);   if(cell!=null && cell.isBomb()) count++; //左
        cell = getCell(i+1, j);   if(cell!=null && cell.isBomb()) count++; //右
        cell = getCell(i-1, j+1); if(cell!=null && cell.isBomb()) count++; //左下
        cell = getCell(i,   j+1); if(cell!=null && cell.isBomb()) count++; //下
        cell = getCell(i+1, j+1); if(cell!=null && cell.isBomb()) count++; //右下

        return count;
    }

    private int getXIndex(int index)
    {
        return (index % xCount);
    }

    private int getYIndex(int index)
    {
        return (index / xCount);
    }

    //揭开指定的Cell。如果周围没有雷，则把周围的Cell也揭开（递归处理）。
    private void openCell(int i, int j)
    {
        if(!isValidIndex(i, j)) return;
        Cell cell = getCell(i, j);
        if(cell==null || cell.isOpen()) return;
        //MyLog.log("openCell("+i+", "+j+")  "+cell);
        cell.openIt();
        if(cell.isBomb())
        {
            gameOver(); return;
        }
        paintCellToMemImage(i, j, false);
        if(cell.getAroundBombCount() != 0) return;

        openCell(i-1,j-1); openCell(i,j-1); openCell(i+1,j-1);
        openCell(i-1,j);                    openCell(i+1,j);
        openCell(i-1,j+1); openCell(i,j+1); openCell(i+1,j+1);
    }

    private void defaultOpenCells()
    {
        if(defaultOpenCount <= 0) return;

        MyRandom random = new MyRandom();
        int loopCount = 0;
        while(true)
        {
            int index = random.nextInt(getCellCount());

            Cell cell = getCell(index);
            if(cell.isOpen() || cell.isBomb()) continue;

            int i = getXIndex(index);
            int j = getYIndex(index);
            openCell(i, j);

            if(getOpenedCount() >= defaultOpenCount) break;

            //防止(可能的)无限循环
            if(loopCount++ > 2*getCellCount()) break;
        }
    }

    private int getOpenedCount()
    {
        //MyLog.log("getOpenedCount()");
        Cell cell = null;
        int count = 0;
        for(int i=0; i<getCellCount(); i++)
        {
            cell = getCell(i);
            if(cell!=null)
            {
                if(cell.isOpen()) count++;
            }
        }
        return count;
    }

    private boolean checkIsWin()
    {
        Cell cell = null;
        for(int i=0; i<getCellCount(); i++)
        {
            cell = getCell(i);
            if(cell!=null)
            {
                if(!cell.isBomb() && !cell.isOpen())
                {
                    return false;
                }
            }
        }
        return true;
    }

    private int getCellCount()
    {
        return (xCount*yCount);
    }

    private boolean isValidIndex(int index)
    {
        return (index>=0 && index<getCellCount());
    }

    private boolean isValidIndex(int i, int j)
    {
        return (i>=0 && i<xCount && j>=0 && j<yCount);
    }

    private Cell getFocusCell()
    {
        return getCell(getFocusCellIndex());
    }

    private Cell getCell(int index)
    {
        try
        {
            return (Cell)cells.elementAt(index);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private Cell getCell(int i, int j)
    {
        return getCell(getCellIndex(i,j));
    }

    private int getCellIndex(int i, int j)
    {
        if(!isValidIndex(i, j)) return -1;
        return (xCount * j) + i;
    }

    private int getFocusCellIndex()
    {
        return getCellIndex(xFocus, yFocus);
    }

    static int paintCount = 0;
    public void paint(Graphics g)
    {
        //MyLog.log("paint "+paintCount++);

        if(paintBuffered)
        {
            g.drawImage(memImage, 0,0, g.TOP|g.LEFT);
        }
        else
        {
            Graphics memG = memImage.getGraphics();
            paintAllCells(memG); //绘制到缓冲区中
            g.drawImage(memImage, 0,0, g.TOP|g.LEFT);
            paintBuffered = true;
        }
    }

    private void paintAllCells(Graphics g)
    {
        clear(g);

        cx = (getWidth()-xMargin*2)  / xCount;
        cy = (getHeight()-yMargin*2) / yCount;
        int xLen = cx * xCount;
        int yLen = cy * yCount;

        xMargin = (getWidth()  - xLen) / 2;
        yMargin = (getHeight() - yLen) / 2;

        int x, y;

        g.setColor(0x0000ff);
        g.setStrokeStyle(g.SOLID);

        g.drawRect(xMargin-1, yMargin-1, xLen+2, yLen+2);

        //画水平线
        x = xMargin; y = yMargin;
        for(int i=0; i<yCount+1; i++)
        {
            g.drawLine(x,y, x+xLen, y);
            y += cy;
        }

        //画垂直线
        x = xMargin; y = yMargin;
        for(int i=0; i<xCount+1; i++)
        {
            g.drawLine(x,y, x, y+yLen);
            x += cx;
        }

        //画Cells
        Cell cell = null;
        Rect rect = null;
        for(int i=0; i<xCount; i++)
        {
            for(int j=0; j<yCount; j++)
            {
                cell = getCell(getCellIndex(i, j));
                rect = getCellRect(i, j);
                cell.paint(g, rect, false);
            }
        }

        //画选中之的Cell
        cell = getFocusCell();
        rect = getCellRect(xFocus, yFocus);
        cell.paint(g, rect, true);

        if(isGameOver() || isWin())
        {
            //todo: 以半透明的形式显示背景区域
            //g.setColor(0xffffff); //white
            //g.fillRect(0, getHeight()/3, getWidth(), getHeight()/3);

            g.setFont(gaveOverFont);
            g.setColor(0x0); //black
            Cell.paintStringInRect(g, canvasRect, isGameOver() ? "游戏结束!" : "小子你赢了!");
        }
    }

    static Rect tempCellRect = new Rect();
    private Rect getCellRect(int i, int j)
    {
        //Rect tempCellRect = new Rect();
        tempCellRect.left = xMargin + 1 + cx*i;
        tempCellRect.right = tempCellRect.left + cx - 1;
        tempCellRect.top = yMargin + 1 + cy*j;
        tempCellRect.bottom = tempCellRect.top + cy - 1;
        return tempCellRect;
    }

    private void paintCellToMemImage(int i, int j, boolean focus)
    {
        Graphics g = memImage.getGraphics();

        Cell cell = getCell(i, j);
        Rect rect = getCellRect(i, j);
        cell.paint(g, rect, focus);
    }



    public void keyPressed(int keyCode)
    {
        //MyLog.log("keyPressed()");
        if(isGameOver() || isWin()) return;

        Cell cell = null;
        boolean bNeedUpdateInfoCommandLabel = false;

        paintCellToMemImage(xFocus, yFocus, false); //取消原焦点

        if(keyCode == KEY_NUM2 || getGameAction(keyCode) == UP) //上
        {
            if(yFocus>0) yFocus--; else yFocus=yCount-1;
        }
        else if(keyCode == KEY_NUM4 || getGameAction(keyCode) == LEFT) //左
        {
            if(xFocus>0) xFocus--; else xFocus=xCount-1;
        }
        else if(keyCode == KEY_NUM6 || getGameAction(keyCode) == RIGHT) //右
        {
            if(xFocus<xCount-1) xFocus++; else xFocus=0;
        }
        else if(keyCode == KEY_NUM8 || getGameAction(keyCode) == DOWN) //下
        {
            if(yFocus<yCount-1) yFocus++; else yFocus=0;
        }
        else if(keyCode == KEY_NUM1 || keyCode == KEY_NUM5 || getGameAction(keyCode) == FIRE)
        {
            cell = getCell(xFocus, yFocus);
            boolean bOldIsFlag = cell.isFlag(); //get it befor openCell()
            openCell(xFocus, yFocus);
            isWin = checkIsWin();
            if(isWin) paintBuffered = false;
            if(bOldIsFlag)
            {
                bombLeftCount++;
                bNeedUpdateInfoCommandLabel = true;
            }
        }
        else if(keyCode == KEY_NUM3)
        {
            cell = getFocusCell();
            if(!cell.isOpen())
            {
                //Flag it, or unFlag it
                cell.flagIt(!cell.isFlag());
                bombLeftCount = bombLeftCount + (cell.isFlag() ? -1 : 1);
                bNeedUpdateInfoCommandLabel = true;
            }
        }

        paintCellToMemImage(xFocus, yFocus, true); //画新的焦点
        repaint();

        if(bNeedUpdateInfoCommandLabel) setInfoCommandLabel();
    }

    private void setInfoCommandLabel()
    {
        if(isWin() || isGameOver())
            setInfoCommandLabel("新局");
        else
            setInfoCommandLabel("雷数: "+String.valueOf(bombLeftCount));
    }

    private void setInfoCommandLabel(String label)
    {
        removeCommand(infoCommand);
        infoCommand = new Command(label, Command.CANCEL, 0);
        addCommand(infoCommand);
    }

    private void gameOver()
    {
        MyLog.log("Game Over!");
        isGameOver = true;
        paintBuffered = false;
        repaint();
        setInfoCommandLabel();
    }

    public boolean isGameOver()
    {
        return isGameOver;
    }

    public boolean isWin()
    {
        return isWin;
    }

    public int getDefaultOpenCount()
    {
        return defaultOpenCount;
    }

    public void setDefaultOpenCount(int n)
    {
        int max = getCellCount() / 3;
        defaultOpenCount = (n <= max ? n : max);
    }

    //just copy from cldc1.1 source code
    class MyRandom extends Random
    {
        public MyRandom()
        {
            setSeed(System.currentTimeMillis());
        }
        public int nextInt(int n)
        {
            if (n<=0)
                return 0; //throw new IllegalArgumentException("n must be positive");

            if ((n & -n) == n)  // i.e., n is a power of 2
                return (int)((n * (long)next(31)) >> 31);

            int bits, val;
            do
            {
                bits = next(31);
                val = bits % n;
            }while(bits - val + (n-1) < 0);
            return val;
        }
    }

    public int getXCount()
    {
        return xCount;
    }

    public void setXCount(int value)
    {
        xCount = value;
    }

    public int getYCount()
    {
        return yCount;
    }

    public void setYCount(int value)
    {
        yCount = value;
    }
}