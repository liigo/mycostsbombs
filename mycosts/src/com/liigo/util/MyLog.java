package com.liigo.util;

/**
 * 日志输出类
 * @author liigo
 *
 */

/*
   奇怪：
   如果定义成 log(Objcect o)，则WTK2.01编译时说找不到Float类。
   如果程序中用到了 "abc"+"123"，则WTK2.01编译时说找不到StringBiuld类。
   WTK2.2中没有发现以上问题。
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