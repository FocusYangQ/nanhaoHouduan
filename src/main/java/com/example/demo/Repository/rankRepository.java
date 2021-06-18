package com.example.demo.Repository;

import com.example.demo.Entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface rankRepository extends JpaRepository < Rank, Integer > {
}
