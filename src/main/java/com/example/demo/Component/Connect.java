package com.example.demo.Component;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

public class Connect {
    public void connect() throws IOException, InterruptedException {

        Socket socket = new Socket("10.10.100.254",8899);
        String message = "ONLINE/";
        String[] messageStringArr = message.split("");
        for (int i = 0; i < messageStringArr.length; i++) {
            System.out.println(messageStringArr[i]);
            socket.getOutputStream().write(messageStringArr[i].getBytes(StandardCharsets.UTF_8));
            sleep(2);
        }
        sleep(1000);
        }
}
