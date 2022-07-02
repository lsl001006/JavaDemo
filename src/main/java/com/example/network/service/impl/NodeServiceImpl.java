package com.example.network.service.impl;

import com.example.network.mapper.NodeMapper;
import com.example.network.model.Node;
import com.example.network.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: modige
 * @date: 2022/5/12 22:11
 * @description:
 */
@Service
public class NodeServiceImpl implements NodeService {
    @Autowired
    private NodeMapper nodeMapper;


    @Override
    public void addNode(String node_name) {
        nodeMapper.addNode(node_name);
    }

    @Override
    public List<Node> selectAllNodes() {
        return nodeMapper.selectAllNodes();
    }

    @Override
    public void updateNodeGroup(String node_name, Integer node_group){
        nodeMapper.updateNodeGroup(node_name, node_group);
    }


}
