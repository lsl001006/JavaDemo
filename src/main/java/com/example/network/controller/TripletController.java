package com.example.network.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.network.model.Entity;
import com.example.network.model.Node;
import com.example.network.model.Triplet;
import com.example.network.service.EntityService;
import com.example.network.service.NodeService;
import com.example.network.service.TripletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * &#064;author:  modige
 * &#064;date:  2022/5/6 0:12
 * &#064;description:控制三元组的展示与增删改查
 */
@RestController
public class TripletController {

    @Autowired
    private TripletService tripletService;


    @Autowired
    private NodeService nodeService;

    @Autowired
    private EntityService entityService;

    public Integer mapLookUp(String label){
        Map<String, Integer> labelmap = new HashMap<String, Integer>();
        labelmap.put("中文名称",0);
        labelmap.put("协议",1);
        labelmap.put("信道",2);
        labelmap.put("标识符",3);
        labelmap.put("设备",4);
        labelmap.put("控制",5);
        labelmap.put("功能",6);
        labelmap.put("网络",7);
        labelmap.put("终端",8);
        labelmap.put("信号",9);
        labelmap.put("电路",10);
        labelmap.put("英文缩写",11);
        labelmap.put("模块",12);
        labelmap.put("服务",13);
        labelmap.put("接口",14);
        labelmap.put("网关",15);
        labelmap.put("消息",16);
        labelmap.put("系统",17);
        labelmap.put("模式",18);
        labelmap.put("标识",19);
        labelmap.put("业务",20);
        labelmap.put("数据",21);
        labelmap.put("路由器",22);
        labelmap.put("英文名称",23);
        labelmap.put("参数",24);
        labelmap.put("default",25);
        labelmap.put("术语",26);
        labelmap.put("协",27);

        return labelmap.get(label);
    }

    public void checkNodeAndUpdate(String name) {
        //用于检查nodes表中是否存在所需name的node
        //导入entities, nodes 两个表中的全部内容
        List<Entity> entities = entityService.selectAllEntities();
        List<Node> nodes = nodeService.selectAllNodes();
        Boolean hasNode = false;
        for (Entity entity : entities) {
            if (entity.getName().equals(name)) {
                // 查找nodes表中是否有name这个名称的node
                for (Node node : nodes) {
                    if (node.getName().equals(name)) {
                        //若存在，则终止添加操作
                        hasNode = true;
                    }
                }
                if (!hasNode) {
                    String label = entity.getLabel();
                    Integer node_group = mapLookUp(label);
                    nodeService.addNode(entity.getName());
                    nodeService.updateNodeGroup(entity.getName(), node_group);
                }
                break;
            }
        }
    }


    @RequestMapping(value = "triplets") //测试语句
    public JSONArray getAllTriples(){

        JSONArray keyLinks = new JSONArray();

        List<Triplet> triplets = tripletService.selectAll();

        for (Triplet triplet:triplets)
            keyLinks.add(triplet.toJSON());
        System.out.println("Hello");
        return keyLinks;
    }


    @PostMapping("/addTriplet") //添加三元组
    public void addTriplet(@RequestBody Map<String,JSONObject> map){

        JSONObject triplet = map.get("form");


        ArrayList<String> source = (ArrayList<String>) triplet.get("source");
        ArrayList<String> target = (ArrayList<String>) triplet.get("target");

        System.out.println(triplet.get("id").toString());


            //首先检查source中选择的实体是否在图中存在node,若没有，则新建对应的node
            checkNodeAndUpdate(source.get(1));
            //其次检查target中选择的实体是否在图中存在node,若没有，则新建对应的node
            checkNodeAndUpdate(target.get(1));


        tripletService.addTriplet(source.get(1), triplet.get("value").toString(), target.get(1));
//        return new Triplet();
    }


    @PostMapping("/updateTriplet")
    public Triplet updateTriplet(@RequestBody Map<String,JSONObject> map){

        JSONObject triplet = map.get("form");
        System.out.println(triplet.get("source"));
        ArrayList<String> source = (ArrayList<String>) triplet.get("source");
        ArrayList<String> target = (ArrayList<String>)triplet.get("target");
        System.out.println("Hello Java!");
        System.out.println(Integer.parseInt(triplet.get("id").toString()));
        System.out.println("Hello Java2!");
        tripletService.updateTriplet(Integer.parseInt(triplet.get("id").toString()),source.get(1),triplet.get("value").toString(),target.get(1));

        return new Triplet();
    }
    @GetMapping("/selTripletById/{id}")
    public JSONObject selectById(@PathVariable("id") Integer id){
        Triplet triplet = tripletService.selectById(id);
        List<Entity> entities = entityService.selectAllEntities();
        String targetLabel="",sourceLabel="";
        for (Entity entity:entities){
            if (entity.getName().equals(triplet.source)) sourceLabel=entity.getLabel();
            if (entity.getName().equals(triplet.target)) targetLabel=entity.getLabel();

        }
        JSONObject res = new JSONObject();
        JSONArray source=new JSONArray();
        JSONArray target = new JSONArray();
        source.add(sourceLabel);
        source.add(triplet.source);
        target.add(targetLabel);
        target.add(triplet.target);
        res.put("id",triplet.id);
        res.put("source",source);
        res.put("target",target);
        res.put("value",triplet.value);

        return res;
    }

    @PostMapping("/batchDeleteTriplet")//批量删除三元组 TODO
    public String delTripletsByIds(@RequestBody Map<String, List<Integer>> ids){
        List<Integer> ids2 = ids.get("ids");
        System.out.println("Triplets to Delete:"+ids2.toString());
        for (int id:ids2){
            Triplet tri = tripletService.selectById(id);
            tripletService.deleteById(id, tri.target);
        }
        return "批量删除成功！";
    }


    @GetMapping("/delTripletById/{id}")
    public String deleteById(@PathVariable("id") Integer id){
        System.out.println(id);
        Triplet tri = tripletService.selectById(id);
        tripletService.deleteById(id, tri.target);
        return "删除成功";
    }

    @GetMapping("/getRelations")
    public JSONArray getRelations(){

        List<String> relations = tripletService.selectRelations();
        JSONArray res = new JSONArray();
        for (String s:relations){
            JSONObject jo = new JSONObject();
            jo.put("value",s);
            jo.put("label",s);
            res.add(jo);
        }
        return res;
    }

    @GetMapping("/triplets/{page}/{size}")
    public List<Triplet> findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size){

        return tripletService.selectPage(page,size);
    }


    @RequestMapping(value = "links")
    public JSONObject getAllLinks(){
        JSONObject res = new JSONObject();
//        JSONObject keyNodes = new JSONObject();
        JSONArray keyNodes = new JSONArray();
        JSONArray keyLinks = new JSONArray();
        List<Node> nodes = nodeService.selectAllNodes();
        System.out.println(nodes.size());
        List<Triplet> triplets = tripletService.selectAll();
        for (Node n:nodes){
            int size = n.getSize()/7 + (n.getSize()%7!=0?1:0) ;

            n.setSize(8 + size*5);
            keyNodes.add(n.toJSON());

        }
        for (Triplet triplet:triplets)
            keyLinks.add(triplet.toJSON());
        res.put("nodes",keyNodes);
        res.put("links",keyLinks);
        return res;
    }

    @RequestMapping(value = "/l",method = RequestMethod.GET)
    public JSONObject linksJson(){
        File file = new File("E:\\oneDrive\\桌面\\前端\\network-test\\src\\main\\resources\\static\\link_detail.json");
        StringBuilder ans = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if(line.length()!=0)
                    ans.append(line);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }


        try {
            JSONObject j = JSONObject.parseObject(ans.toString());
            System.out.println("取值成功");
            return j;
        }
        catch (Exception e){
            System.out.println(e);
        }

        return null;

    }



        @RequestMapping(value = "addtri")
        public String addAll(){
            String path = "E:\\oneDrive\\桌面\\前端\\network-test\\src\\main\\resources\\static\\link_detail.json";

            File file = new File(path);
            StringBuilder ans = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    if(line.length()!=0)
                        ans.append(line);
                }
            }
            catch (Exception e){
                System.out.println(e);
            }

            //存放整个文档
            JSONObject j =  JSONObject.parseObject(ans.toString());

            JSONArray items = (JSONArray)j.get("links");

            for (Object item : items) {
                JSONObject n = (JSONObject) item;
                Triplet triplet = new Triplet();
                triplet.setSource(n.get("source").toString());
                triplet.setValue(n.get("value").toString());
                triplet.setTarget(n.get("target").toString());
//                tripletService.addTriplet(triplet);


                System.out.println(triplet);
            }




            return "插入成功";
        }
}
