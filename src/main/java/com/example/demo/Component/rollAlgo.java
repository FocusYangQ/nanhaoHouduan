package com.example.demo.Component;

import java.io.InputStream;

import static java.lang.Thread.sleep;

public class rollAlgo {

    public String rollAlgo(InputStream inputStream) throws InterruptedException {

        //最多轮询5s
        int flag = 0;
        String res = "";
        byte[] bytes = new byte[1024];
        for(int i = 0; i < 5000; i ++){
            try{
                inputStream.read(bytes);
            } catch (Exception e){
                System.out.println("读缓冲区失败");
            }

            for(int j = 0; j < bytes.length; j++){
                if(bytes[j] == 0) {
                    break;
                }
                res += (char)bytes[j];
            }
            if(!res.equals("EN")){
                System.out.println(res);
            }

            if(res.equals("EN16")){
                System.out.println("读卡失败，请检查阅读机中是否仍有答题卡");
                return "EN16";
            } else if(res.equals("ENEN")){
                return "ENEN";
            } else if(res.equals("EN05")){
                System.out.println("读卡失败，出现A传感器检测点线错");
                return "EN05";
            } else if(res.equals("EN09")){
                System.out.println("读卡失败，出现A传感器同步框计数值超界");
                return "EN09";
            }
            sleep(1);
            if(i == 249){
                flag = 1;
            }
        }
        return "exit";
    }
}
