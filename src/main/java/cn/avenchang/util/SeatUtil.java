package cn.avenchang.util;

import cn.avenchang.domain.Seat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
public class SeatUtil {

    private static final String NULL_SEAT = "_";
    private static final String DEFAULT_SEAT = "a";

    public static List<String> getSeatMap(List<Seat> areas) {
        List<String> map = new ArrayList<>();
        Integer maxLength = 1;
        for (Seat area : areas) {
            if (area.getLength() > maxLength) {
                maxLength = area.getLength();
            }
        }

        String split = repeat(maxLength, NULL_SEAT);
        for (int i = 0; i < areas.size(); i++) {
            String seat = repeat(areas.get(i).getLength(), DEFAULT_SEAT);
            String align = areas.get(i).getAlign();
            if(areas.get(i).getLength() < maxLength) {
                if ("right".equals(align)) {
                    seat = repeat(maxLength - areas.get(i).getLength(), NULL_SEAT) + seat;
                } else if ("left".equals(align)) {
                    seat = seat + repeat(maxLength - areas.get(i).getLength(), NULL_SEAT) ;
                } else {
                    int left = (maxLength - areas.get(i).getLength()) / 2;
                    String fillerL = left == 0 ? "":repeat(left, NULL_SEAT) ;
                    String fillerR = (maxLength - areas.get(i).getLength() - left == 0) ? "":repeat(maxLength - areas.get(i).getLength() - left, NULL_SEAT);
                    seat = fillerL + seat + fillerR;
                }
            }
            for(int j = 0; j < areas.get(i).getWidth(); j ++) {
                map.add(seat);
            }
            if(i < areas.size() - 1){
                map.add(split);
            }
        }
        return map;
    }

    public static void getLiveSeatMap() {

    }

    public static String repeat(int n, String s) {
        return String.format("%0" + n + "d", 0).replace("0", s);
    }
}
