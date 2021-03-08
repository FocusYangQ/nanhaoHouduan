package com.example.demo.Component.testGuangbiao;

import com.oracle.xmlns.internal.webservices.jaxws_databinding.SoapBindingParameterStyle;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

public class connect {
    public void connect(Socket socket) throws Exception {


        String message = "ONLINE/";
//以字符方式发送
//        String[] messageStringArr = message.split("");
//        for (int i = 0; i < messageStringArr.length; i++) {
//            System.out.print(messageStringArr[i]);
//            socket.getOutputStream().write(messageStringArr[i].getBytes(StandardCharsets.UTF_8));
//            sleep(2);
//        }

        send s = new send();
        receive r = new receive();

        s.send(socket,message);

        sleep(3000);
        String res = r.receive(socket);

        if(res.equals("ENEN")){
            System.out.println("接收返回值匹配成功");
        } else {
            System.out.println("接收返回值不匹配，失败");
        }

    }
}
