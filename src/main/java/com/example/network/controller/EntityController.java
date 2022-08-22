package com.example.network.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.network.model.Entity;
import com.example.network.model.Entity2;
import com.example.network.model.Node;
import com.example.network.model.Triplet;
import com.example.network.service.EntityService;
import com.example.network.service.TripletService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/28 13:17
 * &#064;description:  实体控制器
 */
@RestController
public class EntityController {
    @Autowired
    private EntityService entityService;
    @Autowired
    private TripletService tripletService;

    public Map<String, Integer> readLabelJson(String category){
        String readString;
        File jsonFile = new File("src/main/resources/data/nodeLabelMap.json");
        try {
            FileReader fileReader = new FileReader(jsonFile);
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                int ch = reader.read();
                if (ch != -1) {
                    sb.append((char) ch);
                } else {
                    break;
                }
            }
            fileReader.close();
            reader.close();
            readString = sb.toString();
        } catch (IOException e) {
            readString = "";
        }
        JSONObject jsonObject = JSONObject.parseObject(readString);
        return jsonObject.get(category) == null ? new HashMap<>() : (Map<String, Integer>) jsonObject.get(category);
    }

    @GetMapping(value = "selectByLabel/{label}/{category}")//以JSONArray形式返回实体列表
    public List<String> getAllEntities(@PathVariable("label") String label, @PathVariable("category") String category) {
        List<String> entityNames = entityService.selectEntitiesByLabel(label, category);
        return entityNames;
    }

    @GetMapping(value = "selectEntityLabels/{category}")//以JSONArray形式返回实体列表
    public JSONObject getEntityLabels(@PathVariable("category") String category) {
        Map<String, Integer> entityLabelMap = readLabelJson(category);
        JSONObject jsonObject = new JSONObject();
        for (String entity : entityLabelMap.keySet()) {
            jsonObject.put(entityLabelMap.get(entity).toString(), entity);
        }
//        List<String> entityLabels = entityService.selectEntityLabels(category);
//        JSONArray res = new JSONArray();
//        for (String s:entityLabels){
//            JSONObject jo = new JSONObject();
//            jo.put("label",s);
//            res.add(jo);
//        }
        return jsonObject;
    }

    @GetMapping(value = "getEntitiesByLabel/{category}")//以JSONArray形式返回实体列表
    public JSONArray getEntitiesByLabel(@PathVariable("category") String category) {
        List<String> entityLabels = entityService.selectEntityLabels(category);
        JSONArray res = new JSONArray();

        for (String s:entityLabels){
            List<String> entityNames = entityService.selectEntitiesByLabel(s, category);
            JSONObject item = new JSONObject();
            item.put("value",s);
            item.put("label",s);
            JSONArray entityNamesArray = new JSONArray();
            for (String entity_name:entityNames){
                JSONObject entityJson = new JSONObject();
                entityJson.put("value",entity_name);
                entityNamesArray.add(entityJson);
            }
            item.put("children",entityNamesArray);
            res.add(item);
        }
        return res;
    }

    @GetMapping(value = "/entity/{id}")//以JSONArray形式返回实体列表
    public JSONObject getEntityById(@PathVariable("id") Integer id){
        Entity2 entity = entityService.selectById(id);
        JSONObject res = new JSONObject();
        res.put("entity",entity.toJSON());
        return res;
    }

    @GetMapping(value = "/delEntity/{id}")//
    public String delEntityById(@PathVariable("id") Integer id){
        entityService.deleteEntity(id);
        return "删除成功";
    }

    @RequestMapping(value = "test/{category}")//以JSONArray形式返回实体列表
    public List<Entity2> test(@PathVariable("category") String category){
        return entityService.selectAllEntities(category);
    }

    @RequestMapping(value = "entities/{category}")//以JSONArray形式返回实体列表
    public JSONObject getAllEntities(@PathVariable("category") String category){
        List<Entity2> entities = entityService.selectAllEntities(category);
        JSONObject res = new JSONObject();
        for (Entity2 entity:entities){
            res.put(entity.getName(), entity.toJSON());
        }
        return res;
    }
    @RequestMapping(value = "/e/{category}",method = RequestMethod.GET)
    public JSONObject entitiesJson(@PathVariable("category") String category){
        System.out.println("This is a tip");
        List<Entity2> entities = entityService.selectAllEntities(category);
        JSONObject res = new JSONObject();
        for (Entity2 e:entities)
            res.put(e.getName(),e.toJSON());
        return res;
    }
    @PostMapping("/editEntity")
    public String editEntity(@RequestBody Map<String,Entity2> form){
        Entity2 entity = form.get("form");
        System.out.println(entity);
        entityService.editEntity(entity.getId(),
                                 entity.getName(),
                                 entity.getLabel(),
                                 entity.getAttrs(),
                                 entity.getCategory());
        return "success";
    }

    @PostMapping("/addEntity")
    public String addEntity(@RequestBody Map<String,Entity2> form){
        Entity2 entity = form.get("form");
        System.out.println(entity);
        entityService.addEntity(entity.getName(),
                                entity.getLabel(),
                                entity.getAttrs(),
                                entity.getCategory());
        return "success";
    }

    @PostMapping("/entity/upload")
    public void getUpload_entity_batch(MultipartFile file) throws IOException {
        InputStream ip = file.getInputStream();
        byte[] b = new byte[ip.available()];//available()方法可以一次获取全部长度
        ip.read(b);//把读的内容存入字节数组b中
        String input_data = new String(b);

        String[] lines = input_data.split("\\r?\\n");//将输入数据按照换行符分行
        for (String line:lines){
            List<String> a = Arrays.asList(line.split(","));//将数据转换为list，并根据"，"切割
            String entity_name = a.get(0);
            String entity_name_f = entity_name.replaceAll("(\\r\\n|\\n|\\\\n|\\s)", "");
            String entity_label = a.get(1);
            String attrs = a.get(2);
            String category = a.get(3);
            entityService.addEntity(entity_name_f,entity_label,attrs,category);
        }
    }

}



