package com.example.network.mapper;

import com.example.network.model.Triplet;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface TripletMapper {

    @Select("select triplet_id as id,triplet_source as source,triplet_relation as value,triplet_target as target from triplets")
//    @Results(value = {
//            @Result(id = true, column = "triplet_id", property = "id",jdbcType = JdbcType.INTEGER),
//            @Result(column = "triplet_source", property = "source",jdbcType = JdbcType.VARCHAR),
//            @Result(column = "triplet_relation", property = "value",jdbcType = JdbcType.VARCHAR),
//            @Result(column = "triplet_target", property = "target",jdbcType = JdbcType.VARCHAR)
//    })
    List<Triplet> selectAll();

    @Select("select triplet_id as id,triplet_source as source,triplet_relation as value,triplet_target as target from triplets where triplet_source=#{entityName} OR triplet_target=#{entityName}")
    List<Triplet> selectByEntity(String entityName);

    @Select("select triplet_id as id,triplet_source as source,triplet_relation as value,triplet_target as target from triplets where triplet_id=#{triplet_id}")
    Triplet selectById(int triplet_id);

    @Select("select distinct triplet_relation as relation from triplets ")
    List<String> selectRelations();

    @Update("update triplets set triplet_source = #{source},triplet_relation = #{value},triplet_target=#{target} where triplet_id = #{id}")
    void updateTriplet(int id, String source, String value, String target);

    @Delete("delete from triplets where triplet_id = #{triplet_id}")
    void deleteById(int triplet_id);

    @Insert("insert into triplets values(NULL, #{a},#{b},#{c})")
    void addTriplet(String a,String b,String c);

    @Update("UPDATE nodes,(SELECT *,COUNT(*) AS degree FROM \n" +
            "(SELECT triplet_source FROM triplets\n" +
            "UNION  ALL SELECT triplet_target FROM triplets)A\n" +
            "GROUP BY triplet_source)B\n" +
            "SET node_size = degree \n" +
            "WHERE  nodes.`node_name` = B.triplet_source")
    void updateNodeSize();

    @Delete("delete from nodes where node_name = #{node_name}")
    void delNode(String node_name);

}