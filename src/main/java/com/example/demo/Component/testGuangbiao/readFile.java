package com.example.demo.Component.testGuangbiao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class readFile {

        public String get_template(String filePath) throws Exception{

            try {
                File file = new File(filePath);
                if(file.isFile() && file.exists()) {
                    InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String lineTxt = null;
                    String lineResult = null;
                    while ((lineTxt = br.readLine()) != null) {
                        System.out.println(lineTxt);
                        lineResult += lineTxt;
                    }
                    br.close();
                    return lineTxt;
                } else {
                    System.out.println("文件不存在!");
                    return "文件不存在!";
                }
            } catch (Exception e) {
                System.out.println("文件读取错误!");
                return "文件读取错误!";
            } finally {
                return "";
            }

        }
}
