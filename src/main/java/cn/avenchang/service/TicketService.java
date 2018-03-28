package cn.avenchang.service;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
public interface TicketService {

    void buyTicketOffline();

    void buyTicketOffline(String email);

    void buyTicketByAuto();

    void buyTicketBySelect();

    void unSubscribe();

    void checkIn();


}
