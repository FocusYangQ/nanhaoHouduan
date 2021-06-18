package com.example.demo.Service;

import com.example.demo.Entity.Ip;
import com.example.demo.Repository.IpRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IpService {

    @Resource
    private IpRepository ipRepository;

    public boolean save_ip ( Ip ip ) {

        ipRepository.save(ip);

        return true;
    }

    public boolean delete_all ( ) {

        ipRepository.deleteAll();

        return true;

    }

    public String find_ip ( ) {

        List<Ip> ip = ipRepository.findAll();
        Ip ip_list = ip.get(0);
        String ip_res = ip_list.getIp();

        return ip_res;

    }
}
