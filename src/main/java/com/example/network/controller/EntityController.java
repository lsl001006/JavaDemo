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
        entityService.editEntity(entity.getIdentity(),
                                 entity.getName(),
                                 entity.getLabel(),
                                 entity.getChineseName(),
                                 entity.getEnglishName(),
                                 entity.getAbbreName());
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
        entityService.addEntity(entity.getIdentity(),
                                entity.getName(),
                                entity.getLabel(),
                                entity.getChineseName(),
                                entity.getEnglishName(),
                                entity.getAbbreName());

        return "success";
    }

}
