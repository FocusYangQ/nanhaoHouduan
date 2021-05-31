package com.example.demo.Controller;

import com.example.demo.Entity.Stu_id_name;
import com.example.demo.Service.Stu_id_nameService;
import jxl.read.biff.BiffException;
import net.sf.json.JSONArray;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class upLoadController {

    @Autowired
    Stu_id_nameService stu_id_nameService;

    @RequestMapping(value = "/uploadExcel")
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
                    stu_id_name.setStuId(data);
                }
            }
            stu_id_nameService.saveOne(stu_id_name);
        }

        return true;
    }

    @RequestMapping(value="/uploadTable",method= RequestMethod.POST)
    @ResponseBody
    public boolean uploadTable(@RequestBody Object object){

        System.out.println("测试执行");
        System.out.println(object);
        JSONArray jsonArray = JSONArray.fromObject(object);
        System.out.println(jsonArray);
        Stu_id_name stu_id_name = new Stu_id_name();
        for(int i = 0 ; i < jsonArray.size() ; i ++){
            stu_id_name.setName((String) jsonArray.getJSONObject(i).get("name"));
            stu_id_name.setStuId((String) jsonArray.getJSONObject(i).get("stuId"));
            System.out.println("stu_id_name:  " + stu_id_name);
            stu_id_nameService.saveOne(stu_id_name);
        }

        return true;
    }

}
