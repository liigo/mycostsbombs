package com.liigo.j2me.bombs;

import java.io.*;
import java.util.*;
import com.liigo.j2me.framework.*;


public class Options extends DB implements Encodable
{
    int dataVersion = 0;
    int id = 0;
    int xCount, yCount;
    int bombCount;
    int defaultOpenCount;

    public Options()
    {
        init();
    }

    public Options(Options o)
    {
        init();
        setValues(o);
    }

    private void setValues(Options o)
    {
        this.dataVersion = o.getDataVersion();
        this.id = o.getId();
        this.xCount = o.getXCount();
        this.yCount = o.getYCount();
        this.bombCount = o.getBombCount();
        this.defaultOpenCount = o.getDefaultOpenCount();
    }

    private void init()
    {
        MyLog.log("Options.init()");
        xCount = 6;
        yCount = 5;
        bombCount = 5;
        defaultOpenCount = 10;
    }

    public void loadOptionsFromDB()
    {
        setDbName("Options");
        openDB();
        Options tempOptions = new Options();
        Vector v = getObjects(tempOptions);
        try
        {
            tempOptions = (Options) v.elementAt(0);
            if(tempOptions!=null) setValues(tempOptions);
        }
        catch(Exception e)
        {
        }
        finally
        {
            closeDB();
        }
    }

    public boolean saveOptionsToDB()
    {
        openDB();
        boolean result = false;
        if(getId() <= 0)
        {
            int newId = getNextRecordID();
            setId(newId);
            byte[] datas = encode();
            addRecord(datas);
            result = true;
        }
        else
        {
            byte[] datas = encode();
            result = setRecord(getId(), datas);
        }

        closeDB();
        return result;
    }

    public byte[] encode()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] result = new byte[0];
        try
        {
            dos.writeInt(dataVersion);
            dos.writeInt(id);
            dos.writeInt(xCount);
            dos.writeInt(yCount);
            dos.writeInt(bombCount);
            dos.writeInt(defaultOpenCount);
            result = baos.toByteArray();
        }
        catch(IOException e)
        {
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
        return result;
    }

    public Object decode(byte[] datas)
    {
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(datas);
            DataInputStream dis = new DataInputStream(bais);
            dataVersion = dis.readInt();
            id = dis.readInt();
            xCount = dis.readInt();
            yCount = dis.readInt();
            bombCount = dis.readInt();
            defaultOpenCount = dis.readInt();
            dis.close();
            bais.close();
            return this.clone();
        }
        catch(IOException e)
        {
             return null;
        }
    }

    public Object clone()
    {
        Options options = new Options();
        options.setXCount(getXCount());
        options.setYCount(getYCount());
        options.setBombCount(getBombCount());
        options.setDefaultOpenCount(getDefaultOpenCount());
        return options;
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

    public int getBombCount()
    {
        return bombCount;
    }

    public void setBombCount(int value)
    {
        bombCount = value;
    }

    public int getDefaultOpenCount()
    {
        return defaultOpenCount;
    }

    public void setDefaultOpenCount(int value)
    {
        defaultOpenCount = value;
    }

    public int getDataVersion()
    {
        return dataVersion;
    }

    public void setDataVersion(int value)
    {
        dataVersion = value;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int value)
    {
        id = value;
    }
}
