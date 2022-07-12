package com.example.network.service;

import com.example.network.model.Entity;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * &#064;author:  shanglin
 * &#064;date:  2022/5/12 11:41
 * &#064;description:
 */
@Service
public interface EntityService {


    List<Entity> selectAllEntities();
    Entity selectById(String id);

    List<String> selectEntitiesByLabel(String label);

    List<String> selectEntityLabels();

    List<String> selectEntityIds();

    void addEntity(String identity,String name,String label,String chineseName,String englishName,String abbreName);

    void editEntity(String identity,String name,String label,String chineseName,String englishName,String abbreName);

    void deleteEntity(String id);
}
