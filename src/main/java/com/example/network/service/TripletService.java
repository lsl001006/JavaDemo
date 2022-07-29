package com.example.network.service;

import com.example.network.model.Triplet;
import com.example.network.model.Triplet2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: lsl
 * @date: 2022/7/29 22:27
 * @description:
 */
@Service
public interface TripletService {

    List<Triplet2> selectAll(String category);
    List<Triplet2> selectPage(int pagNum,int pageSize,String category);
    List<Triplet2> selectByEntity(String entityName, String category);
    Triplet2 selectById(int id);
    void updateTriplet(int id, String source, String value, String target, String category);
    void deleteById(int triplet_id, String triplet_target, String category);
    List<String> selectRelations(String category);

    void addTriplet(String source, String value, String target, String category);

}
