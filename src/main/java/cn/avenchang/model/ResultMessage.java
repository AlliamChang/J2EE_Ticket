package cn.avenchang.model;

import org.springframework.http.HttpStatus;

/**
 * Created by 53068 on 2018/3/13 0013.
 */
public class ResultMessage<T> {

    public static final int OK = 1;
    public static final int FAIL = 2;
    public static final int UNKNOWN = 3;

    public int status;
    public T result;
    public String message;

    public ResultMessage(int status, T result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public ResultMessage(int status, T result) {
        this(status, result, "");
    }

    public ResultMessage(int status,String message) {
        this(status, null, message);
    }
}
