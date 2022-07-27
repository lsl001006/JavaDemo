package com.example.network.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author lsl
 * @date 2022/7/27 15:34
 * @description
 */
@Data
public class Node2 {

    public String id;

    public String name;

    public int group;

    public  String category;
    private int size;

    public JSONObject toJSON(){
        JSONObject ans = new JSONObject();
        ans.put("category",this.category);
        ans.put("name",this.name);
        ans.put("id",this.id);
        ans.put("group",this.group);
        ans.put("size",this.size);
        return ans;
    }

}
