package cn.avenchang;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by 53068 on 2018/3/25 0025.
 */
public class Test {

    public static void main(String[] args){
        MessageFormat mf = new MessageFormat("(#'{'seats[{0}].venueId}, #'{'seats[{0}].area}, #'{'seats[{0}].length}, )");
//        System.out.println(mf.format());
    }
}
