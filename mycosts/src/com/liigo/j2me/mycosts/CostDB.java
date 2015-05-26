package com.liigo.j2me.mycosts;

import javax.microedition.rms.*;
import java.util.*;
import com.liigo.j2me.framework.DB;
import com.liigo.j2me.mycosts.Cost;
import com.liigo.util.MyLog;

public class CostDB extends DB
{
    String dbName = null;
    public CostDB()
    {
        super();
        setDbName("costs");
    }

    public static int ALL_COSTS = 0;
    public static int DAY_COSTS = 1;
    public static int WEEK_COSTS = 2;
    public static int MONTH_COSTS = 3;
    public static int YEAR_COSTS = 4;

    public String getTotalCostString(int type)
    {
        int costs = getTotalCosts(type);
        StringBuffer sb = new StringBuffer();
        sb.append(costs);
        int len = sb.length();
        if(len > 2)
            sb.insert(len-2, '.');
        else if(len == 2)
            sb.insert(0, "0.");
        else if(len == 1)
            sb.insert(0, "0.0");
        else
            sb = new StringBuffer("0");

        String s = sb.toString();
        if(s.endsWith(".00")) s = s.substring(0, s.length()-3);
        return s;
    }

    public int getTotalCosts(int type)
    {
        boolean bNeedCloseDB = false;
        if(!isOpen())
        {
            openDB();
            bNeedCloseDB = true;
        }

        Vector v = new Vector();
        Cost cost = new Cost();
        v = this.getObjects(cost);
        if(v.size() == 0) return 0;

        Calendar today = Calendar.getInstance();
        int y = today.get(Calendar.YEAR);
        int m = today.get(Calendar.MONTH);
        int d = today.get(Calendar.DATE);
        int w = today.get(Calendar.DAY_OF_WEEK);

        Calendar tempCalendar = Calendar.getInstance();

        int sum = 0;
        for(int i=v.size()-1; i>=0; i--)
        {
            cost = (Cost)v.elementAt(i);
            if(cost == null) continue;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cost.getDate());

            switch(type)
            {
            case 1: //DAY_COSTS:
                if(calendar.get(Calendar.DATE) != d) continue;
                break;
            case 2: //WEEK_COSTS:
                setCalendarAWeekAgo(tempCalendar, y,m,d);
                if(calendar.before(tempCalendar)) continue;
                //break;
            case 3: //MONTH_COSTS:
                setCalendar(tempCalendar, y,m,1);
                if(calendar.before(tempCalendar)) continue;
                break;
            case 4: //YEAR_COSTS:
                setCalendar(tempCalendar, y,0,1);
                if(calendar.before(tempCalendar)) continue;
                break;
            }

            sum += cost.getMoney();
        } // end for

        if(bNeedCloseDB) closeDB();
        return sum;
    }

    /*
       todo: 将以下方法重构到它应该呆的地方。 liigo
    */

    //根据年月日设置calendar
    private void setCalendar(Calendar calendar, int y, int m, int d)
    {
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DATE, d);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    //将calendar设置为指定日期一周之前的日期
    private void setCalendarAWeekAgo(Calendar calendar, int y, int m, int d)
    {
        if(d > 7)
        {
            d -= 7;
            setCalendar(calendar, y, m, d);
            return;
        }

        m--;
        d = getMonthDayCount(y, m) + d - 7;
        setCalendar(calendar, y, m, d);
    }

    //取某一年中某一月的天数, m: 0..11
    private int getMonthDayCount(int y, int m)
    {
        m++;
        if(m<1 || m>12) return 0;
        if(m==1||m==3||m==5||m==7||m==8||m==10||m==12) return 31;
        if(m==4||m==6||m==9||m==11) return 30;
        if(m==2)
        {
            return isLeapYear(y) ? 28 : 29;
        }
        return 0;
    }

    //判断某一年是不是闰年
    private boolean isLeapYear(int y)
    {
        if(y%100 == 0)
            return (y%400 == 0);
        else
            return (y%4 == 0);
    }
}