package com.example.demo.Component.Guangbiao;

import java.io.InputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class connect {
    public void connect(Socket socket, InputStream inputStream) throws Exception {


        String message = "ONLINE/";

        send s = new send();
        receive r = new receive();

        s.send(socket,message);

        sleep(800);
        String res = r.receive(socket,inputStream);

        if(res.equals("ENEN")){
            System.out.println("接收返回值匹配成功");
        } else {
            System.out.println("接收返回值不匹配，失败");
        }

    }
}
