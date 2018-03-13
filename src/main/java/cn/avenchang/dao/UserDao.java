package cn.avenchang.dao;

import cn.avenchang.domain.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Mapper
public interface UserDao {

    @Select("SELECT * FROM user")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "email", column = "email")
            }
    )
    User findUser(@Param("id") Long id);
}
