package cn.avenchang.dao;

import cn.avenchang.domain.Plan;
import cn.avenchang.domain.PlanPrice;
import cn.avenchang.model.PlanInfo;
import org.apache.ibatis.annotations.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
@Mapper
public interface PlanDao {

    @Select("SELECT p.id, p.title, p.venue_id, p.lowest_price, p.time, p.type, p.description, p.venue_name, v.location " +
            "FROM plan as p, venue as v WHERE p.id = #{id} AND p.venue_id = v.id")
    @Results({
            @Result(id = true, column = "id", property = "id"),
//            @Result(column = "venue_id", property = "venueId"),
            @Result(column = "title", property = "title"),
            @Result(column = "lowest_price", property = "lowestPrice"),
            @Result(column = "time", property = "time"),
            @Result(column = "type", property = "type"),
            @Result(column = "description", property = "description"),
            @Result(column = "venue_name", property = "venueName"),
            @Result(column = "location", property = "venueLocation")
    })
    PlanInfo getPlanById(@Param("id") Long id);

    @Select("SELECT * FROM plan WHERE time > now()")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "title", property = "title"),
            @Result(column = "lowest_price", property = "lowestPrice"),
            @Result(column = "time", property = "time"),
            @Result(column = "type", property = "type"),
            @Result(column = "venue_name", property = "venueName")
    })
    List<Plan> getAllPlan();

    @Select("SELECT * FROM plan WHERE venue_id = #{venueId} AND time > now()")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "title", property = "title"),
            @Result(column = "lowest_price", property = "lowestPrice"),
            @Result(column = "time", property = "time"),
            @Result(column = "type", property = "type")
    })
    List<Plan> getVenuePlan(@Param("venueId") Long venueId);

    @Select("SELECT * FROM plan_price WHERE plan_id = #{planId}")
    List<PlanPrice> getPlanPrice(@Param("planId") Long id);

    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    @Insert("INSERT INTO plan(title, venue_id, venue_name, time, description, type, lowest_price) " +
            "VALUE " +
            "(#{title}, #{venueId}, #{venueName}, #{time}, #{description}, #{type}, #{lowestPrice})")
    int insertPlan(Plan plan);

    @InsertProvider(type = PlanDaoProvider.class, method = "insertPlanPrice")
    int insertPlanPrice(@Param("id") Long id, @Param("planPrices") List<PlanPrice> planPrices);

    class PlanDaoProvider {

        public static String insertPlanPrice(Map map) {
            List<PlanPrice> planPrices = (List<PlanPrice>) map.get("planPrices");
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO plan_price(plan_id, name, area, price) ");
            sb.append("VALUES ");
            MessageFormat mf =
                    new MessageFormat(
                            "( #'{'id}, " +
                                    "#'{'planPrices[{0}].name}, " +
                                    "#'{'planPrices[{0}].area}, " +
                                    "#'{'planPrices[{0}].price} )"
                    );
            for (int i = 0; i < planPrices.size(); i++) {
                sb.append(mf.format(new Object[]{i}));
                if (i < planPrices.size() - 1) {
                    sb.append(", ");
                }
            }
            System.out.println(sb.toString());
            return sb.toString();
        }
    }
}
