package com.wei.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public  class StreamUtils {
    public static String getstream(InputStream inputStream) throws IOException
    {
    	ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();//输出流数组
    	byte[] buffer=new byte[1024];
    	int len=0;
    	while((len=inputStream.read(buffer))!=-1)
    	{
    		arrayOutputStream.write(buffer, 0, len);
    	}
    	inputStream.close();
    	arrayOutputStream.close();
    	
    	return arrayOutputStream.toString();//返回字符串
    }
}
