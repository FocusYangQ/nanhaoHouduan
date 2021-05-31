package com.example.demo.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="test")
public class test {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="pwd")
    private String pwd;

}