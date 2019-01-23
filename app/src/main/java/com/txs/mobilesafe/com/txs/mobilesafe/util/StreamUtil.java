package com.txs.mobilesafe.com.txs.mobilesafe.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

public class StreamUtil {

    public static String tostring(InputStream inputStream) throws IOException {
        //拿到的inputStream
        //将读取的内容 储存到 缓存中，然后一次性转换成字符串返回
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int flag = -1;
        byte[] bytes = new byte[1024];
        try {
            while ((flag=inputStream.read(bytes))!=-1){
                byteArrayOutputStream.write(bytes,0,flag);
            }
            return byteArrayOutputStream.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            inputStream.close();
            byteArrayOutputStream.close();
        }
        return null;
    }
}
