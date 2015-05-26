package com.liigo.j2me.framework;

import java.io.IOException;

/**
 * 用于处理Object之序列化为byte[]，或从byte[]反序列化为Object。
 * @author liigo
 */
public interface Encodable
{

    /**
     * 将Object序列化为byte[]
     */
    public byte[] encode() throws IOException;

    /**
     * 将byte[]反序列化为Object
     */
    public Object decode(byte[] datas) throws IOException;

}