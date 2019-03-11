package com.slljr.finance.common.utils;

import io.netty.util.internal.MathUtil;

import java.math.BigDecimal;
import java.time.Period;
import java.util.Random;

/**
 * 算数运算
 *
 * @Auth Created by guoqun.yang
 * @Date Created in 11:22 2017/11/13
 * @Version 1.0
 */
public class MathUtils {
    /**
     * 精确的加法运算
     *
     * @param v1        被加数
     * @param v2        加数
     * @param precision
     * @return 两个参数的和
     * @Author: guoqun.yang
     * @Date: 2018/1/18 14:22
     * @version 1.0
     */
    public static double add(double v1, double v2, int precision) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确的减法运算
     *
     * @param v1        被减数
     * @param v2        减数
     * @param precision 表示表示需要精确到小数点以后几位
     * @return 两个参数的差
     * @Author: guoqun.yang
     * @Date: 2018/1/18 14:22
     * @version 1.0
     */
    public static double sub(double v1, double v2, int precision) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 减法运算
     *
     * @author uncle.quentin
     * @date   2018/12/13 17:05
     * @param   v1 被减数
     * @param   v2 减数
     * @return double
     * @version 1.0
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确的乘法运算
     *
     * @param v1        被乘数
     * @param v2        乘数
     * @param precision
     * @return 两个参数的积
     * @Author: guoqun.yang
     * @Date: 2018/1/18 14:22
     * @version 1.0
     */
    public static double mul(double v1, double v2, int precision) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * （相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1        被除数
     * @param v2        除数
     * @param precision 表示表示需要精确到小数点以后几位
     * @return 两个参数的商
     * @Author: guoqun.yang
     * @Date: 2018/1/18 14:22
     * @version 1.0
     */
    public static double div(double v1, double v2, int precision) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确的小数位四舍五入处理
     *
     * @param v         需要格式化的数字
     * @param type      类型[BigDecimal.ROUND_UP/ROUND_HALF_UP...]
     * @param precision 小数点后保留几位
     * @Author: guoqun.yang
     * @Date: 2018/1/18 14:25
     * @version 1.0
     */
    public static double round(double v, int type, int precision) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, precision, type).doubleValue();
    }

    static Random random = new Random();
    /**
     * 随机生成>=min,<=max的数
     * @param min
     * @param max
     * @return
     */
    public static int randomBetween(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }
    public static double randomBetween(double min, double max){
        int imin = Double.valueOf(min * 100).intValue();
        int imax = Double.valueOf(max * 100).intValue();
        int res = random.nextInt(imax - imin + 1) + imin;
        return MathUtils.div(res, 100, 2);
    }

}
