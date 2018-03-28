package cn.avenchang.dao;

import cn.avenchang.domain.Manager;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.executor.ExecutorException;

/**
 * Created by 53068 on 2018/3/27 0027.
 */
@Mapper
public interface ManagerDao {

    @Select("SELECT count(*) FROM manager WHERE id = #{id} AND token = #{token}")
    Integer validToken(@Param("id") Long id, @Param("token") String token);


    @SelectKey(statement = "select ifnull((select id from manager where username = #{username}), 0) as id", keyProperty = "id", before = false, resultType = Long.class)
    @Update("UPDATE manager SET token = #{token} WHERE username = #{username} AND password = #{password}")
    int updateToken(Manager manager);

    @Update("UPDATE manager SET token = '' WHERE id = #{id} AND token = #{token}")
    int logout(@Param("id") Long id, @Param("token") String token);
}
