package com.example.network.service.impl;

import com.example.network.mapper.EntityMapper;
import com.example.network.model.Entity;
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
    public List<Entity> selectAllEntities() {
        return entityMapper.selectAllEntities();
    }

    @Override
    public Entity selectById(String id) {
        return entityMapper.selectById(id);
    }

    @Override
    public List<String> selectEntitiesByLabel(String label) {
        return entityMapper.selectEntitiesByLabel(label);
    }

    @Override
    public List<String> selectEntityLabels() {
        return entityMapper.selectEntityLabels();
    }

    @Override
    public List<String> selectEntityIds() {
        return entityMapper.selectEntityIds();
    }

    @Override
    public void addEntity(String a,String b,String c,String d,String e,String f, Integer node_group) {
        entityMapper.addEntity(a, b, c, d, e, f);
        entityMapper.addNode(b);//添加实体对应的node
        entityMapper.updateNodeGroup(b,node_group); //更新node group
        System.out.println("Complete");
    }

    @Override
    public void editEntity(String a, String b, String c, String d, String e, String f) {
        entityMapper.editEntity(a,b,c,d,e,f);
    }

    @Override
    public void deleteEntity(String id) {
        entityMapper.deleteEntity(id);

    }




}
