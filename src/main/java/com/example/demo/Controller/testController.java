package com.example.demo.Controller;

import com.example.demo.Component.Connect;
import com.example.demo.Component.Guangbiao.*;
import com.example.demo.Entity.*;
import com.example.demo.Service.IpService;
import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import static java.lang.Thread.sleep;

@CrossOrigin
@RestController
public class testController {

    @Autowired
    UserService userService;

    @Autowired
    Stu_id_nameService stu_id_nameService;

    @Autowired
    Std_ansService std_ansService;

    @Autowired
    IpService ipService;

    int port=8989;

    @RequestMapping("/connect")
    public String connect() throws IOException, InterruptedException {
        Connect c = new Connect();
        c.connect();
        return "连接成功";
    }

    @RequestMapping(value="/login",method= RequestMethod.POST)
    public boolean login(@RequestBody Map map){
        String name = (String) map.get("username");
        String pwd = (String) map.get("password");
        System.out.println(name);
        System.out.println(pwd);
        User u = userService.findByName(name);
        if(u.getPwd().equals(pwd)) return true;
        return false;
    }

    @RequestMapping("/read")
    public JSONArray read() throws JsonProcessingException {

        JSONArray jsonArray = new JSONArray();
        Score score1 = new Score();
        score1.setName("zhangsan1");
        score1.setStu_id("0001");
        score1.setScore(100);

        jsonArray.add(score1);

        Score score2 = new Score();
        score2.setName("zhangsan2");
        score2.setStu_id("0002");
        score2.setScore(99);

        jsonArray.add(score2);

        System.out.println(jsonArray);

        return jsonArray;
    }

    @RequestMapping(value="/shutdown",method=RequestMethod.POST)
    public boolean shutdown() throws Exception {

        String host = ipService.find_ip();
        int port = 8989;

        Socket socket = new Socket(host,port);

        System.out.println("=======断开平台========");

        exit e = new exit();
        e.exit(socket);

        return true;
    }

}
