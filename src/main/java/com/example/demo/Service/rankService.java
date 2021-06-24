package com.example.demo.Service;

import com.example.demo.Entity.Rank;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class rankService {

    @Resource
    private com.example.demo.Repository.rankRepository rankRepository ;

    public boolean saveRank ( Rank rank ) {

        rankRepository.save( rank ) ;
        return true ;

    }

    public List< Rank > findByScore (  ) {

        List < Rank > rank = rankRepository.findAll (  ) ;
        Collections.sort(rank, new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                return o2.getScore().compareTo( o1.getScore() ) ;
            }
        }) ;

        return rank ;
    }

    public boolean deleteAllRank ( ) {

        rankRepository.deleteAll();
        return true ;

    }

}
