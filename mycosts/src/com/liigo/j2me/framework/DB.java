package com.liigo.j2me.framework;

import javax.microedition.rms.*;
import java.util.*;
import com.liigo.util.MyLog;

/**
 * �����ݿ������һЩ��װ
 * @author liigo
 */
/*
  ��Ҳ�ܿ��գ���������Exceptionȫ������֮���Ϊ�߼�ֵ���أ�ʵ�ڷǳ�"non-OO"!
  �������ÿ�ζ�д������try-catch�Ļ����ǲ�����̫���ظ������ˣ�
  ----
  ����, ��ʲô���յ�, �����Ѿ�֪��, Jave��Ҳ�кܶ��˶�try-catch�������,
  �����������Ǻ�ƽ��������, ���� Apache Commons.
*/
public class DB
{
    RecordStore rs = null;
    String dbName;
    boolean isOpen = false;

    public DB()
    {
    }

    public DB(String dbName)
    {
        this.dbName = dbName;
    }

    public RecordStore getRecordStore()
    {
        return rs;
    }

    public String getDbName()
    {
        return dbName;
    }

    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public boolean openDB()
    {
        try
        {
            rs = RecordStore.openRecordStore(dbName, true);
            isOpen = true;
            return true;
        }
        catch(Exception e)
        {
            MyLog.error(this, "openDB() ", e);
            rs = null;
            return false;
        }
    }

    public boolean closeDB()
    {
        try
        {
            rs.closeRecordStore();
            isOpen = false;
            return true;
        }
        catch(Exception e)
        {
            MyLog.error(this, "closeDB()", e);
            return false;
        }
    }

    public boolean deleteDB()
    {
        try
        {
            rs.deleteRecordStore(dbName);
            return true;
        }
        catch(Exception e)
        {
            MyLog.error(this, "deleteDB()", e);
            return false;
        }
    }

    public static boolean deleteDB(String dbName)
    {
        try
        {
            RecordStore.deleteRecordStore(dbName);
            return true;
        }
        catch(Exception e)
        {
            MyLog.error(null, "deleteDB("+dbName+")", e);
            return false;
        }
    }

    public int addRecord(byte[] datas)
    {
        try
        {
            return rs.addRecord(datas, 0, datas.length);
        }
        catch(Exception e)
        {
            MyLog.error(this, "addRecord()", e);
            return -1; //?
        }
    }

    public boolean setRecord(int recordId, byte[] datas)
    {
        try
        {
            rs.setRecord(recordId, datas, 0, datas.length);
            return true;
        }
        catch(Exception e)
        {
            MyLog.error(this, "setRecord()", e);
            return false;
        }
    }

    public boolean deleteRecord(int recordId)
    {
        try
        {
            rs.deleteRecord(recordId);
            MyLog.log("deleteRecord: "+String.valueOf(recordId));
            return true;
        }
        catch(Exception e)
        {
            MyLog.error(this, "deleteRecord()", e);
            return false;
        }
    }

    public RecordEnumeration getRecordEnumeration()
    {
        return getRecordEnumeration(null, null, false);
    }

    public RecordEnumeration getRecordEnumeration ( RecordFilter fiter,
                                                    RecordComparator comparator,
                                                    boolean keepUpdate )
    {
        try
        {
            return rs.enumerateRecords(fiter, comparator, keepUpdate);
        }
        catch(Exception e)
        {
            MyLog.error(this, "getRecordEnumeration()", e);
            return null;
        }
    }

    public Vector getObjects(Encodable encodeObj)
    {
        return getObjects(getRecordEnumeration(), encodeObj);
    }

    public Vector getObjects(RecordEnumeration re, Encodable encodeObj)
    {
        Vector v = new Vector();
        if(re == null)
            return v;
        byte[] datas = null;
        while(re.hasNextElement())
        {
            try
            {
                datas = re.nextRecord();
            }
            catch(Exception e)
            {
                datas = null;
            }

            if(datas==null || datas.length==0)
                continue;
            try
            {
                v.addElement(encodeObj.decode(datas));
            }
            catch(Exception e)
            {
                MyLog.error(this, "getObjects()", e);
            }
        }
        return v;
    }

    public int getNextRecordID()
    {
        try
        {
            return rs.getNextRecordID();
        }
        catch(Exception e)
        {
            return 0;
        }

    }
    public String toString()
    {
        return this.getClass().getName() + " [" + getDbName() + "] ";
    }

}