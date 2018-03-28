package cn.avenchang.dao;

import cn.avenchang.domain.Manager;
import cn.avenchang.domain.Venue;
import cn.avenchang.model.VenueUpdate;
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
    int approveRegister(@Param("id") Long id);

    @Update("UPDATE venue as v, venue_to_approve as v_a " +
            "SET v.flag = 3, v_a.flag = 3, token = '' " +
            "WHERE v.id = v_a.id AND v.id = #{id}")
    int refuseRegister(@Param("id") Long id);

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

    @Update("UPDATE venue as v, venue_to_approve as v_a " +
            "SET v_a.name = #{name}, v_a.location = #{location}, v.flag = 2, v_a.flag = 2 " +
            "WHERE v.id = #{id} AND v.id = v_a.id")
    int updateInfo(Venue venue);

    @Update("UPDATE venue as v, venue_to_approve as v_a " +
            "SET v.name = v_a.name, v.location = v_a.location, v.flag = 1, v_a.flag = 1 " +
            "WHERE v.id = v_a.id AND v_a.id = #{id}")
    int approveUpdateInfo(@Param("id") Long id);

    @Update("UPDATE venue as v, venue_to_approve as v_a " +
            "SET v.flag = 1, v_a.flag = 1 " +
            "WHERE v.id = v_a.id AND v_a.id = #{id}")
    int refuseUpdateInfo(@Param("id") Long id);

    @Select("SELECT v.id as id, v.flag as flag, v.name as name, v.location as location, v.regist_time as regist_time " +
            "FROM venue as v, venue_to_approve as v_a " +
            "WHERE v.id = v_a.id AND v_a.flag = 0")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "flag", property = "flag"),
            @Result(column = "name", property = "name"),
            @Result(column = "location", property = "location"),
            @Result(column = "regist_time", property = "registTime")
    })
    List<Venue> getRegisterUnapprovedVenue();

    @Select("SELECT v.id as id, v.name as oldName, v_a.name as newName, v.location as oldLocation, v_a.location as newLocation " +
            "FROM venue as v, venue_to_approve as v_a " +
            "WHERE v_a.flag = 2 AND v.id = v_a.id")
    @Results({
            @Result(column = "id", property = "venueId"),
            @Result(column = "oldName", property = "oldName"),
            @Result(column = "newName", property = "newName"),
            @Result(column = "oldLocation", property = "oldLocation"),
            @Result(column = "newLocation", property = "newLocation")
    })
    List<VenueUpdate> getUpdateUnapprovedVenue();

    @Select("SELECT * FROM venue WHERE id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "flag", property = "flag"),
            @Result(column = "name", property = "name"),
            @Result(column = "location", property = "location"),
            @Result(column = "account", property = "account"),
            @Result(column = "regist_time", property = "registTime")
    })
    Venue findVenueById(@Param("id") Long id);

    @Select("SELECT email FROM venue WHERE id = #{id}")
    String findVenueEmail(@Param("id") Long id);
}
