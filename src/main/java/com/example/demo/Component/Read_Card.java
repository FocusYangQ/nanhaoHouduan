package com.example.demo.Component;

import com.example.demo.Entity.Score;
import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import jdk.internal.util.xml.impl.Input;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

public class Read_Card {

    @Autowired
    Std_ansService std_ansService;

    @Autowired
    Stu_id_nameService stu_id_nameService;

    public JSONArray read(Socket socket, InputStream inputStream, OutputStream outputStream) throws IOException, InterruptedException {

        System.out.println("==================开始本次读卡================");
        System.out.println();
        System.out.println("=====开始读卡=====");
        System.out.println("发送数据：R A/");
        outputStream.write("R A/".getBytes(StandardCharsets.UTF_8));
        sleep(200);


        //接收数据模块
//        System.out.print("接收数据:");
//        String res = "";
//        byte[] bytes = new byte[1024];
//        inputStream.read(bytes);
//        for(int i=0;i<bytes.length;i++) {
//            if(bytes[i] == 0) break;
//            res += (char)bytes[i];
//        }
//        System.out.println(res);


        //循环访问缓冲区
//        sleep(20);
        String res = "";
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            System.out.println((char)bytes[i]);
            res += (char)bytes[i];
        }
        int k=0;
        if(!res.equals("ENEN")){
            for(k=0;k<=250;k++){
                inputStream.read(bytes);
                for(int j=0;j<bytes.length;j++) {
                    if(bytes[j] == 0) break;
                    System.out.println((char)bytes[j]);
                    res += (char)bytes[j];
                }
                if(res.equals("ENEN")){
                    break;
                }
                sleep(20);
            }
        }



        JSONArray jsonArray = new JSONArray();
        Score score = new Score();


        if(k==251){
            score.setName("false");
            score.setScore(0);
            score.setAnswer("");
            score.setStu_id("");
            jsonArray.add(score);
            return jsonArray;
        }


        System.out.println("发送数据：r A 0001 2048/");
        sleep(200);
        outputStream.write("r A 0001 2048/".getBytes(StandardCharsets.UTF_8));
        //接收数据模块
        System.out.print("接收数据:");
        res = "";
        bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            res += (char)bytes[i];
        }
        System.out.println(res);


        int int_score = 0;
        String card_sum = res.substring(2,6);
        System.out.println("卡的数据量：" + card_sum);
        String card_sort = res.substring(6,7);
        System.out.println("卡的类型：" + card_sort);
        String stu_id = res.substring(12,16);
        System.out.println("该学生的学号：" + stu_id);
        String ans_tem = res.substring(17);
        System.out.println();
        String ans = ans_tem.substring(0, ans_tem.indexOf("."));
        System.out.println("答案：" + ans);
        String need_handle = ans;
        String last = "";

        System.out.println();
        System.out.println();
        System.out.println("=====开始处理数据=====");
        String std_ans = std_ansService.findStd_ans();
        System.out.println("标准答案：" + std_ans);
        System.out.println("该答题卡答案：" + need_handle);
        char[] std_ans_charArray = std_ans.toCharArray();
        char[] ans_charArray = need_handle.toCharArray();


        String name = "";
        for(int i=0;i<std_ans_charArray.length;i++){
            if(std_ans_charArray[i]==ans_charArray[i]){
                int_score++;
            }
        }
        System.out.println("该答题卡分数：" + int_score);
        score.setScore(int_score);
        score.setAnswer(need_handle);
        System.out.println("该学生的学号：" + stu_id);
        System.out.println(stu_id);
        score.setStu_id(stu_id);
        name = stu_id_nameService.find_name_by_id(stu_id);
        System.out.println("该学生的姓名：" + name);
        score.setName(name);
        jsonArray.add(score);

        System.out.println("传送的数据" + jsonArray);

        System.out.println();
        System.out.println("==================本次读卡结束================");
        System.out.println();
        System.out.println();

        return jsonArray;
    }
}
