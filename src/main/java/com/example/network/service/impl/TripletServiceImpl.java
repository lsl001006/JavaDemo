package com.example.network.service.impl;

import com.example.network.mapper.NodeMapper;
import com.example.network.mapper.TripletMapper;
import com.example.network.model.Triplet;
import com.example.network.model.Triplet2;
import com.example.network.service.TripletService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/12 19:04
 * &#064;description:
 */
@Service
public class TripletServiceImpl implements TripletService {

    @Autowired
    private TripletMapper tripletMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Override
    public List<Triplet2> selectAll(String category) {
        return tripletMapper.selectAll(category);
    }

    @Override
    public List<Triplet2> selectPage(int pageNum, int pageSize, String category) {
        PageHelper.startPage(pageNum,pageSize);
        List<Triplet2> triplets = tripletMapper.selectAll(category);
        PageInfo<Triplet2> pageInfo = new PageInfo(triplets);
        System.out.println("selectPage");
        return pageInfo.getList();
    }

    @Override
    public List<Triplet2> selectByEntity(String entityName, String category) {
        return tripletMapper.selectByEntity(entityName, category);
    }

    @Override
    public Triplet2 selectById(int triplet_id) {
        return tripletMapper.selectById(triplet_id);
    }

    @Override
    public void updateTriplet(int id, String source, String value, String target, String category) {
        tripletMapper.updateTriplet(id, source, value, target, category);
        System.out.println(source+"修改成功");
    }

    @Override
    public void deleteById(int triplet_id, String triplet_target, String category) {
        tripletMapper.deleteById(triplet_id);
        nodeMapper.delNode(triplet_target, category);
        nodeMapper.updateNodeSize(category);//添加三元组后更新node size
    }

    @Override
    public List<String> selectRelations(String category) {
        return tripletMapper.selectRelations(category);
    }

    @Override
    public void addTriplet(String source, String value, String target, String category) {
        tripletMapper.addTriplet(source,value,target,category);
        nodeMapper.updateNodeSize(category);//添加三元组后更新node size
    }

}
