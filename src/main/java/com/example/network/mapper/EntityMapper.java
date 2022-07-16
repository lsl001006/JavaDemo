package com.example.network.mapper;

/**
 * @author: modige
 * @date: 2022/5/17 0:04
 * @description:
 */

import com.example.network.model.Entity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;


import java.util.*;


@Component
@Mapper
public interface EntityMapper {


    @Select("select * from entities")
    @Results(value = {
            @Result(id = true, column = "entity_id", property = "identity",jdbcType = JdbcType.INTEGER),
            @Result(column = "entity_name", property = "name",jdbcType = JdbcType.VARCHAR),
            @Result(column = "entity_label", property = "label",jdbcType = JdbcType.VARCHAR),
            @Result(column = "entity_chinese", property = "chineseName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "entity_english", property = "englishName",jdbcType = JdbcType.VARCHAR),
            @Result(column = "entity_abbre", property = "abbreName",jdbcType = JdbcType.VARCHAR)

    })
    List<Entity> selectAllEntities();


    @Select("select entity_name from entities where entity_label=#{label}" )
    List<String> selectEntitiesByLabel(String label);

    @Select("select distinct entity_label from entities" )
    List<String> selectEntityLabels();


    List<Entity> selectMysql();

    @Select("select * from entities where entity_id = #{entity_id}")
    @Results(value = {
    @Result(id = true, column = "entity_id", property = "identity",jdbcType = JdbcType.INTEGER),
    @Result(column = "entity_name", property = "name",jdbcType = JdbcType.VARCHAR),
    @Result(column = "entity_label", property = "label",jdbcType = JdbcType.VARCHAR),
    @Result(column = "entity_chinese", property = "chineseName",jdbcType = JdbcType.VARCHAR),
    @Result(column = "entity_english", property = "englishName",jdbcType = JdbcType.VARCHAR),
    @Result(column = "entity_abbre", property = "abbreName",jdbcType = JdbcType.VARCHAR)

    })
    Entity selectById(String entity_id);

    @Select("select entity_id from entities")
    List<String> selectEntityIds();

    @Insert("insert into entities values(#{identity},#{name},#{label},#{chineseName},#{englishName},#{abbreName})")
    void addEntity(String identity,String name,String label,String chineseName,String englishName,String abbreName);

    @Insert("insert into nodes values(#{node_name}, 0, 1, #{node_name})")
    void addNode(String node_name);

    @Update("update entities set entity_name = #{name}, entity_label = #{label}, entity_chinese = #{chineseName}, entity_english = #{englishName}, entity_abbre = #{abbreName} where entity_id = #{identity}")
    void editEntity(String identity,String name,String label,String chineseName,String englishName,String abbreName);

    @Delete("delete from entities where entity_id = #{id}")
    void deleteEntity(String id);

    @Update("update nodes set node_group = #{node_group} where node_id = #{entity_name}")
    void updateNodeGroup(String entity_name, Integer node_group);

//    @Insert()
//    void Add_Entity_in_Batch(List<List> input){
//
//    }
}