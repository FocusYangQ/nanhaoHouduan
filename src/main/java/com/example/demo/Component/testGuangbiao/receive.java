package com.example.demo.Component.testGuangbiao;

import java.io.InputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class receive {
    public String receive(Socket socket) throws Exception{

        InputStream inputStream=socket.getInputStream();//获取一个输入流，接收服务端的信息
        sleep(20);
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        int len;
        String res = "";

        System.out.print("接收返回数据：");

        try{
            for(int i=0;i<bytes.length;i++){
//                System.out.println(bytes[i]);
                if(bytes[i] == 0){
                    break;
                }
                res += (char)bytes[i];
                System.out.print((char)bytes[i]);
            }
            System.out.println();
//            if(test_res.equals("EN")){
//                System.out.println("接收返回值匹配成功");
//                return "EN";
//            } else {
//                System.out.println("接收返回值不匹配，失败");
//                return "false";
//            }
        } catch (Exception e) {
            System.out.println("无返回值");
        }
//        System.out.println();
//
//        System.out.println("无接收返回值");
//        inputStream.close();
//        inputStream.
//        System.out.println(res);
        return res;
    }
}
