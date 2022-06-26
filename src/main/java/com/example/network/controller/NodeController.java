package com.example.network.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.network.model.Node;
import com.example.network.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author: modige
 * @date: 2022/5/12 22:22
 * @description:
 */
@RestController
public class NodeController {
    @Autowired
    private NodeService nodeService;

    @RequestMapping(value = "ssssnodes")
    public String addAll(){
        String path = "E:\\oneDrive\\桌面\\前端\\network-test\\src\\main\\resources\\static\\link_detail.json";

        File file = new File(path);
        String ans = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = br.readLine()) != null) {
                if(line.length()!=0)
                    ans+=line;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        //存放整个文档
        JSONObject j =  JSONObject.parseObject(ans);

        JSONArray items = (JSONArray)j.get("nodes");

//        for (int i=0;i<items.size();i++){
//            JSONObject n = (JSONObject)items.get(i);
//            Node node = new Node();
//            node.setId(n.get("id").toString());
//            node.setName(n.get("name").toString());
//            node.setGroup(n.get("group").toString());
//            node.setSize(n.get("size").toString());
//            nodeService.addNode(node);
//
//
//            System.out.println(node);
//        }

        return "结点插入成功";
    }
}
