package com.example.demo.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="test_std_ans")
public class Std_ans {

    @Id
    @Column(name="std_ans")
    private String std_ans;
}
