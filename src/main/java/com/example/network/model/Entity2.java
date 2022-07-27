package com.example.network.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author: lsl
 * @date: 2022/7/27 15:24
 * @description:
 */
@Data
public class Entity2 {

    private String id;
    private String name;
    private String label;

    private String category;

    private String attrs;


    public Entity2(String id, String name, String label, String category, String attrs){
        this.setId(id);
        this.setName(name);
        this.setLabel(label);
        this.setCategory(category);
        this.setAttrs(attrs);
    }


    public JSONObject toJSON(){
        JSONObject ans = new JSONObject();
        ans.put("id",Integer.parseInt(this.id));
        ans.put("name",this.name);
        ans.put("label",this.label);
        ans.put("category",this.category);
        ans.put("attrs",this.attrs);
        return ans;
    }


}