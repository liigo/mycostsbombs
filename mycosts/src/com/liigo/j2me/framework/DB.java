package com.liigo.j2me.framework;

import javax.microedition.rms.*;
import java.util.*;
import com.liigo.util.MyLog;

/**
 * 对数据库操作的一些封装
 * @author liigo
 */
/*
  我也很苦恼，象这样将Exception全部捕获之后改为逻辑值返回，实在非常"non-OO"!
  可是如果每次都写这样的try-catch的话，是不是又太多重复代码了？
  ----
  哈哈, 有什么苦恼的, 现在已经知道, Jave界也有很多人对try-catch很有意见,
  象我这样做是很平常的事情, 比如 Apache Commons.
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