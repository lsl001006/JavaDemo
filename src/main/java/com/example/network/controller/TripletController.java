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
import java.util.List;
import java.util.Map;

/**
 * @author: modige
 * @date: 2022/5/6 0:12
 * @description:控制三元组的展示与增删改查
 */
@RestController
public class TripletController {

    @Autowired
    private TripletService tripletService;


    @Autowired
    private NodeService nodeService;

    @Autowired
    private EntityService entityService;


    @RequestMapping(value = "triplets")
    public JSONArray getAllTriples(){

        JSONArray keyLinks = new JSONArray();

        List<Triplet> triplets = tripletService.selectAll();

        for (Triplet triplet:triplets)
            keyLinks.add(triplet.toJSON());
        System.out.println("Hello");
        return keyLinks;
    }



    @PostMapping("/addTriplet") //添加三元组
    public Triplet addTriplet(@RequestBody Map<String,JSONObject> map){

        JSONObject triplet = map.get("form");
//        System.out.println(triplet.get("source"));
        ArrayList<String> source = (ArrayList<String>) triplet.get("source");
        ArrayList<String> target = (ArrayList<String>)triplet.get("target");
//        System.out.println(source);
//        System.out.println(target);
        System.out.println(triplet.get("id").toString());


        tripletService.addTriplet(source.get(1),triplet.get("value").toString(),target.get(1));
        return new Triplet();
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

    @PostMapping("/delTripletsByIds")//批量删除三元组 TODO
    public String delTripletsByIds(@RequestBody List<Integer> ids){
        System.out.println("Triplets to Delete:"+ids.toString());
        for (int id:ids){
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

        List<Triplet> triplets = tripletService.selectPage(page,size);
        return triplets;
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
        finally {
        }

        try {
            JSONObject j = JSONObject.parseObject(ans);
            System.out.println("取值成功");
            return j;
        }
        catch (Exception e){
            System.out.println(e);
        }
        finally {

        }
        return null;

    }



        @RequestMapping(value = "addtri")
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

            JSONArray items = (JSONArray)j.get("links");

            for (int i=0;i<items.size();i++){
                JSONObject n = (JSONObject)items.get(i);
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
