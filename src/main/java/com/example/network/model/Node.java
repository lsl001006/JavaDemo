package com.example.network.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author: modige
 * @date: 2022/5/12 22:07
 * @description:
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
