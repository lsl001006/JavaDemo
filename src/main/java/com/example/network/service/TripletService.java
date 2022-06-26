package com.example.network.service;

import com.example.network.model.Triplet;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: modige
 * @date: 2022/5/12 22:27
 * @description:
 */
@Service
public interface TripletService {

    List<Triplet> selectAll();
    List<Triplet> selectPage(int pagNum,int pageSize);
    List<Triplet> selectByEntity(String entityName);
    Triplet selectById(int id);
    void updateTriplet(int id,String source,String value,String target);
    void deleteById(int triplet_id, String triplet_target);
    List<String> selectRelations();


    void addTriplet(String a,String b,String c);

}
