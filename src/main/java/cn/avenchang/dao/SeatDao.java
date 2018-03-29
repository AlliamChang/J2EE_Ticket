package cn.avenchang.dao;

import cn.avenchang.domain.Seat;
import cn.avenchang.domain.SeatState;
import cn.avenchang.util.SeatUtil;
import org.apache.ibatis.annotations.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Mapper
public interface SeatDao {

    @InsertProvider(type = SeatDaoProvider.class, method = "insertSeat")
    int insertSeats(@Param("venueId") Long venueId, @Param("seat") List<Seat> seats);

    @Select("SELECT * FROM seat WHERE venue_id = #{venueId}")
    List<Seat> getSeatByVenueId(@Param("venueId") Long venueId);

    @Select("SELECT * " +
            "FROM seat as s, " +
            "       ( SELECT venue_id FROM plan WHERE id = #{planId}) as p " +
            "WHERE s.venue_id = p.venue_id ")
    List<Seat> getSeatByPlanId(@Param("planId") Long planId);

    @InsertProvider(type = SeatDaoProvider.class, method = "insertSeatStates")
    int insertSeatStates(@Param("planId") Long planId, @Param("seats") List<Seat> seats);

    @Update("UPDATE seat_state SET state = 1 " +
            "WHERE plan_id = #{planId} AND area = #{area} AND row = #{row} AND col = #{col} AND state = 0")
    int bookSeat(@Param("planId") Long planId, @Param("area") int area, @Param("row") int row, @Param("col") int col);

    @Select("SELECT * FROM seat_state WHERE plan_id = #{planId} AND state = 1")
    List<SeatState> getUnavailableSeats(@Param("planId") Long planId);

    @Select("SELECT count(*) FROM seat_state WHERE plan_id = #{planId} AND area = #{area} AND state = 0")
    int getAvailableSeatNumByArea(@Param("planId") Long planId, @Param("area") int area);

    @Select("SELECT count(*) FROM seat_state WHERE plan_id = #{planId} AND state = 0")
    int getAllAvailableSeatNum(@Param("planId") Long planId);

    class SeatDaoProvider {
        public static String insertSeat(Map map) {
            List<Seat> seats = (List<Seat>) map.get("seat");
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO seat(venue_id, area, length, width, align) ");
            sb.append("VALUES ");
            MessageFormat mf =
                    new MessageFormat(
                            "( #'{'venueId}, " +
                                    " {0}+1, " +
                                    "#'{'seat[{0}].length}, " +
                                    "#'{'seat[{0}].width}, " +
                                    "#'{'seat[{0}].align} )"
                    );
            for (int i = 0; i < seats.size(); i++) {
                sb.append(mf.format(new Object[]{i}));
                if (i < seats.size() - 1) {
                    sb.append(", ");
                }
            }
            System.out.println(sb.toString());
            return sb.toString();
        }

        public static String insertSeatStates(Map map) {
            List<Seat> seats = (List<Seat>) map.get("seats");
            Long planId = (Long) map.get("planId");
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO seat_state(plan_id, area, row, col) ");
            sb.append("VALUES ");

            int row = 1;
            int maxLength = SeatUtil.maxLength(seats);
            for (int i = 0; i < seats.size(); i++) {
                Seat seat = seats.get(i);
                for (int j = 0; j < seat.getWidth(); j++) {
                    for (int k = 0; k < seat.getLength(); k++) {
                        int col = 1;
                        if (seat.getLength() == maxLength) {
                            col = k + 1;
                        } else {
                            if ("right".equals(seat.getAlign())) {
                                col = (maxLength - seat.getLength()) + k + 1;
                            } else if ("center".equals(seat.getAlign())) {
                                col = (maxLength - seat.getLength())/2 + k + 1;
                            } else {
                                col = k + 1;
                            }
                        }

                        sb.append("(" + planId + ", " + seat.getArea() + ", " + row + ", " + col + ")");
                        if (i == seats.size() - 1 && j == seat.getWidth() - 1 && k == seat.getLength() - 1) {
                            //最后一个不添加分隔符
                        }else {
                            sb.append(",");
                        }
                    }
                    row++;
                }
                row++;
            }
            System.out.println(sb.toString());
            return sb.toString();
        }
    }

}
