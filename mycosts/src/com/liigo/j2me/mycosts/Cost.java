package com.liigo.j2me.mycosts;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import com.liigo.j2me.framework.Encodable;
import com.liigo.util.MyLog;

/**
 *
 * @author liigo
 */
public class Cost implements Encodable
{
    int id;
    int money;
    int type;
    String memo;
    Date date = new Date();
    int dataVersion;
    static int lastVersion = 0;


    public Object clone()
    {
        Cost cost = new Cost();
        cost.setDataVersion(getDataVersion());
        cost.setId(getId());
        cost.setMoney(getMoney());
        cost.setType(getType());
        cost.setMemo(getMemo());
        cost.setDate(getDate());
        return cost;
    }

    public Cost()
    {
        init();
    }

    private void init()
    {
        memo = "";
        money = 0;
        type = 0;
        date.setTime(System.currentTimeMillis());
        dataVersion = lastVersion;
    }

    public byte[] encode()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try
        {
            byte[] result;
            dos.writeInt(dataVersion);
            dos.writeInt(id);
            dos.writeInt(money);
            dos.writeInt(type);
            dos.writeLong(date.getTime());
            dos.writeUTF(memo);
            result = baos.toByteArray();
            dos.close();
            baos.close();
            return result;
        }
        catch(IOException e)
        {
            return new byte[0];
        }
        finally
        {
            try
            {
                dos.close();
                baos.close();
            }
            catch(IOException e2){}
        }
    }

    public Object decode(byte[] datas)
    {
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(datas);
            DataInputStream dis = new DataInputStream(bais);
            dataVersion = dis.readInt();
            id = dis.readInt();
            money = dis.readInt();
            type = dis.readInt();
            date = new Date(dis.readLong());
            memo = dis.readUTF();
            dis.close();
            bais.close();
            return this.clone();
        }
        catch(IOException e)
        {
             return null;
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int value)
    {
        id = value;
    }

    public int getMoney()
    {
        return money;
    }

    /**
     * 此处金额单位是"分", 以保证数值为整数
     */
    public void setMoney(int value)
    {
        money = value;
    }

    /**
     * 将单位为"元"的金额(String)转换成单位为"分"的金额(int)
     * 参数可以是一个小数形式的文本, 如"1.5".
     * 如果小数位部分超过两位, 多出部分将被剪切, 不会自动四舍五入.
     * 因为MIDP1.0不支持float类型, 故以文本形式来描述小数.
     */
    public boolean setMoney(String value)
    {
        try
        {
            StringBuffer sb = new StringBuffer();

            int dotIndex = value.indexOf(".");
            if(dotIndex == -1)
            {
                value = value + "00";
            }
            else
            {
                sb.append(value.substring(0, dotIndex));
                String s = value.substring(dotIndex+1) + "000";
                sb.append(s.substring(0, 2));

                value = sb.toString();
            }
            setMoney(Integer.parseInt(value));
            return true;
        }
        catch(Exception e)
        {
            MyLog.error(this, "setMoney("+value+")", e);
            return false;
        }
    }

    public String getMoneyString()
    {
        String s = String.valueOf(getMoney());

        if(s.endsWith("00"))
        {
            return s.substring(0, s.length()-2);
        }

        int n = s.length();
        if(s.equals("0")) return "0";
        if(n <= 2)
            return "0."+("00"+s).substring(n);
        return s.substring(0,n-2) + "." + s.substring(n-2);
    }

    public int getType()
    {
        return type;
    }

    public void setType(int value)
    {
        type = value;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String value)
    {
        memo = value;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date value)
    {
        date.setTime(value.getTime());
    }

    public int getDataVersion()
    {
        return dataVersion;
    }

    public void setDataVersion(int value)
    {
        dataVersion = value;
    }

    public String getDateString()
    {
        StringBuffer sb = new StringBuffer();
        Calendar c = Calendar.getInstance();
        c.setTime(getDate());
        sb.append("["); sb.append(c.get(c.YEAR));
        sb.append("-"); sb.append(c.get(c.MONTH)+1); //!
        sb.append("-"); sb.append(c.get(c.DATE));
        sb.append("]");
        return sb.toString();
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(getDateString());
        sb.append(" ");
        sb.append(getMoneyString() + "元");
        if(getMemo().length() > 0)
            sb.append(", " + getMemo());
        return sb.toString();
    }

    public static void test(boolean b)
    {
        MyLog.log(b ? "[test OK]" : "[test ERROR!] ------------ ");
    }

    //内部测试
    //by liigo
    public static void main(String[] args)
    {
        Cost cost = new Cost();
        cost.setMoney(100);

        byte[] datas = null;

        datas = cost.encode();

        cost.setMoney(0);
        MyLog.log(cost.getMoney());
        MyLog.log(cost.getDate().getTime());

        cost.decode(datas);

        MyLog.log(cost.getMoney());
        test(cost.getMoney() == 100);

        cost.setMoney("1.2385");
        test(cost.getMoney()==123);
        test(cost.getMoneyString().equals("1.23"));

        cost.setMoney("1");
        test(cost.getMoney()==100);
        test(cost.getMoneyString().equals("1"));



        MyLog.log(cost);
    }
}