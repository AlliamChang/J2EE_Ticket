package cn.avenchang;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int machineNum = in.nextInt();
        int taskNum = in.nextInt();
        long[][] machine = new long[machineNum][3];
        long[][] task = new long[taskNum][3];

        for (int i = 0; i < machineNum; i++) {
            machine[i][0] = in.nextLong();
            machine[i][1] = in.nextLong();
            machine[i][2] = machine[i][0] * 100 + machine[i][1] * 3;
        }
        for (int i = 0; i < taskNum; i++) {
            task[i][0] = in.nextLong();
            task[i][1] = in.nextLong();
            task[i][2] = task[i][0] * 100 + task[i][1] * 3;
        }

        for (int i = 0; i < machine.length; i++) {
            for (int j = 1; j < machine.length - i; j ++) {
                if(machine[j][2] < machine[j-1][2]){
                    long[] temp = machine[j];
                    machine[j] = machine[j-1];
                    machine[j - 1] = machine[j];
                }
            }
        }

        for (int i = 0; i < task.length; i++) {
            for (int j = 1; j < task.length - i; j ++) {
                if(task[j][2] < task[j-1][2]){
                    long[] temp = task[j];
                    task[j] = task[j-1];
                    task[j - 1] = task[j];
                }
            }
        }

        int finished = 0;
        long earning = 0;

        for (int i = machine.length - 1; i >= 0; i--) {
            for (int j = task.length - 1; j >= 0; j--) {
                if (machine[i][0] >= task[j][0] && machine[i][1] >= task[j][1]) {

                }
            }
        }

        //公式：task[i][0] * 100 + task[i][1] * 3

        System.out.println(finished + " " + earning);
    }
//    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        long length = in.nextLong();
//        long lengthA = in.nextLong();
//        long numA = in.nextLong();
//        long lengthB = in.nextLong();
//        long numB = in.nextLong();
//        long method = 0;
//
//        long tryA = 0;
//        while (tryA * lengthA <= length && tryA <= numA) {
//            long rest = length - tryA * lengthA;
//            if (rest % lengthB == 0) {
//                long tryB = rest / lengthB;
//                if(tryB <= numB) {
//                    method += ((tryA == 0) ? 1 : arrangement(numA, tryA))
//                            * ((tryB == 0) ? 1 : arrangement(numB, tryB));
//                }
//            }
//
//            tryA ++;
//        }
//
//        System.out.println(Math.floorMod(method, 1000000007));
//    }

    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     * @param n
     * @return
     */
    private static long factorial(long n) {
        if (n <= 1) {
            return 1;
        }
        long result = 1;
        for (long l = n; l > 0; l--) {
            result *= l;
        }
        return result;
//        return (n > 1) ? n * factorial(n - 1) : 1;
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     * @param n
     * @param m
     * @return
     */
    public static long arrangement(long n, long m) {
        System.out.println(n + " " + m);
        System.out.println(factorial(n - m));
        return (n >= m) ? factorial(n) / factorial(n - m) : 0;
    }
}
