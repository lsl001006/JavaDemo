package com.example.network.service;

import com.example.network.model.Entity;

import com.example.network.model.Entity2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/28 17:10
 * &#064;description:
 */
@Service
public interface EntityService {


    List<Entity2> selectAllEntities(String category);
    Entity2 selectById(Integer id);

    List<String> selectEntitiesByLabel(String label, String category);

    List<String> selectEntityLabels(String category);

    List<String> selectAllCategories();

    List<String> selectEntityIds(String category);

    void addEntity(String name, String label, String attrs, String category);

    void editEntity(Integer id,String name,String label,String attrs,String category);

    void deleteEntity(Integer id);

}
