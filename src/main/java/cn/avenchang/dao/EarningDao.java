package cn.avenchang.dao;

import cn.avenchang.model.VenueEarning;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2018/4/4 0004.
 */
@Mapper
public interface EarningDao {

    @Insert("INSERT INTO earning(venue_id, plan_id)" +
            " VALUE" +
            " (#{venueId}, #{planId})")
    int addNewPlanEarning(@Param("planId") Long planId, @Param("venueId") Long venueId);

    @Select("SELECT e.venue_id, p.venue_name, SUM(e.earning) as earning" +
            " FROM earning as e, plan as p" +
            " WHERE e.plan_id = p.id AND p.time < now() AND e.is_settle = 0" +
            " GROUP BY e.venue_id")
    @Results({
            @Result(column = "venue_id", property = "venueId"),
            @Result(column = "venue_name", property = "venueName"),
            @Result(column = "earning", property = "earning")
    })
    List<VenueEarning> getUnsettleEarning();

    /**
     * 场馆售票后先进账到网站中
     * @param orderId
     * @return
     */
    @Update("UPDATE earning as e, orders as o" +
            " SET e.earning = e.earning + o.actual_price" +
            " WHERE o.id = #{orderId} AND o.plan_id = e.plan_id")
    int income(@Param("orderId") Long orderId);

    /**
     * 根据时间，退款部分金额，而后在网站的场馆累积赚钱中扣除相应金额
     * @param planId
     * @param accountId
     * @param amount
     * @return
     */
    @Update("UPDATE earning as e, account as a" +
            " SET e.earning = e.earning - #{amount}, a.balance = a.balance + #{amount}" +
            " WHERE e.plan_id = #{planId} AND a.id = #{accountId}")
    int refund(@Param("planId") Long planId, @Param("accountId") Long accountId, @Param("amount") Double amount);

    /**
     * 经理手动给场馆结算(只是标记为结算)
     * @param venueId
     * @param now
     * @return
     */
    @Update("UPDATE earning as e, plan as p SET e.is_settle = 1" +
            " WHERE e.plan_id = p.id AND e.venue_id = #{venueId} AND p.time < #{now}")
    int settle(@Param("venueId") Long venueId, @Param("now") Date now);

    /**
     * 网站收取手续费赚的钱
     * @param income
     * @return
     */
    @Update("UPDATE earning SET earning = earning + #{income} WHERE venue_id = 10000000 AND plan_id = 0 AND is_settle = 1")
    int webIncome(@Param("income") Double income);

    @Select("SELECT SUM(e.earning) as earning" +
            " FROM earning as e, plan as p" +
            " WHERE e.plan_id = p.id AND e.venue_id = #{venueId} AND p.time < #{now} AND e.is_settle = 0")
    Double getVenueEarning(@Param("venueId") Long venueId, @Param("now") Date now);

    /**
     * 网站目前为止赚的钱
     * @return
     */
    @Select("SELECT earning FROM earning WHERE venue_id = 10000000 AND plan_id = 0 AND is_settle = 1")
    Double getWebEarning();
}
