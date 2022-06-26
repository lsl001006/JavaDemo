package com.example.network.service;

import com.example.network.model.Entity;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: modige
 * @date: 2022/5/12 11:41
 * @description:
 */
@Service
public interface EntityService {


    List<Entity> selectAllEntities();
    Entity selectById(String id);

    List<String> selectEntitiesByLabel(String label);

    List<String> selectEntityLabels();

    List<String> selectEntityIds();

    void addEntity(String a,String b,String c,String d,String e,String f, Integer node_group);

    void editEntity(String a,String b,String c,String d,String e,String f);

    void deleteEntity(String id);
}
