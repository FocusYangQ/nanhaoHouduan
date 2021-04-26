package com.example.demo.Service;

import com.example.demo.Entity.Stu_id_name;
import com.example.demo.Repository.Stu_id_nameRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class Stu_id_nameService {

    @Resource
    private Stu_id_nameRepository stu_id_nameRepository;

    public boolean saveOne(Stu_id_name stu_id_name){
        stu_id_nameRepository.save(stu_id_name);
        return true;
    }

//    public boolean saveall(List<Stu_id_name> list){
//        stu_id_nameRepository.saveAll(list);
//        return true;
//    }

    public String find_name_by_id (String stuid){

        Stu_id_name tem;
        String name = "";
        List<Stu_id_name> name_entity = stu_id_nameRepository.findAll();
        for(int i=0;i<name_entity.size();i++){
            tem = name_entity.get(i);
            if(tem.getStu_id().equals(stuid)){
                name = tem.getName();
            }
        }
        return name;
    }

}
