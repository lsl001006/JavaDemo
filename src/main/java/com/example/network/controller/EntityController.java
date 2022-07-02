package com.example.network.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.network.model.Entity;
import com.example.network.model.Node;
import com.example.network.model.Triplet;
import com.example.network.service.EntityService;
import com.example.network.service.TripletService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: modige
 * @date: 2022/5/12 23:17
 * @description:
 */
@RestController
public class EntityController {
    @Autowired
    private EntityService entityService;
    @Autowired
    private TripletService tripletService;
//    public Integer mapLookUp(String label){
//        Map<String, Integer> labelmap = new HashMap<String, Integer>();
//        labelmap.put("中文名称",0);
//        labelmap.put("协议",1);
//        labelmap.put("信道",2);
//        labelmap.put("标识符",3);
//        labelmap.put("设备",4);
//        labelmap.put("控制",5);
//        labelmap.put("功能",6);
//        labelmap.put("网络",7);
//        labelmap.put("终端",8);
//        labelmap.put("信号",9);
//        labelmap.put("电路",10);
//        labelmap.put("英文缩写",11);
//        labelmap.put("模块",12);
//        labelmap.put("服务",13);
//        labelmap.put("接口",14);
//        labelmap.put("网关",15);
//        labelmap.put("消息",16);
//        labelmap.put("系统",17);
//        labelmap.put("模式",18);
//        labelmap.put("标识",19);
//        labelmap.put("业务",20);
//        labelmap.put("数据",21);
//        labelmap.put("路由器",22);
//        labelmap.put("英文名称",23);
//        labelmap.put("参数",24);
//        labelmap.put("default",25);
//        labelmap.put("术语",26);
//        labelmap.put("协",27);
//
//        return labelmap.get(label);
//    }


    @GetMapping(value = "selectByLabel/{label}")//以JSONArray形式返回实体列表
    public List<String> getAllEntities(@PathVariable("label") String label){
        List<String> entityNames = entityService.selectEntitiesByLabel(label);

        return entityNames;
    }

    @GetMapping(value = "selectEntityLabels")//以JSONArray形式返回实体列表
    public JSONArray getEntityLabels(){

        List<String> entityLabels = entityService.selectEntityLabels();

        JSONArray res = new JSONArray();
        for (String s:entityLabels){
            JSONObject jo = new JSONObject();
            jo.put("value",s);
            jo.put("label",s);
            res.add(jo);
        }
        return res;


    }

    @GetMapping(value = "getEntitiesByLabel")//以JSONArray形式返回实体列表
    public JSONArray getEntitiesByLabel(){
        List<String> entityLabels = entityService.selectEntityLabels();
        JSONArray res = new JSONArray();

        for (String s:entityLabels){
            List<String> entityNames = entityService.selectEntitiesByLabel(s);
            JSONObject item = new JSONObject();
            item.put("value",s);
            item.put("label",s);
            JSONArray entityNamesArray = new JSONArray();
            for (String entity_name:entityNames){
                JSONObject entityJson = new JSONObject();


                entityJson.put("value",entity_name);
                entityJson.put("label",entity_name);
                entityNamesArray.add(entityJson);

            }
            item.put("children",entityNamesArray);
            res.add(item);

        }

        return res;
    }


    @GetMapping(value = "/entity/{id}")//以JSONArray形式返回实体列表
    public JSONObject getEntityById(@PathVariable("id") String id){
        Entity entity = entityService.selectById(id);
        List<Triplet> triplets =tripletService.selectByEntity(entity.getName());
        JSONObject res = new JSONObject();
        res.put("entity",entity);
        res.put("triplets",triplets);

        return res;
    }

    @GetMapping(value = "/delEntity/{id}")//
    public String delEntityById(@PathVariable("id") String id){
        entityService.deleteEntity(id);

        return "删除成功";
    }




    @RequestMapping(value = "test")//以JSONArray形式返回实体列表
    public List<Entity> test(){
        List<Entity> entities = entityService.selectAllEntities();

        return entities;
    }

    @RequestMapping(value = "entities")//以JSONArray形式返回实体列表
    public JSONArray getAllEntities(){
        List<Entity> entities = entityService.selectAllEntities();
        JSONArray res = new JSONArray();
        for (Entity e:entities)
            res.add(e);
        return res;
    }



    @RequestMapping(value = "/e",method = RequestMethod.GET)//以JSONObject返回实体列表
    public JSONObject entitiesJson(){
        List<Entity> entities = entityService.selectAllEntities();
        JSONObject res = new JSONObject();
        for (Entity e:entities)
            res.put(e.getName(),e);
        return res;
    }
    @PostMapping("/editEntity")
    public String editEntity(@RequestBody Map<String,Entity> form){
        Entity entity = form.get("form");
        System.out.println(entity);


//        entityService.editEntity(entity.getIdentity(),entity.getName(),entity.getLabel(),entity.getChineseName(),entity.getEnglishName(),entity.getAbbreName());

        return "success";
    }

    @PostMapping("/addEntity")
    public String addEntity(@RequestBody Map<String,Entity> form){
        Entity entity = form.get("form");

        List<String> existEntityIds = entityService.selectEntityIds();
        int maxId = 0;
        for (String s:existEntityIds) {
            maxId = Math.max(Integer.parseInt(s), maxId);
        }

        entity.setIdentity(""+(maxId+1));
        System.out.println(entity);
//        System.out.println(entity.getIdentity());
//        System.out.println("Hello Java!");
//        Integer node_group = mapLookUp(entity.getLabel());
        entityService.addEntity(entity.getIdentity(),entity.getName(),entity.getLabel(),entity.getChineseName(),entity.getEnglishName(),entity.getAbbreName());

        return "success";
    }

    @GetMapping("/addEntity2")
    public String addEntity2(){
//        Entity entity = form.get("form");
//
//        List<String> existEntityIds = entityService.selectEntityIds();
//        int maxId = 0;
//        Node node = new Node();
//        node.setId("123456");
//        node.setSize(1);
//        node.setGroup();
//        for (String s:existEntityIds) {
//            maxId = Integer.parseInt(s)>maxId?Integer.parseInt(s):maxId;
//        }
//
//        entity.setIdentity(""+(maxId+1));
//        System.out.println(entity);
////        System.out.println(entity.getIdentity());
////        System.out.println("Hello Java!");
//        entityService.addEntity(entity.getIdentity(),entity.getName(),entity.getLabel(),entity.getChineseName(),entity.getEnglishName(),entity.getAbbreName());
//
//
//        return "success";
        System.out.println("123");
        return "success";
    }
//    @RequestMapping(value = {"addEntity"})
//    public ModelAndView addEntity(
//            @RequestParam("ename")String ename
////            @RequestParam("eid")String eid,
////            @RequestParam("elabel")String elabel,
////            @RequestParam("chinesename") String chinese,
////            @RequestParam("english")String englishname,
////            @RequestParam("abbre")String abbre
//                ){
////        Entity entity = new Entity(eid,ename,elabel,chinese,englishname,abbre);
//        Entity entity = new Entity();
//        entity.setName(ename);
////        entityService.addEntity(entity);
//        System.out.println("插入成功");
//        System.out.println(entity);
//        ModelAndView mvc = new ModelAndView("star");
//        return mvc;
//    }

}
