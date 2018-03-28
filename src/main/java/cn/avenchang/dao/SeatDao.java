package cn.avenchang.dao;

import cn.avenchang.domain.Seat;
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
    int insertSeats(@Param("venueId")Long venueId, @Param("seat") List<Seat> seats);

    @Select("SELECT * FROM seat WHERE venue_id = #{venueId}")
    List<Seat> getSeatByVenueId(@Param("venueId") Long id);

    class SeatDaoProvider{
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
    }
}
