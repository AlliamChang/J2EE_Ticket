package cn.avenchang.dao;

import cn.avenchang.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.executor.ExecutorException;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Mapper
public interface UserDao {

    @Select("SELECT * FROM user WHERE email = #{email}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "flag", column = "flag")
            }
    )
    User findUser(@Param("email") String email);

    @Select("SELECT * FROM user WHERE id = #{id} AND token = #{token}")
    @Results(
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "flag", column = "flag")
            }
    )
    User validToken(@Param("id") Long id, @Param("token") String token);


    @Insert("INSERT INTO user(email, password, token, verification, username, real_name, flag, regist_time, latest_time)" +
            " VALUE(#{email}, #{password}, '', #{verification}, #{username}, #{realName}, 0, now(), now())")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int register(User user);

    @SelectKey(statement = "select ifnull((select id from user where email = #{email}), 0) as id", keyProperty = "id", before = false, resultType = Long.class)
    @Update("UPDATE user SET token = #{token}, latest_time = now() WHERE email = #{email} AND password = #{password}")
    int updateToken(User user);

    @Update("UPDATE user SET token = '', latest_time = now() WHERE id = #{id} AND token = #{token}")
    int logout(@Param("id") Long id, @Param("token") String token);

    @Update("UPDATE user SET flag = 1 WHERE id = #{id} AND verification = #{verification}")
    int verify(@Param("id") Long id,
               @Param("verification") String verification);

    @Update("UPDATE user SET verification = #{verification}, flag = 2 WHERE id = #{id} AND token = #{token}")
    int delete(@Param("id") Long id,
               @Param("token") String token,
               @Param("verification") String verification);

    @Update("UPDATE user SET flag = 3, verification = '', token = '', password = '' " +
            "WHERE id = #{id} AND verification = #{verification} AND token = #{token} AND flag = 2")
    int confirmDelete(@Param("id") Long id,
                      @Param("token") String token,
                      @Param("verification") String verification);

    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results(
            value = {
                    @Result(column = "email", property = "email"),
                    @Result(column = "real_name", property = "realName"),
                    @Result(column = "username", property = "username"),
                    @Result(column = "rest_points", property = "restPoints"),
                    @Result(column = "total_points", property = "totalPoints"),
                    @Result(column = "regist_time", property = "registTime")
            }
    )
    User findUserByID(@Param("id") Long id);

    @Select("SELECT rest_points FROM user WHERE id = #{id}")
    int getRestPoints(@Param("id") Long id);

    @Select("SELECT total_points FROM user WHERE id = #{id}")
    int getTotalPoints(@Param("id") Long id);

    @Update("UPDATE user SET real_name = #{realName}, username = #{username} WHERE email = #{email} AND token = #{token}")
    int updateInfo(User user);
}
