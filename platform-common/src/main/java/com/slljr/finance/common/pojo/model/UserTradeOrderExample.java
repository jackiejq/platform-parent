package com.slljr.finance.common.pojo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UserTradeOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserTradeOrderExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Integer value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Integer value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Integer value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Integer value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Integer value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Integer value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Integer> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Integer> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Integer value1, Integer value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Integer value1, Integer value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andOidIsNull() {
            addCriterion("oid is null");
            return (Criteria) this;
        }

        public Criteria andOidIsNotNull() {
            addCriterion("oid is not null");
            return (Criteria) this;
        }

        public Criteria andOidEqualTo(Integer value) {
            addCriterion("oid =", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidNotEqualTo(Integer value) {
            addCriterion("oid <>", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidGreaterThan(Integer value) {
            addCriterion("oid >", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidGreaterThanOrEqualTo(Integer value) {
            addCriterion("oid >=", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidLessThan(Integer value) {
            addCriterion("oid <", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidLessThanOrEqualTo(Integer value) {
            addCriterion("oid <=", value, "oid");
            return (Criteria) this;
        }

        public Criteria andOidIn(List<Integer> values) {
            addCriterion("oid in", values, "oid");
            return (Criteria) this;
        }

        public Criteria andOidNotIn(List<Integer> values) {
            addCriterion("oid not in", values, "oid");
            return (Criteria) this;
        }

        public Criteria andOidBetween(Integer value1, Integer value2) {
            addCriterion("oid between", value1, value2, "oid");
            return (Criteria) this;
        }

        public Criteria andOidNotBetween(Integer value1, Integer value2) {
            addCriterion("oid not between", value1, value2, "oid");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountIsNull() {
            addCriterion("payment_amount is null");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountIsNotNull() {
            addCriterion("payment_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountEqualTo(Double value) {
            addCriterion("payment_amount =", value, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountNotEqualTo(Double value) {
            addCriterion("payment_amount <>", value, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountGreaterThan(Double value) {
            addCriterion("payment_amount >", value, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountGreaterThanOrEqualTo(Double value) {
            addCriterion("payment_amount >=", value, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountLessThan(Double value) {
            addCriterion("payment_amount <", value, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountLessThanOrEqualTo(Double value) {
            addCriterion("payment_amount <=", value, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountIn(List<Double> values) {
            addCriterion("payment_amount in", values, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountNotIn(List<Double> values) {
            addCriterion("payment_amount not in", values, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountBetween(Double value1, Double value2) {
            addCriterion("payment_amount between", value1, value2, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andPaymentAmountNotBetween(Double value1, Double value2) {
            addCriterion("payment_amount not between", value1, value2, "paymentAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountIsNull() {
            addCriterion("receive_amount is null");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountIsNotNull() {
            addCriterion("receive_amount is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountEqualTo(Double value) {
            addCriterion("receive_amount =", value, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountNotEqualTo(Double value) {
            addCriterion("receive_amount <>", value, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountGreaterThan(Double value) {
            addCriterion("receive_amount >", value, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountGreaterThanOrEqualTo(Double value) {
            addCriterion("receive_amount >=", value, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountLessThan(Double value) {
            addCriterion("receive_amount <", value, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountLessThanOrEqualTo(Double value) {
            addCriterion("receive_amount <=", value, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountIn(List<Double> values) {
            addCriterion("receive_amount in", values, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountNotIn(List<Double> values) {
            addCriterion("receive_amount not in", values, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountBetween(Double value1, Double value2) {
            addCriterion("receive_amount between", value1, value2, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andReceiveAmountNotBetween(Double value1, Double value2) {
            addCriterion("receive_amount not between", value1, value2, "receiveAmount");
            return (Criteria) this;
        }

        public Criteria andServiceChargeIsNull() {
            addCriterion("service_charge is null");
            return (Criteria) this;
        }

        public Criteria andServiceChargeIsNotNull() {
            addCriterion("service_charge is not null");
            return (Criteria) this;
        }

        public Criteria andServiceChargeEqualTo(Double value) {
            addCriterion("service_charge =", value, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeNotEqualTo(Double value) {
            addCriterion("service_charge <>", value, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeGreaterThan(Double value) {
            addCriterion("service_charge >", value, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeGreaterThanOrEqualTo(Double value) {
            addCriterion("service_charge >=", value, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeLessThan(Double value) {
            addCriterion("service_charge <", value, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeLessThanOrEqualTo(Double value) {
            addCriterion("service_charge <=", value, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeIn(List<Double> values) {
            addCriterion("service_charge in", values, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeNotIn(List<Double> values) {
            addCriterion("service_charge not in", values, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeBetween(Double value1, Double value2) {
            addCriterion("service_charge between", value1, value2, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andServiceChargeNotBetween(Double value1, Double value2) {
            addCriterion("service_charge not between", value1, value2, "serviceCharge");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNull() {
            addCriterion("channel_id is null");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNotNull() {
            addCriterion("channel_id is not null");
            return (Criteria) this;
        }

        public Criteria andChannelIdEqualTo(Integer value) {
            addCriterion("channel_id =", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotEqualTo(Integer value) {
            addCriterion("channel_id <>", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThan(Integer value) {
            addCriterion("channel_id >", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("channel_id >=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThan(Integer value) {
            addCriterion("channel_id <", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThanOrEqualTo(Integer value) {
            addCriterion("channel_id <=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIn(List<Integer> values) {
            addCriterion("channel_id in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotIn(List<Integer> values) {
            addCriterion("channel_id not in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdBetween(Integer value1, Integer value2) {
            addCriterion("channel_id between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotBetween(Integer value1, Integer value2) {
            addCriterion("channel_id not between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdIsNull() {
            addCriterion("payment_card_id is null");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdIsNotNull() {
            addCriterion("payment_card_id is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdEqualTo(String value) {
            addCriterion("payment_card_id =", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdNotEqualTo(String value) {
            addCriterion("payment_card_id <>", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdGreaterThan(String value) {
            addCriterion("payment_card_id >", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdGreaterThanOrEqualTo(String value) {
            addCriterion("payment_card_id >=", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdLessThan(String value) {
            addCriterion("payment_card_id <", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdLessThanOrEqualTo(String value) {
            addCriterion("payment_card_id <=", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdLike(String value) {
            addCriterion("payment_card_id like", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdNotLike(String value) {
            addCriterion("payment_card_id not like", value, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdIn(List<String> values) {
            addCriterion("payment_card_id in", values, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdNotIn(List<String> values) {
            addCriterion("payment_card_id not in", values, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdBetween(String value1, String value2) {
            addCriterion("payment_card_id between", value1, value2, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andPaymentCardIdNotBetween(String value1, String value2) {
            addCriterion("payment_card_id not between", value1, value2, "paymentCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdIsNull() {
            addCriterion("receive_card_id is null");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdIsNotNull() {
            addCriterion("receive_card_id is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdEqualTo(String value) {
            addCriterion("receive_card_id =", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdNotEqualTo(String value) {
            addCriterion("receive_card_id <>", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdGreaterThan(String value) {
            addCriterion("receive_card_id >", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdGreaterThanOrEqualTo(String value) {
            addCriterion("receive_card_id >=", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdLessThan(String value) {
            addCriterion("receive_card_id <", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdLessThanOrEqualTo(String value) {
            addCriterion("receive_card_id <=", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdLike(String value) {
            addCriterion("receive_card_id like", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdNotLike(String value) {
            addCriterion("receive_card_id not like", value, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdIn(List<String> values) {
            addCriterion("receive_card_id in", values, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdNotIn(List<String> values) {
            addCriterion("receive_card_id not in", values, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdBetween(String value1, String value2) {
            addCriterion("receive_card_id between", value1, value2, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andReceiveCardIdNotBetween(String value1, String value2) {
            addCriterion("receive_card_id not between", value1, value2, "receiveCardId");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNull() {
            addCriterion("summary is null");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNotNull() {
            addCriterion("summary is not null");
            return (Criteria) this;
        }

        public Criteria andSummaryEqualTo(String value) {
            addCriterion("summary =", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotEqualTo(String value) {
            addCriterion("summary <>", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThan(String value) {
            addCriterion("summary >", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThanOrEqualTo(String value) {
            addCriterion("summary >=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThan(String value) {
            addCriterion("summary <", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThanOrEqualTo(String value) {
            addCriterion("summary <=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLike(String value) {
            addCriterion("summary like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotLike(String value) {
            addCriterion("summary not like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryIn(List<String> values) {
            addCriterion("summary in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotIn(List<String> values) {
            addCriterion("summary not in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryBetween(String value1, String value2) {
            addCriterion("summary between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotBetween(String value1, String value2) {
            addCriterion("summary not between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andBillAmountIsNull() {
            addCriterion("bill_amount is null");
            return (Criteria) this;
        }

        public Criteria andBillAmountIsNotNull() {
            addCriterion("bill_amount is not null");
            return (Criteria) this;
        }

        public Criteria andBillAmountEqualTo(Double value) {
            addCriterion("bill_amount =", value, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountNotEqualTo(Double value) {
            addCriterion("bill_amount <>", value, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountGreaterThan(Double value) {
            addCriterion("bill_amount >", value, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountGreaterThanOrEqualTo(Double value) {
            addCriterion("bill_amount >=", value, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountLessThan(Double value) {
            addCriterion("bill_amount <", value, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountLessThanOrEqualTo(Double value) {
            addCriterion("bill_amount <=", value, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountIn(List<Double> values) {
            addCriterion("bill_amount in", values, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountNotIn(List<Double> values) {
            addCriterion("bill_amount not in", values, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountBetween(Double value1, Double value2) {
            addCriterion("bill_amount between", value1, value2, "billAmount");
            return (Criteria) this;
        }

        public Criteria andBillAmountNotBetween(Double value1, Double value2) {
            addCriterion("bill_amount not between", value1, value2, "billAmount");
            return (Criteria) this;
        }

        public Criteria andCardBalanceIsNull() {
            addCriterion("card_balance is null");
            return (Criteria) this;
        }

        public Criteria andCardBalanceIsNotNull() {
            addCriterion("card_balance is not null");
            return (Criteria) this;
        }

        public Criteria andCardBalanceEqualTo(Double value) {
            addCriterion("card_balance =", value, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceNotEqualTo(Double value) {
            addCriterion("card_balance <>", value, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceGreaterThan(Double value) {
            addCriterion("card_balance >", value, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceGreaterThanOrEqualTo(Double value) {
            addCriterion("card_balance >=", value, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceLessThan(Double value) {
            addCriterion("card_balance <", value, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceLessThanOrEqualTo(Double value) {
            addCriterion("card_balance <=", value, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceIn(List<Double> values) {
            addCriterion("card_balance in", values, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceNotIn(List<Double> values) {
            addCriterion("card_balance not in", values, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceBetween(Double value1, Double value2) {
            addCriterion("card_balance between", value1, value2, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardBalanceNotBetween(Double value1, Double value2) {
            addCriterion("card_balance not between", value1, value2, "cardBalance");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateIsNull() {
            addCriterion("card_last_payment_date is null");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateIsNotNull() {
            addCriterion("card_last_payment_date is not null");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateEqualTo(Date value) {
            addCriterionForJDBCDate("card_last_payment_date =", value, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("card_last_payment_date <>", value, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateGreaterThan(Date value) {
            addCriterionForJDBCDate("card_last_payment_date >", value, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("card_last_payment_date >=", value, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateLessThan(Date value) {
            addCriterionForJDBCDate("card_last_payment_date <", value, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("card_last_payment_date <=", value, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateIn(List<Date> values) {
            addCriterionForJDBCDate("card_last_payment_date in", values, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("card_last_payment_date not in", values, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("card_last_payment_date between", value1, value2, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andCardLastPaymentDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("card_last_payment_date not between", value1, value2, "cardLastPaymentDate");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysIsNull() {
            addCriterion("payment_days is null");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysIsNotNull() {
            addCriterion("payment_days is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysEqualTo(String value) {
            addCriterion("payment_days =", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysNotEqualTo(String value) {
            addCriterion("payment_days <>", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysGreaterThan(String value) {
            addCriterion("payment_days >", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysGreaterThanOrEqualTo(String value) {
            addCriterion("payment_days >=", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysLessThan(String value) {
            addCriterion("payment_days <", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysLessThanOrEqualTo(String value) {
            addCriterion("payment_days <=", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysLike(String value) {
            addCriterion("payment_days like", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysNotLike(String value) {
            addCriterion("payment_days not like", value, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysIn(List<String> values) {
            addCriterion("payment_days in", values, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysNotIn(List<String> values) {
            addCriterion("payment_days not in", values, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysBetween(String value1, String value2) {
            addCriterion("payment_days between", value1, value2, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andPaymentDaysNotBetween(String value1, String value2) {
            addCriterion("payment_days not between", value1, value2, "paymentDays");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}