package com.example.demo.Component.testGuangbiao;

import java.net.Socket;

import static java.lang.Thread.sleep;

public class read {
    public String read(Socket socket) throws Exception {
        String message = "R A/";

        send s = new send();
        receive r = new receive();

        s.send(socket,message);

        sleep(2000);

        r.receive(socket);

        message = "r A 0001 2048/";

        s.send(socket,message);

        sleep(2000);

        String res = r.receive(socket);
        System.out.println("卡的数据量：" + res.substring(3,6));
        System.out.println("卡的类型：" + res.substring(6,7));
        System.out.println("答案：" + res.substring(22));
        return res.substring(22);
    }
}
