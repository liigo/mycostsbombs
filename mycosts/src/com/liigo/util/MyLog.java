package com.liigo.util;

/**
 * ��־�����
 * @author liigo
 *
 */

/*
   ��֣�
   �������� log(Objcect o)����WTK2.01����ʱ˵�Ҳ���Float�ࡣ
   ����������õ��� "abc"+"123"����WTK2.01����ʱ˵�Ҳ���StringBiuld�ࡣ
   WTK2.2��û�з����������⡣
   liigo, 2005-9-18 ->
*/
public class MyLog
{
    public MyLog()
    {
    }

    public static void log(String msg)
    {
        System.out.println(msg);
    }

    public static void log(Object obj)
    {
        log(obj==null?"[null]":obj.toString());
    }

    public static void log(int i)
    {
        log(String.valueOf(i));
    }

    public static void log(long l)
    {
        log(String.valueOf(l));
    }

    public static void log(Object obj1, Object obj2)
    {
        log_thisline(obj1);
        log(obj2);
    }

    public static void log_thisline(Object o)
    {
        System.out.print(o==null?"[null]":o.toString() + " ");
    }
    public static void log_(Object o)
    {
        log_thisline(o);
    }

    public static void error(Object o)
    {
        log("[error]", o);
    }

    public static void error_thisline(Object o)
    {
        log_thisline("[error] " + o.toString() + " ");
    }
    public static void error_(Object o)
    {
        error_thisline(o);
    }

    public static void error(Object o1, Object o2)
    {
        log_thisline("[error] ");
        log(o1, o2);
    }

    public static void error(Object o, String msg, Exception e)
    {
        error_(o.toString() + " | "); log(msg);
        error(e.getMessage());
    }
}