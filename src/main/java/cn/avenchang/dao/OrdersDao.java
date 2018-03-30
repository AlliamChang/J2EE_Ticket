package cn.avenchang.dao;

import cn.avenchang.domain.Orders;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by 53068 on 2018/3/30 0030.
 */
@Mapper
public interface OrdersDao {

    @Insert("INSERT INTO orders(user_id, venue_id, plan_id, account_id, time, ticket_num, original_price, actual_price) " +
            "VALUE " +
            "(#{userId}, #{venueId}, #{planId}, -1, now(), #{ticketNum}, #{originalPrice}, #{actualPrice})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int newOrders(Orders orders);

    @Update("UPDATE orders SET state = 3 WHERE id = #{id} AND state = 0")
    int overdue(@Param("id") Long id);

    @Update("UPDATE orders SET state = 1 WHERE id = #{id} AND state = 0")
    int paid(@Param("id") Long id);

    @Update("UPDATE orders as o, plan as p" +
            " SET o.state = 2" +
            " WHERE o.id = #{id} AND o.state = 1 AND o.plan_id = p.id AND p.time > now()")
    int refund(@Param("id") Long id);

    @Select("SELECT * FROM orders WHERE userId = #{userId}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "venue_id", property = "venueId"),
            @Result(column = "plan_id", property = "planId"),
            @Result(column = "account_id", property = "accountId"),
            @Result(column = "time", property = "time"),
            @Result(column = "state", property = "state"),
            @Result(column = "actual_price", property = "actualPrice")
    })
    List<Orders> getOrders(@Param("userId") Long userId);
}
