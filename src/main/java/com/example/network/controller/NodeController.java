package com.example.network.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.network.model.Node;
import com.example.network.model.Triplet2;
import com.example.network.service.NodeService;
import com.example.network.service.TripletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/29 22:22
 * &#064;description:
 */
@RestController
public class NodeController {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private TripletService tripletService;


    public void delNode(String node_name, String category){
        List<Triplet2> triplets = tripletService.selectAll(category);
        boolean inSource = false;
        for (Triplet2 tri:triplets){
            if (tri.source.equals(node_name)){
                inSource = true;
                break;
            }
        }
        if (!inSource){
            nodeService.delNode(node_name,category);
        }
    }


}
