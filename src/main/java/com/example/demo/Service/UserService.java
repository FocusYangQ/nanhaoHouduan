package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.Entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserRepository userRepository;


    public User getNameById(int id){
        return userRepository.getOne(id);
    }
    public List<User> findAll(){ return userRepository.findAll(); }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}
