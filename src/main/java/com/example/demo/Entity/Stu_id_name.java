package com.example.demo.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="test_stu_id_name")
public class Stu_id_name {

    @Id
    @Column(name="stuId")
    private String stuId;

    @Column(name="name")
    private String name;

}
