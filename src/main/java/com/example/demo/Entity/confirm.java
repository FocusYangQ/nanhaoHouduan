package com.example.demo.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table ( name = "confirm" )
public class confirm {

//    @Id
//    @Column(name="id")
//

    @Id
    @Column(name= "mark")
    private String mark;

}
