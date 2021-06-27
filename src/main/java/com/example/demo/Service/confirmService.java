package com.example.demo.Service;

import com.example.demo.Entity.confirm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class confirmService {

    @Resource
    private com.example.demo.Repository.confirmRepository confirmRepository;

    public boolean p ( confirm p ) {

        confirmRepository.save ( p ) ;

        return true ;
    }

    public boolean v ( ) {

        confirmRepository.deleteAll ( ) ;

        return true ;
    }

    public List< confirm > select ( ) {

        List < confirm > list = confirmRepository.findAll ( ) ;

        return list ;

    }



}
