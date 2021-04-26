package com.example.demo.Controller;

import com.example.demo.Component.Guangbiao.*;
import com.example.demo.Entity.Score;
import com.example.demo.Entity.Std_ans;
import com.example.demo.Service.IpService;
import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.lang.Thread.sleep;

@CrossOrigin
@RestController
public class SocketController {

    @Autowired
    IpService ipService;

    @Autowired
    Std_ansService std_ansService;

    @Autowired
    Stu_id_nameService stu_id_nameService;

    int port = 8989;
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    BufferedInputStream bufferedInputStream;
    BufferedOutputStream bufferedOutputStream;

    @RequestMapping("/socket")
    public boolean connect() throws IOException, InterruptedException {

        String host = ipService.find_ip();
        System.out.println(host);
        socket = new Socket(host,port);
        inputStream=socket.getInputStream();
        bufferedInputStream = new BufferedInputStream(inputStream);
        outputStream=socket.getOutputStream();
        bufferedOutputStream = new BufferedOutputStream(outputStream);
        System.out.println("建立连接成功");

        return true;
    }

    @RequestMapping(value="/set_tem",method= RequestMethod.POST)
    @ResponseBody
    public boolean set_tem (@RequestParam Map map) throws Exception {

        System.out.println("===============连接测试===============");
        System.out.println("发送数据：ONLINE/");
        outputStream.write("ONLINE/".getBytes(StandardCharsets.UTF_8));
        sleep(900);
        System.out.print("接收数据:");
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            System.out.print((char)bytes[i]);
        }
        System.out.println();
        System.out.println();

        System.out.println("==============设置模板测试==============");
        //设置模板
        String s = (String) map.keySet().iterator().next();
        System.out.println(s);
        String s_for_use =s.substring(0, s.indexOf("型"));
        String file_path = "G:\\Form\\" + s_for_use + ".txt";
        System.out.println(file_path);
        readFile r = new readFile();
        r.get_template(file_path);

        System.out.println();
        System.out.println();

        System.out.println("==============开始发送模板==============");
        template tem = new template();
        tem.template(socket, file_path,inputStream);
        System.out.println("==============模板设置结束==============");

        System.out.println();
        System.out.println();

        return true;
    }

    @RequestMapping(value="/setTemAnother",method= RequestMethod.POST)
    @ResponseBody
    public boolean set_temAnother (@RequestParam Map map) throws Exception {

        System.out.println("===============退出===============");
        System.out.println("发送数据：QUIT/");
        outputStream.write("QUIT/".getBytes(StandardCharsets.UTF_8));
        sleep(900);
        System.out.print("接收数据:");
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            System.out.print((char)bytes[i]);
        }
        System.out.println();
        System.out.println();

        System.out.println("===============连接测试===============");
        System.out.println("发送数据：ONLINE/");
        outputStream.write("ONLINE/".getBytes(StandardCharsets.UTF_8));
        sleep(900);
        System.out.print("接收数据:");
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            System.out.print((char)bytes[i]);
        }
        System.out.println();
        System.out.println();

        System.out.println("==============设置模板测试==============");
        String s = (String) map.keySet().iterator().next();
        System.out.println(s);
        String s_for_use =s.substring(0, s.indexOf("型"));
        String file_path = "G:\\Form\\" + s_for_use + ".txt";
        System.out.println(file_path);
        readFile r = new readFile();
        r.get_template(file_path);

        System.out.println();
        System.out.println();

        System.out.println("==============开始发送模板==============");
        template tem = new template();
        tem.template(socket, file_path,inputStream);
        System.out.println("==============模板设置结束==============");

        System.out.println();
        System.out.println();
        return true;
    }

    @RequestMapping("/read_once")
    public String read_once() throws Exception {


        System.out.println("==============设置标准答案==============");
        System.out.println();
        outputStream.write("R A/".getBytes(StandardCharsets.UTF_8));
        sleep(900);


        //循环访问缓冲区
        String res = "";
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            res += (char)bytes[i];
        }
        System.out.println(res);

        outputStream.write("r A 0001 2048/".getBytes(StandardCharsets.UTF_8));
        sleep(200);
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


        System.out.println("卡的数据量：" + res.substring(3,6));
        System.out.println("卡的类型：" + res.substring(6,7));

        String need_first_handle = res.substring(17);
        String need_handle = need_first_handle.substring(0,need_first_handle.indexOf("..."));
        System.out.println(need_handle);

        //向数据库中写标准答案
        std_ansService.deleteall();
        Std_ans std_ans = new Std_ans();
        std_ans.setStd_ans(need_handle);
        std_ansService.save(std_ans);
        System.out.println("标准答案成功设置");

        String last = "";
        Score score = new Score();

        //标准答案呈现
        for(int i=0;i<need_handle.length()/10;i++){
            int j = i*10+10;
            int k=i*10;
            if(k==0) k=1;
            last = last + "<p>" +  k +"~" + j + "题：" +need_handle.substring(i*10,i*10+5) + "&nbsp&nbsp" + need_handle.substring(i*10+5,i*10+10) +"</p>";
            System.out.println(last);
        }

        System.out.println();
        System.out.println("==============标准答案设置结束==============");
        System.out.println();
        System.out.println();

        return last;

    }

    @RequestMapping("/another")
    public JSONArray another() throws Exception {

        System.out.println("==================开始本次读卡================");
        System.out.println();
        System.out.println("=====开始读卡=====");
        System.out.println("发送数据：R A/");
        outputStream.write("R A/".getBytes(StandardCharsets.UTF_8));
        sleep(200);

        String res = "";
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            res += (char)bytes[i];
        }
        int k=0;
        if(!res.equals("ENEN")){
            for(k=0;k<=250;k++){
                inputStream.read(bytes);
                for(int j=0;j<bytes.length;j++) {
                    if(bytes[j] == 0) break;
                    res += (char)bytes[j];
                }
                if(res.equals("ENEN")){
                   break;
                }
                sleep(20);
            }
        }
        System.out.print("接收数据:" + res);
        System.out.println("");

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


    @RequestMapping("/read_all")
    public JSONArray read_all() throws Exception {

        System.out.println("====================开始循环读卡==================");
        System.out.println();
        System.out.println("==================开始本次读卡================");
        System.out.println();
        System.out.println("=====开始读卡=====");
        System.out.println("发送数据：R A/");
        outputStream.write("R A/".getBytes(StandardCharsets.UTF_8));
        sleep(200);

        //接收数据
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
        System.out.println("====================开始循环读卡==================");
        System.out.println();
        System.out.println();

        return jsonArray;
    }

}