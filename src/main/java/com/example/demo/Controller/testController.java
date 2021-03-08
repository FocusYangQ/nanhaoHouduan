package com.example.demo.Controller;

import com.example.demo.Component.Connect;
import com.example.demo.Component.ForGB.ForGB;
import com.example.demo.Component.ForGB.ForGB2;
import com.example.demo.Component.ForGB.ForGB3;
import com.example.demo.Component.RequestIP.IpUtil;
import com.example.demo.Component.testGuangbiao.*;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@CrossOrigin
@RestController
public class testController {

    @Autowired
    UserService userService;

//    String host= "192.168.2.239";
//    int port=8899;
//    String host= "192.168.2.239";
//    int port=8899;

    @RequestMapping("/test")
    public boolean test(){
        return true;
    }

    @RequestMapping("/testmysql")
    public String testfindById(int id){
        return userService.getNameById(id).getName();
    }

    @RequestMapping("/connect")
    public String connect() throws IOException, InterruptedException {
        Connect c = new Connect();
        c.connect();
        return "连接成功";
    }

    //form表单的格式
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public boolean login(@RequestBody Map map){
        String name = (String) map.get("username");
        String pwd = (String) map.get("password");
        System.out.println(name);
        System.out.println(pwd);
        User u = userService.findByName(name);
        if(u.getPwd().equals(pwd)) return true;
        return false;
    }

    @RequestMapping(value="/set_tem",method=RequestMethod.POST)
    @ResponseBody
    public boolean set_tem (@RequestParam Map map) throws Exception {

//        System.out.println(map);
        String s = (String) map.keySet().iterator().next();
        System.out.println(s);
        String s_for_use =s.substring(0, s.indexOf("型"));
//        System.out.println(s_for_use);
        String file_path = "G:\\Form\\" + s_for_use + ".txt";
        System.out.println(file_path);
        readFile r = new readFile();
        r.get_template(file_path);

        System.out.println("执行到这里了么");

//        String host= "192.168.100.106";
//        int port=8899;
//
//        Socket socket = new Socket(host, port);
//
//        System.out.println("==============链接测试==============");
//        connect connect = new connect();
//        connect.connect(socket);
//
//        System.out.println("==============发送模板测试==============");
//        template tem = new template();
//        tem.template(socket, file_path);

        return true;
    }

    @RequestMapping("/guangbiao")
    public String guangbiao() throws Exception {
        test_fun t = new test_fun();
        return "答案：" + t.test();
    }

    @RequestMapping("/shutdown")
    public String shutdown() throws Exception {
        shutdown sd = new shutdown();
        sd.shutdown();
        return "shutdown";
    }

//    @RequestMapping("/read_once")
//    public String read_once() throws Exception {
//
//        String host= "192.168.2.239";
//        int port=8899;
//        Socket socket = new Socket(host,port);
//
//        String message = "R A/";
//
//        send s = new send();
//        receive r = new receive();
//
//        s.send(socket,message);
//        sleep(2000);
//        r.receive(socket);
//
//        message = "r A 0001 2048/";
//        s.send(socket,message);
//        sleep(2000);
//
//        String res = r.receive(socket);
//        System.out.println("卡的数据量：" + res.substring(3,6));
//        System.out.println("卡的类型：" + res.substring(6,7));
//        System.out.println("答案：" + res.substring(22));
//
//        return res.substring(22);
//
//    }

//    @RequestMapping(value="/classid",method=RequestMethod.POST)
    @RequestMapping(value="/classid")
    public List<String> classNum () throws Exception {
        List<String> classid = new ArrayList<String>();
        ForGB forGB = new ForGB();
        ForGB2 forGB2 = new ForGB2();
        ForGB3 forGB3 = new ForGB3();
        classid.add("三年六班");
        classid.add(new String("三年六班".getBytes(StandardCharsets.UTF_8),"GB2312"));
        classid.add(new String("三年六班".getBytes(StandardCharsets.UTF_16),"GB2312"));
//        classid.add(new String("三年六班".getBytes(StandardCharsets.GBK),"GB2312"));
        classid.add(new String("三年六班".getBytes("GB2312"),"utf-8"));
        classid.add("涓夊勾鍏\uE160彮");
        classid.add(forGB.ForGB("三年六班"));
        classid.add(forGB2.ForGB2("三年六班"));
        classid.add(forGB3.ForGB3("三年六班"));
//        classid.add("三年六班");
//        classid.add("sannianliuban");
//        classid.add("三年六班");
//        String classid = "["+ "\"" + "三年六班" + "\"" + "]";
//        String res = new String(classNum().getBytes(StandardCharsets.UTF_8),"GB2312")
//        String encode = "GB2312";
//        if (classid.equals(new String(classid.getBytes(encode), encode))) {      //判断是不是GB2312
//            return classid;
//
//        } else{
//            return "不是GB2312编码格式";
//        }
        return classid;
    }

    @RequestMapping("/name")
    public List<String> name (HttpServletRequest request)throws Exception {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        System.out.println(date);

        String ipAddress = IpUtil.getIpAddr(request);
        System.out.print(ipAddress + "  ");
        System.out.print(date + "  ");
        System.out.print("访问姓名库  ");

        List<User> List_user = userService.findAll();
        //遍历java数组，提取某个键的内容
        List<String> user_name =  List_user.stream().map(User::getName).collect(Collectors.toList());
        List<String> user_name_GB2312 = new ArrayList<String>();
        for(int i=0; i<user_name.size(); i++){
            user_name_GB2312.add(new String(user_name.get(i).getBytes(StandardCharsets.UTF_8),"GB2312"));
        }
        return user_name_GB2312;
    }



    @RequestMapping(value = "/testip", method = RequestMethod.GET)
    public String test(HttpServletRequest request) {
        //获取IP地址
        String ipAddress = IpUtil.getIpAddr(request);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        System.out.println(date + "  " + ipAddress + "访问");

        return "your ip is:" + ipAddress;
    }
}
