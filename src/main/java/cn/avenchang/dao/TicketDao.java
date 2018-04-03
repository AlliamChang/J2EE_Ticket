package cn.avenchang.dao;

import cn.avenchang.domain.Ticket;
import cn.avenchang.model.SeatInfo;
import org.apache.ibatis.annotations.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by 53068 on 2018/3/30 0030.
 */
@Mapper
public interface TicketDao {

    @InsertProvider(type = TicketDaoProvider.class, method = "insertTickets")
    int insertTickets(@Param("orderId") Long orderId, @Param("tickets") List<Ticket> tickets);

    @Update("UPDATE ticket SET state = 2 WHERE order_id = #{orderId}")
    int refundTicketsByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT t.row, t.col, p.name, p.price" +
            " FROM ticket as t, plan_price as p" +
            " WHERE t.plan_id = p.plan_id AND t.area = p.area AND t.order_id = #{orderId}")
    List<SeatInfo> getSeatInfo(@Param("orderId") Long orderId);

    @Update("UPDATE ticket SET state = 1 WHERE id = #{id} AND state = 0")
    int checkIn(@Param("id") Long id);
    
    class TicketDaoProvider{

        public static String insertTickets(Map map) {
            StringBuilder sb = new StringBuilder();
            List<Ticket> tickets = (List<Ticket>) map.get("tickets"); 
            sb.append("INSERT INTO ticket(order_id, user_id, plan_id, venue_id, area, row, col, time, state, is_online) ");
            sb.append("VALUES ");
            MessageFormat mf =
                    new MessageFormat(
                            "( #'{'orderId}, " +
                                    "#'{'tickets[{0}].userId}, " +
                                    "#'{'tickets[{0}].planId}, " +
                                    "#'{'tickets[{0}].venueId}," +
                                    "#'{'tickets[{0}].area}, " +
                                    "#'{'tickets[{0}].row}," +
                                    "#'{'tickets[{0}].col}," +
                                    "#'{'tickets[{0}].time}," +
                                    "#'{'tickets[{0}].state}," +
                                    "#'{'tickets[{0}].isOnline} )"
                    );
            for (int i = 0; i < tickets.size(); i++) {
                sb.append(mf.format(new Object[]{i}));
                if (i < tickets.size() - 1) {
                    sb.append(", ");
                }
            }
            System.out.println(sb.toString());
            return sb.toString();
        }
    }
}
