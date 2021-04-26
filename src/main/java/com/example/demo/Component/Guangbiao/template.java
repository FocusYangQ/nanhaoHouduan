package com.example.demo.Component.Guangbiao;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class template {

    public static boolean template(Socket socket, String filePath, InputStream inputStream) throws Exception {

        try {
            Socket thisSocket = socket;
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = "";

                send s = new send();
                receive r = new receive();
                s.send(thisSocket,"S a.txt/");
                sleep(200);
                if(r.receive(thisSocket,inputStream).equals("EN")){
                    while ((lineTxt = br.readLine()) != null){

                        if(lineTxt.equals("")){
                            sleep(5);
                            continue;
                        } else {
                            lineTxt = lineTxt.substring(0,lineTxt.indexOf("/")) + "/";
                            s.send(thisSocket,lineTxt);
                            sleep(5);
                            if(!r.receive(thisSocket,inputStream).equals("EN")){
                            System.out.println("返回值错误");
                            break;
                            }
                        }
                    }
                }
                else {
                    System.out.println("S a.txt/ 无返回值或返回值错误");
                }
                br.close();
                s.send(thisSocket,"EN/");
                return true;
//                System.out.println(lineTxt);
//                return Result + "EN/";
            } else {
                System.out.println("文件不存在!");
//                return "false";
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }
        return false;
    }
}
