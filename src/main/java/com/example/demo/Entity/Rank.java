package com.example.demo.Entity;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Data
@Entity
@Table(name="forRank")
public class Rank {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="stuId")
    private String stuId;

    @Column(name="score")
    private Double score;

    @Column(name="ans")
    private String ans;

}
