package com.example.network.service.impl;

import com.example.network.mapper.TripletMapper;
import com.example.network.model.Triplet;
import com.example.network.service.TripletService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: modige
 * @date: 2022/5/12 22:28
 * @description:
 */
@Service
public class TripletServiceImpl implements TripletService {

    @Autowired
    private TripletMapper tripletMapper;


    @Override
    public List<Triplet> selectAll() {
        return tripletMapper.selectAll();
    }





    @Override
    public List<Triplet> selectPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Triplet> triplets = tripletMapper.selectAll();
        PageInfo<Triplet> pageInfo = new PageInfo(triplets);
        System.out.println("selectPage");
        return pageInfo.getList();
    }

    @Override
    public List<Triplet> selectByEntity(String entityName) {
        return tripletMapper.selectByEntity(entityName);
    }

    @Override
    public Triplet selectById(int triplet_id) {
        return tripletMapper.selectById(triplet_id);
    }

    @Override
    public void updateTriplet(int id, String source, String value, String target) {
        tripletMapper.updateTriplet(id,source,value,target);
        System.out.println(source+"修改成功");
    }

    @Override
    public void deleteById(int triplet_id, String triplet_target) {
        tripletMapper.deleteById(triplet_id);
        tripletMapper.delNode(triplet_target);
        tripletMapper.updateNodeSize();//添加三元组后更新node size
    }

    @Override
    public List<String> selectRelations() {
        return tripletMapper.selectRelations();
    }

    @Override
    public void addTriplet(String a, String b, String c) {
        tripletMapper.addTriplet(a,b,c);
        tripletMapper.updateNodeSize();//添加三元组后更新node size
    }


}
