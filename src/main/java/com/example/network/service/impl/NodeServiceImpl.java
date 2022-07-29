package com.example.network.service.impl;

import com.example.network.mapper.NodeMapper;
import com.example.network.model.Node2;
import com.example.network.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/29 22:11
 * &#064;description:
 */
@Service
public class NodeServiceImpl implements NodeService {
    @Autowired
    private NodeMapper nodeMapper;


    @Override
    public void addNode(String node_name, String category) {
        nodeMapper.addNode(node_name, category);
    }

    @Override
    public List<Node2> selectAllNodes(String category) {
        return nodeMapper.selectAllNodes(category);
    }

    @Override
    public void updateNodeGroup(Integer node_id, Integer node_group){
        nodeMapper.updateNodeGroup(node_id, node_group);
    }

    public void updateNodeSize(String category)
    {
        nodeMapper.updateNodeSize(category);
    }

    public void delNode(String node_name, String category)
    {
        nodeMapper.delNode(node_name, category);
    }


}
