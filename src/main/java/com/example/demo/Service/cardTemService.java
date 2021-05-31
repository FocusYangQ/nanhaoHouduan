package com.example.demo.Service;

import com.example.demo.Entity.cardTem;
import com.example.demo.Repository.cardTemRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class cardTemService {

    @Resource
    private cardTemRepository cardTypeRepository;

    public boolean saveTem(cardTem cardTem){
        cardTypeRepository.save(cardTem);
        return true;
    }

    public List<String> findAll(){
        ArrayList<cardTem> arr = (ArrayList<cardTem>) cardTypeRepository.findAll();
        ArrayList<String> list = new ArrayList<String>();
        String strTem = "";
        System.out.println("arr.size():  " + arr.size());
        for(int i = 0 ; i < arr.size() ; i ++){
            System.out.println(arr.get(i).getCard_type());
            strTem = arr.get(i).getCard_type();
            strTem = strTem.substring(0,strTem.indexOf("."));
            System.out.println("直接输出的值：  " + strTem);
            list.add(strTem);
            strTem = "";
        }
        return list;
    }

}
