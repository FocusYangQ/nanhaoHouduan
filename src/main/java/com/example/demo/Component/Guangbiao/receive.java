package com.example.demo.Component.Guangbiao;

import java.io.InputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class receive {
    public String receive(Socket socket, InputStream inputStream) throws Exception{

        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        int len;
        String res = "";

        System.out.print("接收返回数据：");

        try{
            for(int i=0;i<bytes.length;i++){
                if(bytes[i] == 0){
                    break;
                }
                res += (char)bytes[i];
                System.out.print((char)bytes[i]);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("无返回值");
        }
        return res;
    }
}
