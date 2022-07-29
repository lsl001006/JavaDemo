package com.example.network.mapper;

import com.example.network.model.Node2;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lsl
 * @date: 2022/7/29 22:55
 * @description:
 */
@Component

@Mapper
public interface NodeMapper {

    @Select("select * from new_nodes where category = #{category}")
    @Results(value = {
            @Result(id = true, column = "node_id", property = "id",jdbcType = JdbcType.VARCHAR),
            @Result(column = "node_name", property = "name",jdbcType = JdbcType.VARCHAR),
            @Result(column = "node_group", property = "group",jdbcType = JdbcType.INTEGER),
            @Result(column = "node_size", property = "size",jdbcType = JdbcType.INTEGER),
            @Result(column = "category", property = "category",jdbcType = JdbcType.VARCHAR)})
    List<Node2> selectAllNodes(String category);

    @Insert("insert into new_nodes values(NULL, #{node_name}), 0, 1, #{category}")
    void addNode(String node_name, String category);

    @Update("update new_nodes set node_group = #{node_group} where node_id = #{id}")
    void updateNodeGroup(Integer id, Integer node_group);

    @Update("UPDATE new_nodes,(SELECT *,COUNT(*) AS degree FROM \n" +
            "(SELECT triplet_source FROM new_triplets\n" +
            "UNION  ALL SELECT triplet_target FROM new_triplets)A\n" +
            "GROUP BY triplet_source)B\n" +
            "SET node_size = degree \n" +
            "WHERE  new_nodes.`node_name` = B.triplet_source AND new_nodes.`category` = #{category}")
    void updateNodeSize(String category);

    @Delete("delete from new_nodes where node_name = #{node_name} AND category = #{category}")
    void delNode(String node_name, String category);
}
