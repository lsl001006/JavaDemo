package com.example.network.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.network.mapper.EntityMapper;
import com.example.network.model.Entity;
import com.example.network.model.Entity2;
import com.example.network.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: modige
 * @date: 2022/5/12 12:07
 * @description:
 */
@Service
public class EntityServiceImpl implements EntityService {

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<Entity2> selectAllEntities(String category) {
        return entityMapper.selectAllEntities(category);
    }

    @Override
    public Entity2 selectById(Integer id) {
        return entityMapper.selectById(id);
    }

    @Override
    public List<String> selectEntitiesByLabel(String label, String category) {
        return entityMapper.selectEntitiesByLabel(label, category);
    }

    @Override
    public List<String> selectEntityLabels(String category) {
        return entityMapper.selectEntityLabels(category);
    }

    @Override
    public List<String> selectEntityIds(String category) {
        return entityMapper.selectEntityIds(category);
    }

    @Override
    public void addEntity(String name, String label, String attrs, String category) {
        entityMapper.addEntity(name, label, attrs, category);
        System.out.println("Complete");
    }

    @Override
    public void editEntity(Integer id,String name,String label,String attrs,String category) {
        entityMapper.editEntity(id, name, label, attrs, category);
    }

    @Override
    public void deleteEntity(Integer id) {
        entityMapper.deleteEntity(id);

    }


}
