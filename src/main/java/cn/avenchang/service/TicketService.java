package cn.avenchang.service;

import cn.avenchang.domain.SeatState;

import java.util.List;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
public interface TicketService {

    void buyTicketOfflineWithoutUser(List<SeatState> seatStates);

    void buyTicketOfflineByUser(String email, List<SeatState> seatStates);

    void buyTicketByAuto();

    void buyTicketBySelect();

    void unSubscribe();

    void checkIn();


}
