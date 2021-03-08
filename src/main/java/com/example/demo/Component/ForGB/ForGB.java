package com.example.demo.Component.ForGB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ForGB {

    public String ForGB(String str) throws IOException {
                String SB=null;
                StringBuffer sb=new StringBuffer();
                String zifuchu=str;
                byte[] bytes=zifuchu.getBytes("GB2312");

                for(byte b : bytes){
                    SB=new String(Integer.toHexString(b)).substring(6, 8);
                    sb.append(SB);
                }
                System.out.println(sb);
                File file = new File("G:\\test.txt");
                Writer out = new FileWriter(file);
                System.out.println(sb);








                return sb.toString();
    }
}
