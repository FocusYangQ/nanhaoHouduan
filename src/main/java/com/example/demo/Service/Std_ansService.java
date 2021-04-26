package com.example.demo.Service;

import com.example.demo.Entity.Std_ans;
import com.example.demo.Repository.Std_ansRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class Std_ansService {

    @Resource
    private Std_ansRepository std_ansRepository;

    public boolean save(Std_ans std_ans){
        std_ansRepository.save(std_ans);
        return true;
    }

    public String findStd_ans(){

        List<Std_ans> list_res = std_ansRepository.findAll();

        return list_res.get(0).getStd_ans();
    }

    public boolean deleteall(){
        std_ansRepository.deleteAll();
        return true;
    }
}
