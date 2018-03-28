package cn.avenchang.dao;

import cn.avenchang.domain.Manager;
import cn.avenchang.domain.Venue;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by 53068 on 2018/3/27 0027.
 */
@Mapper
public interface VenueDao {

    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    @Insert("Insert INTO venue(email, password, token, flag, name, regist_time, location) " +
            "VALUE (#{email}, #{password}, '', 0, #{name}, now(), #{location})")
    int register(Venue venue);

    @Update("UPDATE venue as v, venue_to_approve as v_a " +
            "SET v.flag = 1, v_a.flag = 1 " +
            "WHERE v.id = v_a.id AND v.id = #{id}")
    int confirmRegister(Long id);

    @Select("SELECT * FROM venue WHERE id = #{id} AND token = #{token}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "flag", property = "flag"),
            @Result(column = "email", property = "email")
    })
    Venue validToken(@Param("id") Long id, @Param("token") String token);

    @Update("UPDATE venue SET token = #{token} WHERE id = #{id} AND password = #{password}")
    int updateToken(Venue venue);

    @Update("UPDATE venue SET token = '' WHERE id = #{id} AND token = #{token}")
    int logout(@Param("id") Long id, @Param("token") String token);

    @Update("UPDATE venue_to_approve SET name = #{name}, location = #{location}, flag = 2 WHERE id = #{id}")
    int updateInfo(Venue venue);

    @Update("UPDATE venue as v, venue_to_approve as v_a " +
            "SET v.name = v_a.name, v.location = v_a_name, v.flag = 1, v_a.flag = 1 " +
            "WHERE v.id = v_a.id AND v_a.id = #{id}")
    int confirmUpdateInfo(@Param("id") Long id);

    @Select("SELECT * FROM venue_to_approve WHERE flag != 1")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "flag", property = "flag"),
            @Result(column = "name", property = "name"),
            @Result(column = "location", property = "location")
    })
    List<Venue> getUnapprovedVenue();

    @Select("SELECT * FROM venue WHERE id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "flag", property = "flag"),
            @Result(column = "name", property = "name"),
            @Result(column = "location", property = "location")
    })
    Venue findVenueById(Long id);
}
