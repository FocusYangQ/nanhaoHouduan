package com.example.demo.Controller;

import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    String host= "192.168.2.239";
    int port=8989;

    @RequestMapping("/test")
    public boolean test(){
        return true;
    }

    @RequestMapping("/importTem")
    public boolean importTem(){



        return true;
    }

}
