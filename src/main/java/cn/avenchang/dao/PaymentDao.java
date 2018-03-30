package cn.avenchang.dao;

import cn.avenchang.domain.Earning;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2018/3/30 0030.
 */
@Mapper
public interface PaymentDao {

    @Select("SELECT e.venue_id, v.name, SUM(e.earning) as earning " +
            "FROM plan as p, earning as e, venue as v " +
            "WHERE p.id = e.plan_id AND p.time < now() AND v.id = e.venue_id AND is_settle = 0 " +
            "GROUP BY e.venue_id")
    @Results({
            @Result(column = "venue_id", property = "venueId"),
            @Result(column = "name", property = "venueName"),
            @Result(column = "earning", property = "earning")
    })
    List<Earning> getSettleList();

    @Select("SELECT SUM(e.earning) " +
            "FROM plan as p, earning as e " +
            "WHERE p.id = e.plan_id AND p.time < now() AND is_settle = 0 ")
    Double getSettleByVenueId(@Param("venueId") Long venueId);

    @Insert("INSERT INTO earning(venue_id, plan_id) VALUE (#{venueId}, #{planId})")
    int insertEarning(@Param("venueId") Long venueId, @Param("planId") Long planId);

    @Update("UPDATE earning as e, plan as p" +
            " SET e.is_settle = 1" +
            " WHERE e.venue_id = #{venueId} AND e.plan_id = p.id AND p.time < #{now}")
    int settlePlan(@Param("venueId") Long venueId, @Param("now") Date now);

    @Update("UPDATE account SET balance = balance - #{amount} WHERE id = #{id} AND password = #{password} AND balance > #{amount}")
    int pay(@Param("id") Long id, @Param("password") String password, @Param("amount") Double amount);

    @Update("UPDATE account SET balance = balance + #{amount} WHERE id = #{id}")
    int refund(@Param("id") Long id, @Param("amount") Double amount);

    @Update("UPDATE earning SET earning = earning + #{earning} WHERE venue_id = #{venueId} AND plan_id = #{planId}")
    int earn(@Param("venueId") Long venueId, @Param("planId") Long planId, @Param("earning") Double earning);

    @Update("UPDATE venue SET account = account + #{earning} WHERE id = #{venueId}")
    int settle(@Param("venueId") Long venueId, @Param("earning") Double earning);

    @Update("UPDATE earning SET earning = earning + #{earning} WHERE venue_id = 10000000")
    int webEarn(@Param("earning") Double earning);
}
