package com.slljr.finance.front.utils;

import com.slljr.finance.common.enums.PaymentChannelTypeEnum;
import com.slljr.finance.common.enums.PaymentStatusEnum;
import com.slljr.finance.common.enums.PaymentTypeEnum;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.pojo.model.PaymentChannelBank;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.utils.DateUtil;
import com.slljr.finance.common.utils.MathUtils;

import java.math.BigDecimal;
import java.util.*;

public class BillUtils {
    /**
     * 获取可支付日期集合
     * @param lastPayDate
     * @return
     */
    public static List<Date> getMaxPayDates(Date lastPayDate){
        List<Date> list = new ArrayList<>();
        Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Calendar lastPayCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

        //计算时不算今日
        todayCalendar.add(Calendar.DAY_OF_MONTH, 1);
        //设置还款日
        lastPayCalendar.setTime(lastPayDate);

        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        lastPayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        lastPayCalendar.set(Calendar.MINUTE, 0);
        lastPayCalendar.set(Calendar.SECOND, 0);
        lastPayCalendar.set(Calendar.MILLISECOND, 0);

        while (todayCalendar.getTimeInMillis() <= lastPayCalendar.getTimeInMillis()){
            list.add(todayCalendar.getTime());
            todayCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    /**
     * 获取卡内最低余额(每期金额,最高刷80%)
     * @param billAmount
     * @param totalPeriod
     * @param channel
     * @return
     */
    private static Double getCardBalanceMin(double billAmount, int totalPeriod, PaymentChannel channel){
        //支付手续费
        double totalPayServiceCharge = MathUtils.mul(billAmount, channel.getUserPaymentRate(), 2);
        //提现手续费
        double totalRepayServiceCharge = MathUtils.mul(totalPeriod , channel.getUserWithdrawCharge(), 2);
        //实际支付金额
        double totalPayAmount = MathUtils.add(billAmount, totalPayServiceCharge + totalRepayServiceCharge , 2);
        //向上取整
        totalPayAmount = MathUtils.round(totalPayAmount, BigDecimal.ROUND_UP, 0);

        //计算最低余额
        double minBalance = MathUtils.div(totalPayAmount, totalPeriod, 2);
        //每期最高刷80%
        minBalance = MathUtils.div(minBalance, 0.8, 0);
        return minBalance;
    }

    /**
     * 获取单笔最大金额
     * @param balance
     * @param channelBank
     * @return
     */
    private static Double getSingleAmountMax(double balance, PaymentChannelBank channelBank){
        //默认单笔最大限额1000
        double singleAmountMax = 1000;
        //通道银行不为空,设置为通道银行单笔限额
        if (channelBank != null) singleAmountMax = channelBank.getSingleLimit();
        //单笔最大限额大于卡余额,则最大限额为卡余额
        if (singleAmountMax > balance) singleAmountMax = balance;

        return singleAmountMax;
    }

    /**
     * 获取单期最大金额
     * @param balance
     * @param channelBank
     * @return
     */
    private static Double getPeriodAmountMax(double balance, PaymentChannelBank channelBank){
        //默认单期最大限额3000
        double periodAmountMax = 1000 * 3;
        if (channelBank != null) periodAmountMax = channelBank.getSingleLimit() * 3;
        if (periodAmountMax > balance) periodAmountMax = balance;

        return periodAmountMax;
    }

    /**
     * 获取单日最大金额
     * @param balance
     * @param channelBank
     * @return
     */
    private static Double getDayAmountMax(double balance, PaymentChannelBank channelBank){
        double dayAmountMax = 1000 * 9;
        if (channelBank != null) dayAmountMax = channelBank.getSingleDayLimit();
        if (dayAmountMax > balance * 3) dayAmountMax = balance * 3;

        return dayAmountMax;
    }

    /**
     * 支付日期过滤(过滤最后还款日)
     * @param payDates
     * @param dayAmountMax
     * @param billAmount
     */
    private static void payDateFilter(List<Date> payDates, double dayAmountMax, double billAmount){
        if (dayAmountMax * payDates.size() > (billAmount + dayAmountMax * 2)){
            payDates.remove(payDates.size()-1);
        }
    }

    /**
     * 账单金额拆分成期数(未包含手续费,期数金额为实际还款金额,扣款时应加手续费)
     * @param billParams
     * @return
     */
    public static List<Double> getPeriodAmounts(BillParams billParams){
        List<Double> periodAmounts = new ArrayList<>();

        //待拆分账单金额
        double billBalance = billParams.getBillAmount();
        //循环拆分
        while (billBalance > 0){
            //随机还款剩余金额的比例75%-85%
            double ratio = MathUtils.randomBetween(0.75, 0.85);
            //每期应还金额(整数)
            double periodAmount = MathUtils.mul(billParams.getPeriodAmountMax(), ratio, 0);
            //最后一笔
            if (periodAmount > billBalance) periodAmount = billBalance;

            billBalance -= periodAmount;
            periodAmounts.add(periodAmount);
        }

        return periodAmounts;
    }

    /**
     * 把期数分配到支付日期
     * @param billParams
     * @param periodAmounts
     * @return
     */
    public static Map<String, List<Double>> getDatePayPeriodAmount(BillParams billParams, List<Double> periodAmounts){
        Map<String, List<Double>> payDayPeriodMap = new LinkedHashMap<>();

        //可支付日期集合
        List<Date> canPayDates = billParams.getCanPayDates();
        //由期数求出最小还款天数
        int minPayDays = periodAmounts.size() % 3 == 0 ? periodAmounts.size() / 3 : periodAmounts.size() / 3 + 1;
        //最大还款天数
        int maxPayDays = billParams.getCanPayDates().size();
        System.out.println(String.format("还款期数%s,最小还款天数%s,最大还款天数%s", periodAmounts.size(), minPayDays, maxPayDays));


        //当天18点前设置代偿,就近分配一期,增加用户体验感,注意:只能分配一期
        String todayKey = DateUtil.DateToString(new Date(), "yyyy-MM-dd");
        if (DateUtil.DateToString(canPayDates.get(0), "yyyy-MM-dd").equals(todayKey)){
            //取出期数金额
            Double amount = periodAmounts.remove(0);
            //不存在则创建
            if (!payDayPeriodMap.containsKey(todayKey)) payDayPeriodMap.put(todayKey, new ArrayList<>());
            //把期数金额放入该还款日
            payDayPeriodMap.get(todayKey).add(amount);
            //该还款日期从待分配集合上移除
            canPayDates.remove(0);
        }

        while (!periodAmounts.isEmpty()){
            //取出期数金额
            Double amount = periodAmounts.remove(0);
            //随机取出支付日期
            Date payDate = canPayDates.get(MathUtils.randomBetween(0, canPayDates.size()-1));
            //支付日期key
            String dateKey = DateUtil.DateToString(payDate, "yyyy-MM-dd");
            //不存在则创建
            if (!payDayPeriodMap.containsKey(dateKey)) payDayPeriodMap.put(dateKey, new ArrayList<>());
            //把期数金额放入该还款日
            payDayPeriodMap.get(dateKey).add(amount);
            //该还款日期数达到3则从待分配集合上移除
            if (payDayPeriodMap.get(dateKey).size() == 3) canPayDates.remove(payDate);
        }

        return payDayPeriodMap;
    }

    /**
     * 生成扣款记录
     * @param amount
     * @param payDate
     * @param hour
     * @param billParams
     * @param last
     * @return
     */
    private static UserTradePaymentRecord toPayRecord(Double amount, Date payDate, int hour, boolean last, BillParams billParams){
        Calendar payCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        payCld.setTime(payDate);
        payCld.set(Calendar.HOUR_OF_DAY, hour);
        payCld.set(Calendar.MINUTE, randomBetween(0, 59));
        payCld.set(Calendar.SECOND, randomBetween(0, 59));

        if(last){
            //每期最后一笔扣款,加上提现手续费
            amount += billParams.getChannel().getUserWithdrawCharge();
            //扣款分钟数保证在0-20,给还款预留时间.防止还款时间跨到下一期造成支付顺序错乱
            payCld.set(Calendar.MINUTE, randomBetween(0, 20));
        }

        //公式: 实际支付金额=金额/(1-刷卡费率)
        //刷卡金额精确到角,每笔刷卡用户可能多支付几分钱
        double payAmount = MathUtils.round(amount/(1-billParams.getChannel().getUserPaymentRate()), BigDecimal.ROUND_UP, 1);
        double serviceCharge = MathUtils.round(payAmount * billParams.getChannel().getUserPaymentRate(), BigDecimal.ROUND_UP, 2);

        UserTradePaymentRecord payRecord = new UserTradePaymentRecord();
        payRecord.setUid(billParams.getOrder().getUid());
        payRecord.setTradeId(billParams.getOrder().getId());
        payRecord.setAmount(payAmount);
        payRecord.setServiceCharge(serviceCharge);
        payRecord.setCardId(billParams.getOrder().getPaymentCardId());
        payRecord.setPaymentTime(payCld.getTime());
        payRecord.setChannelId(billParams.getOrder().getChannelId());
        payRecord.setType(PaymentTypeEnum.PAYMENT.getKey());
        payRecord.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
        payRecord.setCreateTime(new Date());
        payRecord.setUpdateTime(new Date());

        return payRecord;
    }

    /**
     * 生成还款记录
     * @param periodAmount
     * @param lastPayCld
     * @param billParams
     * @return
     */
    private static UserTradePaymentRecord toRepayRecord(Double periodAmount, Calendar lastPayCld, BillParams billParams){
        //生成还款时间,在最后一笔支付时间延迟10-30分钟,且不能超过21:15
        lastPayCld.add(Calendar.MINUTE, randomBetween(20, 30));
        lastPayCld.set(Calendar.SECOND, randomBetween(0, 59));
        if (lastPayCld.get(Calendar.HOUR_OF_DAY) == 21) lastPayCld.set(Calendar.MINUTE, randomBetween(1, 15));

        UserTradePaymentRecord rePayRecord = new UserTradePaymentRecord();
        rePayRecord.setUid(billParams.getOrder().getUid());
        rePayRecord.setTradeId(billParams.getOrder().getId());
        rePayRecord.setAmount(periodAmount);
        rePayRecord.setServiceCharge(billParams.getChannel().getUserWithdrawCharge());
        rePayRecord.setCardId(billParams.getOrder().getReceiveCardId());
        rePayRecord.setPaymentTime(lastPayCld.getTime());
        rePayRecord.setChannelId(billParams.getOrder().getChannelId());
        rePayRecord.setType(PaymentTypeEnum.REPAYMENT.getKey());
        rePayRecord.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
        rePayRecord.setCreateTime(new Date());
        rePayRecord.setUpdateTime(new Date());

        return rePayRecord;
    }

    /**
     * 获取某日支付记录
     * @param payDateStr
     * @param periodAmounts
     * @param billParams
     * @return
     */
    public static List<UserTradePaymentRecord> getDatePayRecord(String payDateStr, List<Double> periodAmounts, BillParams billParams){
        List<UserTradePaymentRecord> data = new ArrayList<>();
        Date payDate = DateUtil.StringToDate(payDateStr);
        Calendar todayCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        //期数消费开始时间,结束时间为开始时间+3
        List<Integer> startTimes = new ArrayList<>(Arrays.asList(10,14,18));
        for(int periodNum = 0; periodNum < periodAmounts.size(); periodNum ++) {
            List<UserTradePaymentRecord> list = new ArrayList<>();
            //本期金额
            Double periodAmount = periodAmounts.get(periodNum);
            //本期剩余待还金额
            Double periodBalance = periodAmounts.get(periodNum);
            //本期刷卡开始时间小时
            int startTimeHour = startTimes.remove(randomBetween(0, startTimes.size() - 1));
            //当天18点前设置代偿,就近分配一期,增加用户体验感
            if (payDateStr.equals(DateUtil.DateToString(todayCld.getTime(), "yyyy-MM-dd"))){
                int nowHour = todayCld.get(Calendar.HOUR_OF_DAY);
                if (nowHour < 10){startTimeHour = 10;}
                else if(nowHour < 14){startTimeHour = 14;}
                else if(nowHour < 18){startTimeHour = 18;}
            }

            //10,11,12,13
            //14,15,16,17
            //18,19,20
            List<Integer> timeHours = null;
            if (startTimeHour == 18) {
                timeHours = new ArrayList<>(Arrays.asList(startTimeHour, startTimeHour + 1, startTimeHour + 2));
            } else {
                timeHours = new ArrayList<>(Arrays.asList(startTimeHour, startTimeHour + 1, startTimeHour + 2, startTimeHour + 3));
            }

            //计算本次刷卡最低次数
            int minSplitCount = 1;
            //每笔交易是否需要金额更小?如果需要,则大于100就至少拆分两次
            if (periodBalance >= billParams.getSingleAmountMax() * 2){
                minSplitCount = 3;
            }else if (periodBalance >= billParams.getSingleAmountMax() * 1 || periodAmount > 500){
                minSplitCount = 2;
            }

            //随机本期刷卡次数
            int splitCount = randomBetween(minSplitCount, 3);
            //随机拆分本期金额
            for(int index = 0; index < splitCount; index ++) {
                //计算本次随机金额最小值
                Double randomAmountMin = MathUtils.div(periodBalance, splitCount - index, 2);
                //计算本次随机金额最大值
                Double randomAmountMax = randomAmountMin * 1.4 > billParams.getSingleAmountMax() ? billParams.getSingleAmountMax() : randomAmountMin * 1.4;
                //随机本次支付金额(还款额取整,扣款时加上手续费)
                Double randomAmount = randomBetween(randomAmountMin, randomAmountMax);

                //剩余金额小于50,则一次性支付完成
                if (periodBalance - randomAmount < 50) {
                    if (periodBalance - randomAmount == 0 || periodBalance < billParams.getSingleAmountMax()) {
                        randomAmount = periodBalance;
                    } else {
                        randomAmount = MathUtils.div(periodBalance, 2, 0);
                    }
                }
                randomAmount = Double.valueOf(randomAmount.intValue());

                periodBalance = MathUtils.sub(periodBalance, randomAmount, 2);
                //生成随机消费时间
                int hour = timeHours.remove(randomBetween(0, timeHours.size()-1));

                //生成扣款记录
                UserTradePaymentRecord payRecord = toPayRecord(randomAmount, payDate, hour, periodBalance == 0, billParams);
                list.add(payRecord);

                //生成还款记录
                if (periodBalance == 0){
                    //取最后一笔扣款时间
                    Collections.sort(list, Comparator.comparing(UserTradePaymentRecord::getPaymentTime));
                    Calendar lastPayCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                    lastPayCld.setTime(list.get(list.size()-1).getPaymentTime());

                    UserTradePaymentRecord repayRecord = toRepayRecord(periodAmount, lastPayCld, billParams);
                    list.add(repayRecord);
                    break;
                }
            }

            data.addAll(list);
        }

        return data;
    }

    public static BillParams initBillParams(UserTradeOrder order, PaymentChannel channel, PaymentChannelBank channelBank){
        BillParams params = new BillParams(order, channel);
        //账单金额
        params.setBillAmount(order.getPaymentAmount());
        //内卡余额
        params.setCardBalance(order.getCardBalance());
        //可支付日期集合,计算限额时,不计算今日
        params.setCanPayDates(getMaxPayDates(order.getCardLastPaymentDate()));

        //卡内最低余额
        params.setCardBalanceMin(getCardBalanceMin(params.getBillAmount(), params.getCanPayDates().size() * 3, channel));
        //单笔最小金额
        params.setSingleAmountMin(MathUtils.div(params.getCardBalanceMin(), 3, 2));

        //单笔最大金额
        params.setSingleAmountMax(getSingleAmountMax(params.getCardBalance(), channelBank));
        //单期最大金额
        params.setPeriodAmountMax(getPeriodAmountMax(params.getCardBalance(), channelBank));
        //单日最大金额
        params.setDayAmountMax(getDayAmountMax(params.getCardBalance(), channelBank));

        //如果还款时间充裕,则不在最后还款日还款
        payDateFilter(params.getCanPayDates(), params.getDayAmountMax(), params.getBillAmount());

        //当天18点前设置代偿,就近分配一期,增加用户体验感
        Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        if (todayCalendar.get(Calendar.HOUR_OF_DAY) < 18){
            params.getCanPayDates().add(0, todayCalendar.getTime());
        }
       return params;
    }

    /**
     * 获取单笔支付最低金额
     * @param tradeOrder
     * @return
     */
    public static Double getSingleAmountMin(UserTradeOrder tradeOrder){
        Double paymentAmount = tradeOrder.getPaymentAmount();
        if (tradeOrder.getType() == PaymentChannelTypeEnum.DAIHUAN.getKey()){
            //还款日数量
            int maxDayCount = getMaxPayDates(tradeOrder.getCardLastPaymentDate()).size();
            //支付手续费
            double totalPayServiceCharge = MathUtils.mul(paymentAmount, 0.008, 2);
            //提现手续费
            double totalRepayServiceCharge = MathUtils.mul(maxDayCount * 3 , 1, 2);
            //实际支付金额
            double totalPayAmount = MathUtils.add(paymentAmount, totalPayServiceCharge + totalRepayServiceCharge , 2);
            //单笔最低金额
            Double singleAmount = MathUtils.div(totalPayAmount, maxDayCount * 9, 2);

            return singleAmount;
        }
        return paymentAmount;
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
