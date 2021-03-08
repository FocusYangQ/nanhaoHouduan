package com.example.demo.Component.testGuangbiao;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

public class send {
    public void send(Socket socket,String info) throws Exception {
        try{
            String message = info;
            String[] messageStringArr = message.split("");
            for (int i = 0; i < messageStringArr.length; i++) {
                System.out.print(messageStringArr[i]);
                socket.getOutputStream().write(messageStringArr[i].getBytes(StandardCharsets.UTF_8));
                sleep(10);
            }
        } catch (Exception e){
            System.out.println();
            System.out.println("  发送失败");
        }
//        System.out.println();
        System.out.println("  发送成功");

//        receive r = new receive();
//        r.receive(socket);

    }
}
