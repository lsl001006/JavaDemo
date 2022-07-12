package com.example.network.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author: modige
 * @date: 2022/5/12 11:45
 * @description:
 */
@Data
public class Entity {


    private String identity;

    private String name;


    private String label;


    private String chineseName;


    private String englishName;


    private String abbreName;
    public Entity(String identity,String name,String label,String chineseName,String englishName,String abbreName){
        this.setIdentity(identity);
        this.setName(name);
        this.setLabel(label);
        this.setChineseName(chineseName);
        this.setEnglishName(englishName);
        this.setAbbreName(abbreName);
    }


    public JSONObject toJSON(){
        JSONObject ans = new JSONObject();
        ans.put("name",this.name);
        ans.put("label",this.label);
        ans.put("identity",Integer.parseInt(this.identity));
        ans.put("中文名称",this.chineseName);
        ans.put("英文名称",this.englishName);
        ans.put("英文缩写",this.abbreName);
        return ans;
    }


}
