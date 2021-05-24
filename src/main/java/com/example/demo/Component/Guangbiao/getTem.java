package com.example.demo.Component.Guangbiao;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class getTem {

    public String getTem(String filePath) throws Exception {
        File file = new File(filePath);
        String res = "";
        if (file.isFile() && file.exists()) {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String lineTxt = "";

            send s = new send();
            receive r = new receive();
            while ((lineTxt = br.readLine()) != null) {
                res += lineTxt + "\n";
            }
            br.close();
        }
        return res;
    }
}