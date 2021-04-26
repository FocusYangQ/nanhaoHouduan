package com.example.demo.Controller;

import com.example.demo.Component.Connect;
import com.example.demo.Service.Std_ansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Contended;

import java.io.IOException;

@CrossOrigin
@RestController
public class Controller {

    @Autowired
    Std_ansService std_ansService;

    @RequestMapping("/delete_std_ans")
    public boolean delete_std_ans() throws IOException, InterruptedException {

        std_ansService.deleteall();

        return true;
    }

}
