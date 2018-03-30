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
        final Test test = new Test();
        new Thread(
                () -> {
                    test.test();
                }, "test1"
        ).start();
        new Thread(
                () -> {
                    test.test();
                }, "test2"
        ).start();
    }

    public void test() {
        int i = 5;
        while( i-- > 0)
        {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException ie)
            {
            }
        }
    }
}
