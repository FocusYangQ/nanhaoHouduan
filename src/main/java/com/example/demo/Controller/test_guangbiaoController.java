package com.example.demo.Controller;

import com.example.demo.Component.Connect;
import com.example.demo.Component.Guangbiao.*;
import com.example.demo.Entity.*;
import com.example.demo.Service.IpService;
import com.example.demo.Service.Std_ansService;
import com.example.demo.Service.Stu_id_nameService;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jxl.read.biff.BiffException;
import net.sf.json.JSONArray;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

import static java.lang.Thread.sleep;

@CrossOrigin
@RestController
public class test_guangbiaoController {

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


    @RequestMapping(value = "/upload")
    public boolean upload(@RequestParam("file") MultipartFile xls_file) throws IOException, InvalidFormatException, BiffException {

        System.out.println("传过来的数据file:"+xls_file);
        String name = xls_file.getOriginalFilename();
        System.out.println(name);

        InputStream inputStream = xls_file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        System.out.println(workbook);

        int sheet_size = workbook.getNumberOfSheets();
        System.out.println(sheet_size);
        Sheet sheet = workbook.getSheetAt(0);
        int row_total = sheet.getLastRowNum()+1;
        System.out.println(row_total);
        Row row = sheet.getRow(0);
        int colLength = row.getLastCellNum();
        Cell cell = row.getCell(0);

        Stu_id_name stu_id_name = new Stu_id_name();
        String data="";

        for (int i = 1; i < row_total; i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < colLength; j++) {
                cell = row.getCell(j);
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    data = cell.getStringCellValue();
                }
                if(j==0){
                    stu_id_name.setName(data);
                }
                if(j==1){
                    stu_id_name.setStu_id(data);
                }
            }
            stu_id_nameService.saveOne(stu_id_name);
        }

        return true;
    }


    @RequestMapping(value="/set_ip",method=RequestMethod.POST)
    public boolean set_ip(@RequestParam Map map) throws Exception {

        ipService.delete_all();

        String s = (String) map.keySet().iterator().next();
        System.out.println(s);

        Ip ip = new Ip();
        ip.setIp(s);
        ipService.save_ip(ip);

        return true;
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
