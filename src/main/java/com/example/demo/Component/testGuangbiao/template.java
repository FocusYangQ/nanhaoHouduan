package com.example.demo.Component.testGuangbiao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Array;

import static java.lang.Thread.sleep;

public class template {

    public static boolean template(Socket socket, String filePath) throws Exception {

        try {
            Socket thisSocket = socket;
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = "";
//                String forsend = "";
//                String Result = "";
//                while ((lineTxt = br.readLine()) != null) {
//                    Result += lineTxt + "\n";
//                    System.out.println(Result);
//                    char[] line = lineTxt.toCharArray();
//                    if(lineTxt == null){
//                        break;
//                    }
//                    for(int i = 0; i<lineTxt.length(); i++){
//                        if(line[i]!='/') {
//                            forsend += line[i];
//                        }else{
//                            break;
//                        }
//                    }
//                    forsend += '/';
//                    System.out.println(forsend);
//                    send s = new send();
//                    s.send(thisSocket,forsend);
//                    forsend = "";
//                    sleep(10);
//                }

                send s = new send();
                receive r = new receive();
                s.send(thisSocket,"S a.txt/");
                sleep(200);
                if(r.receive(thisSocket).equals("EN")){
                    while ((lineTxt = br.readLine()) != null){

//                        lineTxt = lineTxt.substring(0,lineTxt.indexOf("/")) + "/";
//                        s.send(thisSocket,lineTxt);
//                        sleep(200);
//                        if(lineTxt.equals("")){
//                            sleep(200);
//                            continue;
//                        }
//                        if(!r.receive(thisSocket).equals("EN")){
//                            System.out.println("返回值错误");
//                            break;
//                        }

                        if(lineTxt.equals("")){
                            sleep(5);
                            continue;
                        } else {
                            lineTxt = lineTxt.substring(0,lineTxt.indexOf("/")) + "/";
                            s.send(thisSocket,lineTxt);
                            sleep(5);
                            if(!r.receive(thisSocket).equals("EN")){
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
