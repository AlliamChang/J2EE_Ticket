package cn.avenchang.model;

/**
 * Created by 53068 on 2018/3/27 0027.
 */
public class ValidInfo {

    private Long id;
    private String token;

    public ValidInfo(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
