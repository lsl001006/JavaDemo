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
 * &#064;author:  lsl
 * &#064;date:  2022/7/29 22:22
 * &#064;description:
 */
@RestController
public class NodeController {
    @Autowired
    private NodeService nodeService;


}
