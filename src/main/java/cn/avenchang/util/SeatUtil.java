package cn.avenchang.util;

import cn.avenchang.domain.PlanPrice;
import cn.avenchang.domain.Seat;
import cn.avenchang.domain.SeatState;
import cn.avenchang.model.LiveSeatMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
public class SeatUtil {

    private static final String NULL_SEAT = "_";
    private static final String DEFAULT_SEAT = "a";

    public static List<String> getSeatMap(List<Seat> areas) {
        List<String> map = new ArrayList<>();
        Integer maxLength = maxLength(areas);

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

    public static LiveSeatMap getLiveSeatMap(List<Seat> areas, List<PlanPrice> price, List<SeatState> unavailable) {
        LiveSeatMap liveSeatMap = new LiveSeatMap();
        Map<Integer, Double> areaToPrice = new HashMap();
        Map<Integer, String> areaToName = new HashMap<>();
        int maxLength = maxLength(areas);

        List<Double> priceLevel = new ArrayList<>();
        //build seat legend and price
        price.forEach(planPrice -> {
            if(priceLevel.indexOf(planPrice.getPrice()) < 0){
                liveSeatMap.getSeatLegend().add(new String[]{
                        String.valueOf((char) ('a'+priceLevel.size())),
                        "available",
                        planPrice.getName()+"(¥"+planPrice.getPrice()+")"
                });
                liveSeatMap.getSeatPrice().add(new Object[]{
                        planPrice.getPrice(),
                        planPrice.getName(),
                        planPrice.getArea()
                });
                priceLevel.add(planPrice.getPrice());
            }
            areaToPrice.put(planPrice.getArea(), planPrice.getPrice());
        });
        liveSeatMap.getSeatLegend().add(new String[]{"a", "unavailable", "不可选"});

        //build seat map
        String split = repeat(maxLength, NULL_SEAT);
        for (int i = 0; i < areas.size(); i++) {
            String seat = repeat(areas.get(i).getLength(),
                    String.valueOf(
                            (char)('a'+priceLevel.indexOf(areaToPrice.get(areas.get(i).getArea()) ) )
                    )
            );
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
                liveSeatMap.getSeatMap().add(seat);
            }
            if(i < areas.size() - 1){
                liveSeatMap.getSeatMap().add(split);
            }
        }

        //build seat unavailable
        unavailable.forEach(seatState -> {
            String ua = seatState.getRow() + "_" + seatState.getCol();
            liveSeatMap.getSeatUnavailable().add(ua);
        });

        return liveSeatMap;
    }

    public static int maxLength(List<Seat> areas) {
        Integer maxLength = 1;
        for (Seat area : areas) {
            if (area.getLength() > maxLength) {
                maxLength = area.getLength();
            }
        }
        return maxLength;
    }

    public static String repeat(int n, String s) {
        return String.format("%0" + n + "d", 0).replace("0", s);
    }
}
