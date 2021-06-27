package com.example.demo;

import com.example.demo.Entity.confirm;
import com.example.demo.Service.confirmService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class confirmTest {

    @Autowired
    confirmService confirmService ;

    @Test
    void PTest ( ) {

        confirm p = new confirm( ) ;
        p.setMark ( "true" ); ;
        confirmService.p ( p ) ;

    }

    @Test
    void v ( ) {

        confirmService.v () ;

    }

}
