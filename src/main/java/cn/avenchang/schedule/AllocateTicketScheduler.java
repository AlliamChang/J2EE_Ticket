package cn.avenchang.schedule;

import cn.avenchang.util.TicketSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
@Component
public class AllocateTicketScheduler {

    @Autowired
    private TicketSynchronizer ticketSynchronizer;

    @Scheduled(cron = "0 0 1 * * ? *")
    public void allocate() {
//        ticketSynchronizer.buyTicketWithoutSelect()
    }
}
