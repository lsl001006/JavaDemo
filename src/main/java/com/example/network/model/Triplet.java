package com.example.network.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author: modige
 * @date: 2022/5/12 22:25
 * @description:
 */
@Data

public class Triplet {
    public int id;
    public String source;
    public String value;
    public String target;

    public JSONObject toJSON(){
        JSONObject ans = new JSONObject();

        ans.put("source",this.source);
        ans.put("value",this.value);
        ans.put("target",this.target);
        return ans;
    }
}
