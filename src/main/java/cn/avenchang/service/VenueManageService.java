package cn.avenchang.service;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
public interface VenueManageService {

    void updateInfo(String id, String token, String updateInfo);

    void releasePlan();

    void updateSeat();


}
