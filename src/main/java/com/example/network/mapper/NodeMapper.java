package com.example.network.mapper;

import com.example.network.model.Entity;
import com.example.network.model.Node;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: modige
 * @date: 2022/5/18 22:55
 * @description:
 */
@Component

@Mapper
public interface NodeMapper {

    @Select("select * from nodes")
    @Results(value = {
            @Result(id = true, column = "node_id", property = "id",jdbcType = JdbcType.VARCHAR),
            @Result(column = "node_name", property = "name",jdbcType = JdbcType.VARCHAR),
            @Result(column = "node_group", property = "group",jdbcType = JdbcType.INTEGER),
            @Result(column = "node_size", property = "size",jdbcType = JdbcType.INTEGER)    })
    List<Node> selectAllNodes();

    @Insert("insert into nodes values(#{node_name}, 0, 1, #{node_name})")
    void addNode(String node_name);

    @Update("update nodes set node_group = #{node_group} where node_name = #{node_name}")
    void updateNodeGroup(String node_name, Integer node_group);
}
