package com.example.network.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author lsl
 * &#064;date  2022/7/27 15:34
 * &#064;description
 */
@Data
public class Node2 {

    public Integer id;

    public String name;

    public Integer group;

    public  String category;
    private Integer size;

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
