package cn.avenchang.schedule;

import cn.avenchang.dao.OrdersDao;
import cn.avenchang.domain.Orders;
import cn.avenchang.util.TicketSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
@Component
public class AllocateTicketScheduler {

    @Autowired
    private TicketSynchronizer ticketSynchronizer;
    @Autowired
    private OrdersDao ordersDao;

//    @Scheduled(cron = "0 0 1 * * ? *")
    @Scheduled(fixedRate = 10000)
    public void allocate() {
        List<Orders> unselected = ordersDao.getUnselectedOrder();
        System.out.println(unselected.size());
        if (unselected != null) {
            unselected.forEach(orders -> {
                ticketSynchronizer.allocateTicket(orders);
            });
        }
//        ticketSynchronizer.buyTicketWithoutSelect()

    }
}
