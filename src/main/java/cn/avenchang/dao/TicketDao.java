package cn.avenchang.dao;

import cn.avenchang.domain.Ticket;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 53068 on 2018/3/30 0030.
 */
@Mapper
public interface TicketDao {

    @InsertProvider(type = TicketDaoProvider.class, method = "insertTickets")
    int insertTickets(@Param("orderId") Long orderId, @Param("tickets") List<Ticket> tickets);

    class TicketDaoProvider{

        public static String insertTickets(Map map) {
            StringBuilder sb = new StringBuilder();
            return sb.toString();
        }
    }
}
