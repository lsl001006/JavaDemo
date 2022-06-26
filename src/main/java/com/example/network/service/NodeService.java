package com.example.network.service;

import com.example.network.model.Node;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: modige
 * @date: 2022/5/12 22:10
 * @description:
 */
@Service
public interface NodeService {
    void addNode(Node node);
    List<Node> selectAllNodes();
}
