package com.example.demo.Controller;

import com.example.demo.Entity.cardTem;
import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import com.example.demo.Service.UserService;
import com.sun.javaws.IconUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Thread.sleep;

@CrossOrigin
@RestController
public class temController {

    @Autowired
    UserService userService;

    @Autowired
    Stu_id_nameService stu_id_nameService;

    @Autowired
    Std_ansService std_ansService;
    
    @Autowired
    com.example.demo.Service.cardTemService cardTemService;

    String host= "192.168.2.239";
    int port=8989;

    @RequestMapping("/test")
    public boolean test(){
        return true;
    }

    @RequestMapping("/importTem")
    public boolean importTem(){

        String path = "G:\\Form\\";
        File file1 = new File(path);
        File[] array = file1.listFiles();
        ArrayList<String> list = new ArrayList<String>();
        cardTem cardTem = new cardTem();
        File file = null;
        String lineTxt = "";
        String res = "";
        String cardTe = "";
        for(int i = 0; i < array.length; i++){
            file = new File(path + array[i].getName());
            System.out.println(path + array[i].getName());
            try{
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                while ((lineTxt = br.readLine()) != null){
                    res += lineTxt;
                }
            } catch (Exception e){
                System.out.println("读取文件失败");
            }
            cardTe = array[i].getName();
            System.out.println("cardTepe:  " + cardTe);
            cardTem.setId(i+1);
            cardTem.setCard_type(cardTe);
            cardTem.setContent(res);
            System.out.println(cardTem);
            cardTemService.saveTem(cardTem);
            res = "";
        }

        return true;
    }

    @RequestMapping("/findTem")
    public String[] findTem(){

        String[] str = cardTemService.findAll().toArray(new String[0]);
        System.out.println(str);

        return str;
    }

    @RequestMapping("/saveTem")
    public String saveTem(@RequestBody Map map){

        String first = (String)map.get("temName");
        String second = (String)map.get("TemText");

        String path = "G:\\Form\\";
        System.out.println();
        //检查是否存在同名文件start
        File file1 = new File(path);
        File[] array = file1.listFiles();
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<array.length;i++){
            if(array[i].isFile()){
                if(array[i].getName().equals((String)map.get("temName") + ".txt")){
                    System.out.println("文件名重复，文件已存在");
                    return "2"; //文件重名
                }
            }
        }
        //检查是否存在同名文件end

        String textPath = path + (String)map.get("temName") + ".txt";
        System.out.println(textPath);
        String content = (String)map.get("TemText");
        System.out.println(content);
        File file = new File(textPath);
        try{
            file.createNewFile();
        } catch (Exception e){
            System.out.println("创建文件失败");
            return "1"; //创建文件失败
        }

        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        } catch (Exception e){
            System.out.println("向文件写入内容失败");
        }

        return "0";
    }



    @RequestMapping ( "/saveResult" )

    public boolean saveResult ( @RequestBody Map map ) {

        System.out.println ( map ) ;

        return true ;

    }





}
