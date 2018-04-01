package cn.avenchang.dao;

import cn.avenchang.domain.Orders;
import cn.avenchang.model.OrderInfo;
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

    @Update("UPDATE orders as o, seat_state as s, ticket as t " +
            " SET o.state = 3, s.state = 0, t.state = 2" +
            " WHERE o.id = #{id} AND o.state = 0 AND o.plan_id = s.plan_id" +
            "    AND o.id = t.order_id AND t.row = s.row AND t.col = s.col")
    int overdue(@Param("id") Long id);

    @Update("UPDATE orders as o, user as u" +
            " SET o.state = 1, o.account_id = #{accountId}," +
            "    u.total_points = u.total_points + o.original_price, u.rest_points = u.rest_points + o.actual_price" +
            " WHERE o.id = #{id} AND o.state = 0 AND o.user_id = u.id ")
    int paid(@Param("id") Long id, @Param("accountId") Long accountId);

    @Update("UPDATE orders as o, user as u" +
            " SET o.state = 1, o.actual_price = o.actual_price - 50, o.account_id = #{accountId}," +
            "    u.rest_points = u.rest_points - 1000 + o.actual_points, u.total_points = u.total_points + o.original_price" +
            " WHERE o.id = #{id} AND o.state = 0 AND o.user_id = u.id ")
    int paidByUseProfit(@Param("id") Long id, @Param("accountId") Long accountId);

    @Update("UPDATE orders as o, plan as p" +
            " SET o.state = 2 " +
            " WHERE o.id = #{id} AND o.state = 1 AND o.plan_id = p.id AND p.time > now()")
    int refund(@Param("id") Long id);

    @Select("SELECT o.id, o.original_price, o.actual_price, p.title" +
            " FROM orders as o, plan as p" +
            " WHERE o.plan_id = p.id AND o.id = #{id} AND o.state = 0")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "original_price", property = "originalPrice"),
            @Result(column = "actual_price", property = "actualPrice"),
            @Result(column = "title", property = "planTitle")
    })
    OrderInfo getUnpaidOrderDetail(@Param("id") Long id);

    @Select("SELECT o.id, o.venue_id, o.plan_id, o.account_id, " +
            "   o.time, o.state, o.actual_price, p.title " +
            " FROM orders as o, plan as p" +
            " WHERE o.user_id = #{userId} AND o.plan_id = p.id" +
            " ORDER BY o.time desc")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "venue_id", property = "venueId"),
            @Result(column = "plan_id", property = "planId"),
            @Result(column = "account_id", property = "accountId"),
            @Result(column = "time", property = "time"),
            @Result(column = "state", property = "state"),
            @Result(column = "actual_price", property = "actualPrice"),
            @Result(column = "title", property = "planTitle")
    })
    List<OrderInfo> getOrders(@Param("userId") Long userId);
}
