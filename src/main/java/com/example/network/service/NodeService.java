package com.example.network.service;

import com.example.network.model.Node2;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/29 22:10
 * &#064;description:
 */
@Service
public interface NodeService {
    void addNode(String node_name, String category);

    void updateNodeGroup(Integer node_id, Integer node_group);

    List<Node2> selectAllNodes(String category);
    void delNode(String node_name, String category);
    void updateNodeSize(String category);

}
