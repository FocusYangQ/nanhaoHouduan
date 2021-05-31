package com.example.demo.Repository;

import com.example.demo.Entity.Ip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpRepository extends JpaRepository<Ip, String> {
}
