package cn.avenchang;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by 53068 on 2018/3/25 0025.
 */
public class Test {

    public static void main(String[] args) {
        final Test myt2 = new Test();
        Thread test1 = new Thread(  new Runnable() {  public void run() {  myt2.test1();  }  }, "test1"  );
        Thread test2 = new Thread(  new Runnable() {  public void run() { myt2.test2();   }  }, "test2"  );
        test1.start();;
        test2.start();
    }

    public void test1()
    {
        synchronized(this)
        {
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

    public void test2()
    {
        synchronized(this)
        {
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
}
