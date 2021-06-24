package com.example.demo.Controller;

import com.example.demo.Component.Guangbiao.*;
import com.example.demo.Component.rollAlgo;
import com.example.demo.Entity.Rank;
import com.example.demo.Entity.Score;
import com.example.demo.Entity.Std_ans;
import com.example.demo.Service.IpService;
import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import com.example.demo.Service.rankService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    rankService rankService ;

    int port = 8989;
    String host;
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    BufferedInputStream bufferedInputStream;
    BufferedOutputStream bufferedOutputStream;
    String CardType = "";
    double scoreRecord [ ] = new double [ 110 ] ;
    char ans [ ] = new char [ 110 ] ;
    int corNum [ ] = new int [ 110 ] ;
    double scoreSum = 0 ;
    int perNum = 0 ;
    int sum = 0 ;
    String std_ans = "" ;
    int corRateInt [ ] = new int [ 110 ] ;
    double corRateDou [ ] = new double[ 110 ] ;

    //构建整个答题流程的Socket测试
    @RequestMapping("/socket")
    public boolean connect() {



        for ( int i = 0 ; i < 110 ; i ++ ) {

            scoreRecord [ i ] = 0 ;
            corRateInt [ i ] = 0 ;
            corRateDou [ i ] = 0 ;

        }



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
        byte[] bytes = new byte [ 1024 ] ;
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
        String file_path = "G:\\Form\\Tem\\" + s_for_use + ".txt";
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
        String file_path = "G:\\Form\\Tem\\" + s_for_use + ".txt";
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

    @RequestMapping("/readTem")

    public String readTem(@RequestParam Map map){

        String res = "";
        String s = (String) map.keySet().iterator().next();
        System.out.println(s);
        String s_for_use =s.substring(0, s.indexOf("型"));
        String file_path = "G:\\Form\\Tem\\" + s_for_use + ".txt" ;
        System.out.println(file_path);
        getTem r = new getTem();
        try{
            res = r.getTem(file_path);
        } catch (Exception e){
            System.out.println("读取模板失败");
        }
        System.out.println(res);

        return res;
    }


    //设置标准答案读卡1次
    @RequestMapping("/read_once")
    public String read_once() throws Exception {



        System.out.println("==============设置标准答案==============");
        System.out.println();
        System.out.println("发送数据：R A/");
        outputStream.write("R A/".getBytes(StandardCharsets.UTF_8));



        //循环访问缓冲区
        String res = "";
        byte[] bytes = new byte[1024];



        for( int i = 0 ; i < 5000; i ++) {

            try {

                inputStream.read(bytes);

            } catch (Exception e) {

                System.out.println("读缓冲区失败");

            }

            for (int j = 0; j < bytes.length; j++) {

                if (bytes[j] == 0) {
                    break;
                }
                res += (char) bytes[j];

            }

            if ( res.equals("ENEN") ) {

                System.out.println( "接收返回数据:  ENEN" );
                break ;

            } else if ( res.equals( "EN16" ) ) {

                    return "noCard" ;

            }

            if ( i == 4999 ) {

                System.out.println( "接收返回数据错误" );

            }

            sleep( 1 );

        }




        System.out.println("发送数据：r A 0001 2048/");
        sleep(1);
        outputStream.write("r A 0001 2048/".getBytes(StandardCharsets.UTF_8));



        //接收数据模块
        res = "";
        bytes = new byte [ 1024 ] ;




        //接收数据模块
        System.out.print("接收到的数据:  ");
        res = "";
        bytes = new byte [ 1024 ] ;
        inputStream.read(bytes);
        for(int i = 0; i < bytes.length; i ++) {
            if(bytes[i] == 0) break;
            res += (char)bytes[i];
        }
        System.out.println(res);    //输出所有接收到的数据




        System.out.println();
        System.out.println();




        System.out.println( res );    //输出所有接收到的数据



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
            case "50T":
                System.out.println("执行case50T");
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
                    System.out.println("学生的学号：" + res.substring(7,15));
                } catch (Exception e){
                    System.out.println("出现读学号异常");
                }
                try{
                    System.out.println(res.substring(17,67));
                    ans = res.substring(17,67);
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

        System.out.println( "答案的长度是：  " + ans.length( ) );
        try{
            switch (ans.length()/10){
                case 4:
                    System.out.println("执行switch语句中case4");
                    for(int i = 0; i <= 4; i ++) {
                        int j = i * 10 + 10;
                        int k = i * 10;
                        if (k == 0) k = 1;
                        last = last  + k + "~" + j + "题：" + ans.substring(i * 10, i * 10 + 5) + "  " + ans.substring(i * 10 + 5, i * 10 + 10) + "\n" ;
                    }
                    break;

                case 5:
                    System.out.println("执行switch语句中case5");
                    for(int i = 0; i <= 4; i ++) {
                        int j = i * 10 + 10;
                        int k = i * 10;
                        if (k == 0) k = 1;
                        if(i == 5 ){
                            last = last +  k + "~" + (k + 5) + "题： " + ans.substring(i * 10, i * 10 + 5) + "\n" ;
                        } else {
                            last = last  + k + "~" + j + "题：" + ans.substring(i * 10, i * 10 + 5) + "  " + ans.substring(i * 10 + 5, i * 10 + 10) + "\n";
                        }
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
                    }
                    break;
            }
        } catch (Exception e){
            System.out.println("设置答案出错");
        }


        System.out.println();
        System.out.println();
        System.out.println(last);
        System.out.println();
        System.out.println();
        System.out.println();



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




        System.out.println("==================开始本次读卡================");
        System.out.println();
        System.out.println("=====开始读卡=====");
        System.out.println("发送数据：R A/");
        outputStream.write("R A/".getBytes(StandardCharsets.UTF_8));
        sleep(1);
        System.out.println("接收数据：");




        rollAlgo rollAlgo = new rollAlgo();
        //轮询数据缓冲区
        String resRollAlgo = rollAlgo.rollAlgo(inputStream);



        if ( resRollAlgo.equals( "EN16" ) ) {

            score.setName( resRollAlgo ) ;
            jsonArray.add( score ) ;
            return jsonArray ;

        } else if ( resRollAlgo.equals( "EN07" ) ) {

            score.setName( resRollAlgo ) ;
            jsonArray.add( score ) ;
            return jsonArray ;

        } else if ( resRollAlgo.equals( "EN09" ) ) {

            score.setName( resRollAlgo ) ;
            jsonArray.add( score ) ;
            return jsonArray ;

        }




        System.out.println("发送数据：r A 0001 2048/");
        sleep(1);
        outputStream.write("r A 0001 2048/".getBytes(StandardCharsets.UTF_8));




        //接收数据模块
        System.out.print("接收到的数据:  ");
        res = "";
        bytes = new byte[1024];
        inputStream.read(bytes);
        for(int i = 0; i < bytes.length; i ++) {
            if(bytes[i] == 0) break;
            res += (char)bytes[i];
        }
        System.out.println(res);    //输出所有接收到的数据



        System.out.println();
        System.out.println();




        String stuId = "";
        String ans = "";
        System.out.println( CardType );




        switch ( CardType ) {

            case "40T" :
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

            case "43lu" :

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

            case "50lu" :
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

            case "50T" :

                try{
                    System.out.println("卡的数据量：" + res.substring(3,6));
                } catch (Exception e){
                    System.out.println("出现读卡的数量异常");
                }
                try{
                    System.out.println( " 卡的类型 ： " + res.substring(6,7));
                } catch (Exception e){
                    System.out.println( " 出现读卡类型异常 " ) ;
                }
                try{
                    stuId = res.substring ( 7 , 17 ) ;
                    System.out.println( " 学号：" + stuId ) ;
                } catch ( Exception e ) {
                    System.out.println( " 出现读卡类型异常 " ) ;
                }
                try{
                    ans = res.substring( 17 , res.indexOf ("EN" ,3 ) ) ;
                    System.out.println( " 卡的选项： " + ans ) ;
                } catch (Exception e) {
                    System.out.println( " 读取答案异常 " ) ;
                }
                break ;

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
                    ans = res.substring( 17 , res.indexOf("EN",3));
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


        System.out.println( ) ;
        System.out.println( ) ;
        System.out.println( ) ;



        System.out.println("=====开始处理数据=====");



        System.out.println();
        System.out.println();
        System.out.println();



        if ( std_ans.equals( "" ) ) {

            std_ans = std_ansService.findStd_ans();

        }



        System.out.println("标准答案：" + std_ans);
        System.out.println("该答题卡答案：" + ans);
        char[] std_ans_charArray = std_ans.toCharArray();
        char[] ans_charArray = ans.toCharArray();

        String name = "";
        double double_score = 0;
        for( int i = 0 ; i < std_ans_charArray.length ; i ++ ) {

            if ( std_ans_charArray[ i ] == ans_charArray [ i ] && ans_charArray [ i ] != '.' ) {

                corRateInt [ i ] ++ ;
                double_score += scoreRecord [ i + 1 ];

            }

        }
        double_score = new BigDecimal(double_score).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() ;
        System.out.println("该答题卡分数：" + double_score);

        scoreSum += double_score ;



        System.out.println( "scoreSum:  " + scoreSum );





        score.setScore ( double_score );
        score.setAnswer(ans);
        System.out.println("该学生的学号：" + stuId);
        System.out.println(stuId);
        score.setStu_id(stuId);
        name = stu_id_nameService.find_name_by_id( stuId ) ;
        System.out.println("该学生的姓名：" + name);
        score.setName(name);
        jsonArray.add(score);




        //需要进行排序数据
        Rank rank = new Rank( ) ;
        rank.setName ( name ) ;
        rank.setStuId ( stuId ) ;
        rank.setScore (double_score) ;
        rank.setAns ( ans ) ;
        rankService.saveRank ( rank ) ;




        System.out.println("传送的数据" + jsonArray);




        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("==================本次读卡结束================");
        System.out.println();
        System.out.println();


        return jsonArray;


    }



    @RequestMapping("/setStdAns")
    @ResponseBody

    public boolean setStdAns( @RequestParam Map<Object,Object> map ) {



        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println( "全部数据:" );
        System.out.println();
        System.out.println(map);
        System.out.println();
        System.out.println();
        System.out.println();



        String str = (String) map.keySet().iterator().next();
        System.out.println( "第一部分数据:");
        System.out.println();
        System.out.println( str );
        System.out.println();
        System.out.println();
        System.out.println();



        String ansOri = str.substring( 0 , str.indexOf( "---" ) );
        String scoreOri = str.substring( str.indexOf( "---" ) + 3 );

        String ansRemainPart = ansOri ;
        String ansUsePart = " " ;



        System.out.println( "答案:");
        System.out.println();
        System.out.println( ansOri );
        System.out.println();
        System.out.println();
        System.out.println();


        System.out.println( "分数:");
        System.out.println( scoreOri );
        System.out.println();
        System.out.println();
        System.out.println();



        int j = 0 ;

        for ( int i = 0 ; i < 5 ; i ++ ) {

            try {

                ansRemainPart = ansRemainPart.substring( ansRemainPart.indexOf("题") + 2 );

            } catch ( Exception e ) {

                System.out.println( " 截取剩余部分失败 " );

            }


            if ( i != 4 ) {

                try {

                    ansUsePart = ansRemainPart.substring( 0 , ansRemainPart.indexOf("题") - 5 );

                } catch ( Exception e ) {

                    System.out.println( " 截取使用部分失败 " );

                }

            } else {

                ansUsePart = ansRemainPart ;

            }




            System.out.println();
            System.out.println();
            System.out.println();



            char [ ] arr = ansUsePart.toCharArray( ) ;

            for ( int k = 0 ; k < ansUsePart.length() ; k ++ ) {
                ans [ j ++ ] = arr [ k ] ;
            }

        }

        for ( int k = 0 ; k < 110 ; k ++ ) {
            System.out.print( ans [ k ] );
        }

        System.out.println("答案设置成功");
        System.out.println();
        System.out.println();
        System.out.println();




        String remainStr = scoreOri ;
        String testTimes = scoreOri ;

        int times = 0 ;
        for ( int i = 0 ; i <= 20 ; i ++ ) {

            try {

                if ( testTimes.indexOf( "~" ) == -1 ) {

                    break ;

                }
                testTimes = testTimes.substring( testTimes.indexOf( "~" ) + 1 ) ;
                System.out.println( testTimes.indexOf( "~" ) );
                System.out.println( testTimes );
                System.out.println( times );
                times ++ ;

            } catch ( Exception e ) {
                break ;
            }

        }


        System.out.println( "开始执行循环" );
        System.out.println("======================================================================");

        String useToScore = "" ;

        for ( int i = 1 ; i <= times ; i ++ ) {
            useToScore = remainStr.substring( 0 , remainStr.indexOf( "分" ) + 1 ) ;
            remainStr = remainStr.substring( remainStr.indexOf( "分" ) + 1 ) ;
            String useToNum = useToScore.substring( 0 , useToScore.indexOf( "题" ) ) ;
            String firstStr = useToNum.substring( 0 , useToNum.indexOf( "~" ) ) ;
            firstStr = firstStr.replace( '\n','0' ) ;
            int first = Integer.parseInt( firstStr );
            System.out.println( "第一个数据：  " + first );
            String secondStr = useToNum.substring( useToNum.indexOf( "~" ) + 1 );
            int second = Integer.parseInt( secondStr );
            System.out.println( "第二个数据：  " + second );
            String scoreToCom = useToScore.substring( useToScore.indexOf( "分" ) - 2 , useToScore.indexOf( "分" ) );
            scoreToCom = scoreToCom.replace( ' ' , '0' ) ;
            double scoreToUse = Double.parseDouble( scoreToCom ) ;
            System.out.println( "分数：  " + scoreToUse );
            int ii = first ;
            for (  ; ii <= second ; ii ++ ) {
                scoreRecord [ ii ] = scoreToUse ;
            }
        }

        for ( int k = 1 ; k < 110 ; k ++ ) {
            System.out.print( scoreRecord [ k ] );
        }
        System.out.println();
        System.out.println( " 答案设置成功 " );



        return true;

    }



    @RequestMapping("/clear")

    public boolean clearBuf() throws SocketException {

        System.out.println("==================清空缓冲区开始================");
        byte[] bytes = new byte[1024];
        for(int i = 0; i < 3; i++){
            try{
                inputStream.read(bytes);
                System.out.println( bytes ) ;
            } catch (Exception e){
                System.out.println("第" + i + "次尝试读取缓冲区");
            }
        }
        System.out.println("==================清空缓冲区结束================");
        return true;

    }



    @RequestMapping ( "/setSum" )
    @ResponseBody

    public boolean setSum ( @RequestParam Map map ) {


//        System.out.println( map );

        System.out.println();
        System.out.println();
        System.out.println();




        String s = (String) map.keySet().iterator().next();
        sum = Integer.parseInt( s ) ;
        System.out.println( "读卡的总量：  " + sum );




        return true ;
    }



    @RequestMapping ( "/averageScore" )
    @ResponseBody

    public String averageScore (  ) {


        System.out.println( "scoreSum:  " + Double.valueOf( scoreSum ) );
        System.out.println( "sum:  " + Double.valueOf( sum ) );

        double resNum = Double.valueOf( scoreSum ) / Double.valueOf( sum ) ;

        System.out.println( "double类型：   " + resNum ) ;

        System.out.println( "保留4位小数的resNum：  " + String.format ( "%.4f" , resNum )  ) ;


        return String.format ( "%.4f" , resNum )  ;

    }



    @RequestMapping ( "/corRate" )
    @ResponseBody
    public Map < Integer , String > corRate ( ) {

        Map<Integer, String > map = new HashMap <Integer, String > ();


        // a是用来暂存CardType判断值的
        String a = CardType.substring( 0 , 2 ) ;
        System.out.println( a );


        if ( a == "10" ) {

            a = "105" ;

        }

        int a1 = Integer.parseInt( a ) ;

        for ( int i = 0 ; i < a1 ; i ++ ) {

            double tem = (double) (corRateInt [ i ] * 1.0 / sum) ;;

            map.put( i , String.format( "%.4f" , tem ) ) ;

        }

        return map ;

    }



    @RequestMapping ( "/forRank")
    @ResponseBody
    public List < Rank > rank ( ) {

        List < Rank > list = rankService.findByScore ( ) ;
        System.out.println ( list ) ;

        return list ;

    }



    @RequestMapping ( "/deleteAllRank" )
    @ResponseBody
    public boolean deleteAllRank ( ) {

        rankService.deleteAllRank() ;

        return true ;

    }

}
