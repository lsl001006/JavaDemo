package com.example.network.mapper;

/**
 * @author: modige
 * @date: 2022/5/17 0:04
 * @description:
 */

import com.alibaba.fastjson.JSONObject;
import com.example.network.model.Entity;
import com.example.network.model.Entity2;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;


import java.util.*;


@Component
@Mapper
public interface EntityMapper {

    @Select("select * from new_entities where category = #{category}")
    @Results(value = {
            @Result(id = true, column = "entity_id", property = "id",jdbcType = JdbcType.INTEGER),
            @Result(column = "entity_name", property = "name",jdbcType = JdbcType.VARCHAR),
            @Result(column = "entity_label", property = "label",jdbcType = JdbcType.VARCHAR),
            @Result(column = "attrs", property = "attrs",jdbcType = JdbcType.VARCHAR),
            @Result(column = "category", property = "category",jdbcType = JdbcType.VARCHAR)
    })
    List<Entity2> selectAllEntities(String category);


    @Select("select entity_name from new_entities where entity_label=#{label} and category=#{category}" )
    List<String> selectEntitiesByLabel(String label, String category);

    @Select("select distinct entity_label from new_entities where category=#{category}" )
    List<String> selectEntityLabels(String category);

    @Select("select distinct category from new_entities")
    List<String> selectAllCategories();


    @Select("select * from new_entities where entity_id = #{entity_id}" )
    @Results(value = {
    @Result(id = true, column = "entity_id", property = "id",jdbcType = JdbcType.INTEGER),
    @Result(column = "entity_name", property = "name",jdbcType = JdbcType.VARCHAR),
    @Result(column = "entity_label", property = "label",jdbcType = JdbcType.VARCHAR),
    @Result(column = "attrs", property = "attrs",jdbcType = JdbcType.VARCHAR),
    @Result(column = "category", property = "category",jdbcType = JdbcType.VARCHAR)
    })
    Entity2 selectById(Integer entity_id);

    @Select("select entity_id from new_entities where category=#{category}" )
    List<String> selectEntityIds(String category);

    @Insert("insert into new_entities values(NULL, #{name},#{label},#{attrs},#{category})")
    void addEntity(String name, String label, String attrs, String category);

    @Update("update new_entities set entity_name = #{name}, entity_label = #{label}, attrs = #{attrs}, category = #{category} where entity_id = #{id}")
    void editEntity(Integer id, String name,String label,String attrs,String category);

    @Delete("delete from new_entities where entity_id = #{id}")
    void deleteEntity(Integer id);

}