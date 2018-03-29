package cn.avenchang.domain;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
public class Account {

    private Long id;
    private String password;
    private Double balane;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalane() {
        return balane;
    }

    public void setBalane(Double balane) {
        this.balane = balane;
    }
}
