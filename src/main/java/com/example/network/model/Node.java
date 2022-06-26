package com.example.network.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * &#064;author:  modige
 * &#064;date:  2022/5/12 22:07
 * &#064;description:
 */
@Data
public class Node {

    public String id;

    public String name;

    public int group;

    private int size;

    public JSONObject toJSON(){
        JSONObject ans = new JSONObject();
        ans.put("name",this.name);
        ans.put("id",this.id);
        ans.put("group",this.group);
        ans.put("size",this.size);
        return ans;
    }

}
