package com.example.demo;

import com.example.demo.Entity.Rank;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import com.example.demo.Service.rankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class mysqlTest {

    @Autowired
    UserService userService ;

    @Autowired
    rankService rankService ;

    @Test
    void mysqlTest ( ) {

        List<User> list =  userService.findAll ( ) ;
        for ( User u : list ) {
            System.out.println ( u.getName() ) ;
        }

    }

    @Test
    void saveRank ( ) {

        Rank rank = new Rank ( ) ;
        rank.setStuId ( "666" ) ;
        rank.setScore (103.0) ;
        rank.setName( "hello world2" );
        System.out.println ( rank ) ;
        rankService.saveRank ( rank ) ;

    }

    @Test
    void findAll ( ) {

        List < Rank > list = rankService.findByScore ( ) ;
        for ( Rank r : list ) {
            System.out.println ( r ) ;
        }

    }


}
