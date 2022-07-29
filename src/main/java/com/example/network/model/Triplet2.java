package com.example.network.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * &#064;author:  lsl
 * &#064;date:  2022/7/27 15:29
 * &#064;description:
 */
@Data

public class Triplet2 {
    public Integer id;

    public String category;

    public String source;

    public String value;

    public String target;

    public JSONObject toJSON(){
        JSONObject ans = new JSONObject();
        ans.put("category", this.category);
        ans.put("source",this.source);
        ans.put("value",this.value);
        ans.put("target",this.target);
        return ans;
    }
}