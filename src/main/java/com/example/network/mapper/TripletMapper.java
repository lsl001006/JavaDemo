package com.example.network.mapper;

import com.example.network.model.Triplet;
import com.example.network.model.Triplet2;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface TripletMapper {

    @Select("select * from new_triplets where category = #{category}")
    @Results(value = {
            @Result(id = true, column = "triplet_id", property = "id",jdbcType = JdbcType.INTEGER),
            @Result(column = "triplet_source", property = "source",jdbcType = JdbcType.VARCHAR),
            @Result(column = "triplet_relation", property = "value",jdbcType = JdbcType.VARCHAR),
            @Result(column = "triplet_target", property = "target",jdbcType = JdbcType.VARCHAR),
            @Result(column = "category", property = "category",jdbcType = JdbcType.VARCHAR)
    })
    List<Triplet2> selectAll(String category);

    @Select("select * from new_triplets where triplet_source=#{entityName} OR triplet_target=#{entityName} AND category=#{category}" )
    @Results(value = {
            @Result(id = true, column = "triplet_id", property = "id",jdbcType = JdbcType.INTEGER),
            @Result(column = "triplet_source", property = "source",jdbcType = JdbcType.VARCHAR),
            @Result(column = "triplet_relation", property = "value",jdbcType = JdbcType.VARCHAR),
            @Result(column = "triplet_target", property = "target",jdbcType = JdbcType.VARCHAR),
            @Result(column = "category", property = "category",jdbcType = JdbcType.VARCHAR)
    })
    List<Triplet2> selectByEntity(String entityName, String category);

    @Select("select * from new_triplets where triplet_id=#{triplet_id}")
    @Results(value = {
            @Result(id = true, column = "triplet_id", property = "id",jdbcType = JdbcType.INTEGER),
            @Result(column = "triplet_source", property = "source",jdbcType = JdbcType.VARCHAR),
            @Result(column = "triplet_relation", property = "value",jdbcType = JdbcType.VARCHAR),
            @Result(column = "triplet_target", property = "target",jdbcType = JdbcType.VARCHAR),
            @Result(column = "category", property = "category",jdbcType = JdbcType.VARCHAR)
    })
    Triplet2 selectById(int triplet_id);

    @Select("select distinct triplet_relation as relation from new_triplets where category=#{category}")
    List<String> selectRelations(String category);

    @Update("update new_triplets set triplet_id = #{id}," +
            " triplet_source = #{source}, triplet_relation = #{value}," +
            " triplet_target = #{target}, category = #{category} where triplet_id = #{id}")
    void updateTriplet(int id, String source, String value, String target, String category);

    @Delete("delete from new_triplets where triplet_id = #{triplet_id}")
    void deleteById(int triplet_id);

    @Insert("insert into new_triplets values(NULL, #{source},#{value},#{target},#{category})")
    void addTriplet(String source,String value,String target,String category);

}