package com.example.network.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.network.model.Entity2;
import com.example.network.model.Node2;
import com.example.network.model.Triplet2;
import com.example.network.service.EntityService;
import com.example.network.service.NodeService;
import com.example.network.service.TripletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/28 12:14
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

    public Integer mapLookUp(String label, String category){
        // mapLookUp函数重构， 传入参数加入category用于指示所属图谱类别
        // 同时，node的种类与node_group数值的对应关系由原始的代码定义改为了文本读入的方式，以便更好的维护
        Map<String, Integer> labelmap;
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
        labelmap = (Map<String, Integer>) jsonObject.get(category);
        System.out.println(labelmap);
        return labelmap.get(label);
    }

    public void checkNodeAndUpdate(String name, String category) {
        // checkNodeAndUpdate函数重构， 传入参数加入category用于指示所属图谱类别
        //用于检查nodes表中是否存在所需name的node
        //导入entities, nodes 两个表中的全部内容
        List<Entity2> entities = entityService.selectAllEntities(category);
        List<Node2> nodes = nodeService.selectAllNodes(category);
        boolean hasNode = false;
        Integer node_id = 0;
        for (Entity2 entity : entities) {
            if (entity.getName().equals(name)) {
                // 查找nodes表中是否有name这个名称的node
                for (Node2 node : nodes) {
                    if (node.getName().equals(name)) {
                        //若存在，则终止添加操作
                        hasNode = true;
                        node_id = node.getId();
                        break;
                    }
                }
                if (!hasNode) {
                    String label = entity.getLabel();
                    Integer node_group = mapLookUp(label, category);
                    nodeService.addNode(entity.getName(), category);
                    nodeService.updateNodeGroup(node_id, node_group);
                }
                break;
            }

        }
    }


    @RequestMapping(value = "triplets/{category}") //测试语句
    public JSONArray getAllTriples(@PathVariable("category") String category) {

        JSONArray keyLinks = new JSONArray();

        List<Triplet2> triplets = tripletService.selectAll(category);

        for (Triplet2 triplet:triplets)
            keyLinks.add(triplet.toJSON());
        return keyLinks;
    }


    @PostMapping("/addTriplet") //添加三元组
    public void addTriplet(@RequestBody Map<String,JSONObject> map){
    // TODO 待完善，form返回参数需包括category图谱类别参数
        JSONObject triplet = map.get("form");
        ArrayList<String> source = (ArrayList<String>) triplet.get("source");
        ArrayList<String> target = (ArrayList<String>) triplet.get("target");
        ArrayList<String> relation = (ArrayList<String>) triplet.get("value");
        ArrayList<String> category = (ArrayList<String>) triplet.get("category");
        String source_name = source.get(1);
        String target_name = target.get(1);
        String connection = relation.get(0);
        String category_name = category.get(0);

        //首先检查source中选择的实体是否在图中存在node,若没有，则新建对应的node
        checkNodeAndUpdate(source_name, category_name);
        //其次检查target中选择的实体是否在图中存在node,若没有，则新建对应的node
        checkNodeAndUpdate(target_name, category_name);

        tripletService.addTriplet(source_name, connection, target_name, category_name);
    }


    @PostMapping("/updateTriplet")
    public void updateTriplet(@RequestBody Map<String,JSONObject> map){

        JSONObject triplet = map.get("form");
        System.out.println(triplet.get("source"));
        ArrayList<String> source = (ArrayList<String>) triplet.get("source");
        ArrayList<String> target = (ArrayList<String>)triplet.get("target");
        ArrayList<String> relation = (ArrayList<String>)triplet.get("value");
        ArrayList<String> category = (ArrayList<String>) triplet.get("category");
        String source_name = source.get(1);
        String target_name = target.get(1);
        String connection = relation.get(0);
        String category_name = category.get(0);

        tripletService.updateTriplet(Integer.parseInt(triplet.get("id").toString()),
                                    source_name,
                                    connection,
                                    target_name,
                                    category_name);
    }

    @GetMapping("/selTripletById/{id}")
    public JSONObject selectById(@PathVariable("id") Integer id){
        Triplet2 triplet = tripletService.selectById(id);
        String category = triplet.getCategory();
        List<Entity2> entities = entityService.selectAllEntities(category);
        String targetLabel="",sourceLabel="";
        for (Entity2 entity:entities){
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
        res.put("category",category);

        return res;
    }

    @PostMapping("/batchDeleteTriplet")//批量删除三元组
    public String delTripletsByIds(@RequestBody Map<String, List<Integer>> ids){
        List<Integer> ids2 = ids.get("ids");
        System.out.println("Triplets to Delete:"+ids2.toString());
        for (int id:ids2){
            Triplet2 tri = tripletService.selectById(id);
            tripletService.deleteById(id, tri.target, tri.category);
        }
        return "批量删除成功！";
    }


    @GetMapping("/delTripletById/{id}")
    public String deleteById(@PathVariable("id") Integer id){
    // 用于删除三元组，传入id即可删除对应的三元组
        System.out.println(id);
        Triplet2 tri = tripletService.selectById(id);
        tripletService.deleteById(id, tri.target, tri.category);
        return "删除成功";
    }

    @GetMapping("/getRelations/{category}")
    public JSONArray getRelations(@PathVariable("category") String category){
    // 用于获取所有的关系类型
        List<String> relations = tripletService.selectRelations(category);
        JSONArray res = new JSONArray();
        for (String s:relations){
            JSONObject jo = new JSONObject();
            jo.put("value",s);
            res.add(jo);
        }
        return res;
    }

    @GetMapping("/triplets/{page}/{size}/{category}")
    public List<Triplet2> findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @PathVariable("category") String category){
        return tripletService.selectPage(page,size,category);
    }


    @RequestMapping(value = "links/{category}")
    public JSONObject getAllLinks(@PathVariable("category") String category) {
        // 每次网页刷新和执行的时候调用，用于将数据库中的数据更新到页面中
        JSONObject res = new JSONObject();
        JSONArray keyNodes = new JSONArray();
        JSONArray keyLinks = new JSONArray();

        List<Node2> nodes = nodeService.selectAllNodes(category);
        List<Triplet2> triplets = tripletService.selectAll(category);
        for (Node2 n:nodes){
            int size = n.getSize()/7 + (n.getSize()%7!=0?1:0) ;
            n.setSize(8 + size*5);
            keyNodes.add(n.toJSON());
        }
        for (Triplet2 triplet:triplets)
            keyLinks.add(triplet.toJSON());
        res.put("nodes",keyNodes);
        res.put("links",keyLinks);
        return res;
    }

    public void addTriplet_in_Batch(String triplet_source,String triplet_relation,String triplet_target, String category){
        // 批量导入三元组
        //首先检查source中选择的实体是否在图中存在node,若没有，则新建对应的node
        checkNodeAndUpdate(triplet_source, category);
        //其次检查target中选择的实体是否在图中存在node,若没有，则新建对应的node
        checkNodeAndUpdate(triplet_target, category);
        tripletService.addTriplet(triplet_source,triplet_relation,triplet_target,category);
    }

    @PostMapping("/triplet/upload")
    public JSONObject getUpload_triplet_batch(MultipartFile file) throws IOException {
        // 传入category参数，用于区分三元组的图谱类别
        InputStream ip = file.getInputStream();
        byte[] b = new byte[ip.available()];//available()方法可以一次获取全部长度
        ip.read(b);//把读的内容存入字节数组b中
        String input_data = new String(b);

        String[] lines = input_data.split("\\r?\\n");//将输入数据按照换行符分行
        List<String> problem_source = new ArrayList<>();
        List<String> problem_target = new ArrayList<>();

        for (String line:lines){
            List<String> a = Arrays.asList(line.split(","));//将数据转换为list，并根据"，"切割
            String triplet_source = a.get(0);
            String triplet_relation = a.get(1);
            String triplet_target = a.get(2);
            String category = a.get(3);
            String triplet_source_f = triplet_source.replaceAll("(\\r\\n|\\n|\\\\n|\\s)", "");
            String triplet_source_ff = triplet_source_f.replaceAll("\\p{C}", "");
            List<Entity2> entities = entityService.selectAllEntities(category);
            boolean has_source = false;
            boolean has_target = false;
            for (Entity2 entity:entities){
                if (entity.getName().equals(triplet_source_ff)){
                    has_source = true;
                }
                if (entity.getName().equals(triplet_target)){
                    has_target = true;
                }
            }
            if (has_source & has_target){
                addTriplet_in_Batch(triplet_source_ff,triplet_relation,triplet_target, category);
            }
            if (!has_source){
                problem_source.add(triplet_source_f);
            }
            if (!has_target){
                problem_target.add(triplet_target);
            }

        }
        System.out.println("不存在的头实体"+problem_source);
        System.out.println("不存在的尾实体"+problem_target);

        JSONObject res = new JSONObject();
        JSONArray prob_src = new JSONArray();
        JSONArray prob_tail = new JSONArray();
        prob_src.addAll(problem_source);
        prob_tail.addAll(problem_target);
        res.put("head", prob_src);
        res.put("tail", prob_tail);
        return res;
    }

}
