package com.example.demo.Controller;

import com.example.demo.Component.Guangbiao.*;
import com.example.demo.Entity.Score;
import com.example.demo.Entity.Std_ans;
import com.example.demo.Service.IpService;
import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
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
    String host;
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    BufferedInputStream bufferedInputStream;
    BufferedOutputStream bufferedOutputStream;
    String CardType = "";

    //构建整个答题流程的Socket
    @RequestMapping("/socket")
    public boolean connect() {

        host = ipService.find_ip();
        System.out.println("当前IP地址是：" + host);
        try{
            socket = new Socket(host,port);
        } catch (Exception e){
            System.out.println("建立socket失败");
            return false;
        }
        try{
            inputStream=socket.getInputStream();
            bufferedInputStream = new BufferedInputStream(inputStream);
            outputStream=socket.getOutputStream();
            bufferedOutputStream = new BufferedOutputStream(outputStream);
        } catch (IOException e){
            System.out.println("建立Socket输入输出流及缓冲区失败");
            return false;
        }
        System.out.println("建立连接成功");

        return true;
    }

    @RequestMapping("/online")
    public boolean online() throws InterruptedException {

        System.out.println("===============连接测试===============");
        try{
            outputStream.write("ONLINE/".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e){
            System.out.println("发送ONLINE失败");
            return false;
        }
        System.out.println("发送数据：ONLINE/成功");
        byte[] bytes = new byte[1024];
        String res = "";

        //循环读取接收缓冲区start
        //最多轮询5s
        int flag = 0;
        for(int i = 0; i < 5000; i ++){
            try{
                inputStream.read(bytes);
            } catch (Exception e){
                System.out.println("读取缓冲区失败");
            }
            for(int j = 0; j < bytes.length; j++){
                if(bytes[j] == 0) {
                    break;
                }
                res += (char)bytes[j];
            }
            if(res.equals("ENEN")){
                System.out.println("接收返回值ENEN成功");
                break;
            }
            sleep(1);
            if(i == 249){
                flag = 1;
            }
        }
        //循环读取接收缓冲区end

        return true;
    }

    //设置模板
    @RequestMapping(value="/set_tem",method= RequestMethod.POST)
    @ResponseBody
    public String set_tem (@RequestParam Map map) throws Exception {

        System.out.println("==============设置模板测试==============");
        //设置模板
        String s = (String) map.keySet().iterator().next();
        System.out.println(s);
        String s_for_use =s.substring(0, s.indexOf("型"));
        CardType = s_for_use;
        System.out.println("CardType:" + CardType);
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

        return "true";
    }

    //设置模板失败后，重新设置模板指令
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

        //循环读取缓冲区字符start
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            System.out.print((char)bytes[i]);
        }
        //循环读取缓冲区字符end

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

    //设置标准答案读卡1次
    @RequestMapping("/read_once")
    public String read_once() throws Exception {
        System.out.println("==============设置标准答案==============");
        System.out.println();
        System.out.println("发送数据：R A/");
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
        System.out.println("接收返回数据：" + res);

        //读卡异常错误处理start
        if(res.equals("ENEN16") || res.equals("EN16")){
            System.out.println("无卡error");
            return "noCard";
        }
        if(res.equals("ENEN09") || res.equals("EN09")){
            System.out.println("出现A传感器同步框计数值超界");
            return "出现A传感器同步框计数值超界";
        }
        if(res.equals("ENEN07")){
            System.out.println("出现A传感器卡纸（同步信号）错");
            return "出现A传感器卡纸（同步信号）错";
        }
        //读卡异常错误处理end

        //读光标阅读季缓冲区start
        System.out.println("发送数据：r A 0001 2048/");
        outputStream.write("r A 0001 2048/".getBytes(StandardCharsets.UTF_8));
        //读光标阅读季缓冲区end
        sleep(200);

        //接收数据start
        System.out.print("接收数据:");
        res = "";
        bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == 0) break;
            res += (char)bytes[i];
        }
        System.out.println(res);
        //接收数据end

        //数据处理开始
        String ans = "";
        String stu_id = "";
        System.out.println("CardType:" + CardType);
        switch (CardType) {
            case "40T":
                System.out.println("执行case40T");
                try{
                    System.out.println("卡的数据量：" + res.substring(2,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    System.out.println("学生的学号：" + res.substring(6,15));
                } catch (Exception e){
                    System.out.println("出现读学号异常");
                }
                try{
                    System.out.println(res.substring(16,63));
                    ans = res.substring(16,61);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("出现读卡的异常");
                }
                break;
            case "43lu":
                System.out.println("执行case43lu");
                try{
                    System.out.println("卡的数据量：" + res.substring(2,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    System.out.println("学生的学号：" + res.substring(6,15));
                } catch (Exception e){
                    System.out.println("出现读学号异常");
                }
                try{
                    System.out.println(res.substring(16,63));
                    ans = res.substring(18,61);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("出现读卡的异常");
                }
                break;
            case "85T":
                System.out.println("执行case85T");
                try{
                    System.out.println("卡的数据量：" + res.substring(2,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    System.out.println("学生的学号：" + res.substring(7,16));
                } catch (Exception e){
                    System.out.println("出现读学号异常");
                }
                try{
                    ans = res.substring(17,res.indexOf("EN",3));
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("出现读卡的异常");
                }
                break;
            case "105T":
                System.out.println("执行case105T");
                try{
                    System.out.println("卡的数据量：" + res.substring(2,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    System.out.println("学生的学号：" + res.substring(7,22));
                } catch (Exception e){
                    System.out.println("出现读学号异常");
                }

                try{
                    ans = res.substring(22,127);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("出现读卡的异常");
                }
                break;
        }
        //数据处理结束

        //向数据库中写标准答案start
        std_ansService.deleteall();
        Std_ans std_ans = new Std_ans();
        std_ans.setStd_ans(ans);
        std_ansService.save(std_ans);
        System.out.println("标准答案写入数据库成功");
        //向数据库中写标准答案end

        String last = "";
        Score score = new Score();

        //标准答案呈现start
        System.out.println(ans.length());
        try{
            switch (ans.length()/10){
                case 4:
                    System.out.println("执行switch语句中case4");
                    for(int i = 0; i <= 4; i ++) {
                        int j = i * 10 + 10;
                        int k = i * 10;
                        if (k == 0) k = 1;
                        if(i == 4 ){
                            last = last + "<p>" + k + "~" + (k + 5) + "题：" + ans.substring(i * 10, i * 10 + 5) + "</p>";
                        } else {
                            last = last + "<p>" + k + "~" + j + "题：" + ans.substring(i * 10, i * 10 + 5) + "&nbsp&nbsp" + ans.substring(i * 10 + 5, i * 10 + 10) + "</p>";
                        }
                        System.out.println(last);
                    }
                    break;
                case 8:
                    System.out.println("执行switch语句中case8");
                    for(int i = 0; i <= 8; i ++) {
                        int j = i * 10 + 10;
                        int k = i * 10;
                        if (k == 0) k = 1;
                        if(i == 8 ){
                            last = last + "<p>" + k + "~" + (k + 5) + "题：" + ans.substring(i * 10, i * 10 + 5) + "</p>";
                        } else {
                            last = last + "<p>" + k + "~" + j + "题：" + ans.substring(i * 10, i * 10 + 5) + "&nbsp&nbsp" + ans.substring(i * 10 + 5, i * 10 + 10) + "</p>";
                        }
                        System.out.println(last);
                    }
                    break;
                case 10:
                    System.out.println("执行switch语句中case10");
                    for(int i = 0; i <= 11; i ++) {
                        int j = i * 10 + 10;
                        int k = i * 10;
                        if (k == 0) k = 1;
                        if(i == 11 ){
                            last = last + "<p>" + k + "~" + (k + 5) + "题：" + ans.substring(i * 10, i * 10 + 5) + "</p>";
                        } else {
                            last = last + "<p>" + k + "~" + j + "题：" + ans.substring(i * 10, i * 10 + 5) + "&nbsp&nbsp" + ans.substring(i * 10 + 5, i * 10 + 10) + "</p>";
                        }
                        System.out.println(last);
                    }
                    break;
            }
        } catch (Exception e){
            System.out.println("设置答案出错");
        }
        //标准答案呈现end

        System.out.println();
        System.out.println("==============标准答案设置结束==============");
        System.out.println();
        System.out.println();

        return last;

    }

    //循环读卡
    @RequestMapping("/another")
    public JSONArray another() throws Exception {

        JSONArray jsonArray = new JSONArray();
        Score score = new Score();
        String res = "";
        byte[] bytes = new byte[1024];

//        System.out.println("==================清空缓冲区开始================");
        socket.setSoTimeout(5000);
//        for(int i = 0; i < 3; i++){
//            try{
//                inputStream.read(bytes);
//            } catch (Exception e){
//                System.out.println("第" + i + "次尝试读取缓冲区");
//            }
//        }
//        System.out.println("==================清空缓冲区结束================");

        System.out.println("==================开始本次读卡================");
        System.out.println();
        System.out.println("=====开始读卡=====");
        System.out.println("发送数据：R A/");
        outputStream.write("R A/".getBytes(StandardCharsets.UTF_8));
        sleep(1);
        System.out.println("接收数据：");

        //循环读取接收缓冲区start
        //最多轮询5s
        int flag = 0;
        for(int i = 0; i < 5000; i ++){
            inputStream.read(bytes);
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
                score.setName("EN16");
                score.setScore(0);
                score.setAnswer("");
                score.setStu_id("");
                jsonArray.add(score);
                return jsonArray;
            } else if(res.equals("ENEN")){
                break;
            } else if(res.equals("EN05")){
                System.out.println("读卡失败，出现A传感器检测点线错");
                score.setName("EN05");
                score.setScore(0);
                score.setAnswer("");
                score.setStu_id("");
                jsonArray.add(score);
                return jsonArray;
            } else if(res.equals("EN09")){
                System.out.println("读卡失败，出现A传感器同步框计数值超界");
                score.setName("EN09");
                score.setScore(0);
                score.setAnswer("");
                score.setStu_id("");
                jsonArray.add(score);
                return jsonArray;
            }
            sleep(1);
            if(i == 249){
                flag = 1;
            }
        }

        System.out.println("发送数据：r A 0001 2048/");
        sleep(1);
        outputStream.write("r A 0001 2048/".getBytes(StandardCharsets.UTF_8));

        //接收数据模块
        System.out.print("接收数据:");
        res = "";
        bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i = 0; i < bytes.length; i ++) {
            if(bytes[i] == 0) break;
            res += (char)bytes[i];
        }
        System.out.println(res);    //输出所有接收到的数据

//try catch 模板
//        try{
//
//        } catch (Exception e){
//            System.out.println("截取卡的数据量error");
//        }

//        String card_sum = "";
//        try{
//            //读取卡的数据量start
//            card_sum = res.substring(2,6);
//            System.out.println("卡的数据量：" + card_sum);
//            //读取卡的数据量end
//        } catch (Exception e){
//            System.out.println("截取卡的数据量error");
//        }
//
//        String card_sort = "";
//        try{
//            //读取卡的类型start
//            card_sort = res.substring(6,7);
//            System.out.println("卡的类型：" + card_sort);
//            //读取卡的类型end
//        } catch (Exception e){
//            System.out.println("截取卡的类型error");
//        }
//
//        String stu_id = "";
//        try{
//            //读取学生的学号start
//            stu_id = res.substring(12,16);
//            System.out.println("该学生的学号：" + stu_id);
//            //读取学生的学号end
//        } catch (Exception e){
//            System.out.println("截取学生的学号error");
//        }
//
//        String ans = "";
//        try{
//            //截取学生答案start
//            String ans_tem = res.substring(17);
//            System.out.println();
//            ans = ans_tem.substring(0, ans_tem.indexOf("."));
//            System.out.println("答案：" + ans);
//            //截取学生答案end
//        } catch (Exception e){
//            System.out.println("截取卡的答案error");
//        }
//
//
//        String need_handle = ans;
//        String last = "";


        System.out.println();
        System.out.println();

        String stuId = "";
        String ans = "";
        System.out.println(CardType);
        switch (CardType){
            case "40T":
                try{
                    System.out.println("卡的数据量：" + res.substring(2,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    stuId = res.substring(7,16);
                    System.out.println("学号：" + stuId);
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    ans = res.substring(16,61);
                    System.out.println("答案：" + ans);
                } catch (Exception e){
                    System.out.println("读取答案异常");
                }
                break;
            case "43lu":
                try{
                    System.out.println("卡的数据量：" + res.substring(3,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    stuId = res.substring(7,17);
                    System.out.println("学号：" + stuId);
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    ans = res.substring(22,127);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("读取答案异常");
                }
                break;
            case "50lu":
                try{
                    System.out.println("卡的数据量：" + res.substring(3,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    stuId = res.substring(7,17);
                    System.out.println("学号：" + stuId);
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    ans = res.substring(22,127);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("读取答案异常");
                }
                break;
            case "50T":
                try{
                    System.out.println("卡的数据量：" + res.substring(3,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    stuId = res.substring(7,17);
                    System.out.println("学号：" + stuId);
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    ans = res.substring(22,127);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("读取答案异常");
                }
                break;
            case "85T":
                try{
                    System.out.println("卡的数据量：" + res.substring(3,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    stuId = res.substring(7,16);
                    System.out.println("学号：" + stuId);
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    ans = res.substring(17,res.indexOf("EN",3));
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("读取答案异常");
                }
                break;
            case "90T":
                try{
                    System.out.println("卡的数据量：" + res.substring(3,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    stuId = res.substring(7,17);
                    System.out.println("学号：" + stuId);
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    ans = res.substring(22,127);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("读取答案异常");
                }
                break;
            case "105T":
                //数据处理开始
                try{
                    System.out.println("卡的数据量：" + res.substring(2,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println("卡的类型：" + res.substring(6,7));
                } catch (Exception e){
                    System.out.println("出现读卡类型异常");
                }
                try{
                    stuId = res.substring(7,17);
                    System.out.println("学生的学号：" + stuId);
                } catch (Exception e){
                    System.out.println("出现读学号异常");
                }
                try{
                    ans = res.substring(22,127);
                    System.out.println("卡的选项：" + ans);
                } catch (Exception e){
                    System.out.println("出现读卡的异常");
                }
                //数据处理结束
                break;
        }
        
        System.out.println("=====开始处理数据=====");
        String std_ans = std_ansService.findStd_ans();
        System.out.println("标准答案：" + std_ans);
        System.out.println("该答题卡答案：" + ans);
        char[] std_ans_charArray = std_ans.toCharArray();
        char[] ans_charArray = ans.toCharArray();

        String name = "";
        int int_score = 0;
        for(int i=0;i<std_ans_charArray.length;i++){
            if(std_ans_charArray[i]==ans_charArray[i]){
                int_score++;
            }
        }
        System.out.println("该答题卡分数：" + int_score);
        score.setScore(int_score);
        score.setAnswer(ans);
        System.out.println("该学生的学号：" + stuId);
        System.out.println(stuId);
        score.setStu_id(stuId);
        name = stu_id_nameService.find_name_by_id(stuId);
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


    @RequestMapping("/clear")
    public boolean clearBuf() throws SocketException {
        System.out.println("==================清空缓冲区开始================");
        socket.setSoTimeout(5000);
        byte[] bytes = new byte[1024];
        for(int i = 0; i < 3; i++){
            try{
                inputStream.read(bytes);
            } catch (Exception e){
                System.out.println("第" + i + "次尝试读取缓冲区");
            }
        }
        System.out.println("==================清空缓冲区结束================");
        return true;
    }
}
