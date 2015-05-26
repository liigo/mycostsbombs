package com.liigo.j2me.framework;

import java.io.IOException;

/**
 * ���ڴ���Object֮���л�Ϊbyte[]�����byte[]�����л�ΪObject��
 * @author liigo
 */
public interface Encodable
{

    /**
     * ��Object���л�Ϊbyte[]
     */
    public byte[] encode() throws IOException;

    /**
     * ��byte[]�����л�ΪObject
     */
    public Object decode(byte[] datas) throws IOException;

}