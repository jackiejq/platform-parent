package com.slljr.finance.payment.utils;

import java.security.MessageDigest;
import java.util.*;

/**
 * ZM渠道,支持佳付通/通联/腾付通
 */
public class ZMUtils {
    static Map<String, String> resMsgMap = new HashMap<>();
    static Map<String, String> areaCodeMap = new HashMap<>();
    static Map<String, String> bankCodeMap = new HashMap<>();

    /**
     * 字符串转16进制
     */
    public static String str2HexStr(String str) {
        try {
            char[] chars = "0123456789ABCDEF".toCharArray();
            StringBuilder sb = new StringBuilder("");
            byte[] bs = str.getBytes("utf-8");
            int bit;
            for (int i = 0; i < bs.length; i++) {
                bit = (bs[i] & 0x0f0) >> 4;
                sb.append(chars[bit]);
                bit = bs[i] & 0x0f;
                sb.append(chars[bit]);
            }
            return sb.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 16进制转字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        try {
            return new String(bytes, "utf-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static String encoderByMd5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Title: sortMap
     * @Description: 对集合内的数据按key的字母顺序做排序
     */
    public static List<Map.Entry<String, String>> sortMap(final Map<String, String> map) {
        final List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        // 重写集合的排序方法：按字母顺序
        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(final Map.Entry<String, String> o1, final Map.Entry<String, String> o2) {
                return (o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase()));
            }
        });

        return infos;
    }


    /**
     * 根据交易状态码获取交易提示信息
     * @param key
     * @return
     */
    public static String getResMsg(String key){
        resMsgMapInit();

        String msg = resMsgMap.get(key);
        return msg != null? msg : "未知状态码!" + key;
    }

    /**
     * 获取城市代码
     * @param city
     * @return cityCode,provinceCode
     */
    public static String getAreaCode(String city){
        areaCodeMapInit();
        
        city = city.replaceAll("自治区", "");
        city = city.replaceAll("直辖市", "");
        city = city.replaceAll("市", "");

        for(String key : areaCodeMap.keySet()){
            if (key.equals(city)){
                return areaCodeMap.get(key);
            }
        }

        for(String key : areaCodeMap.keySet()){
            if (key.contains(city) || city.contains(key)){
                return areaCodeMap.get(key);
            }
        }

        //匹配不上则返回上海
        return "310000,000000";
    }

    /**
     * 获取银行代码
     * @param bankName
     * @return 联行号,简称代码
     */
    public static String getBankCode(String bankName){
        bankCodeMapInit();
        for(String key : bankCodeMap.keySet()){
            if (key.equals(bankName)){
                return bankCodeMap.get(key);
            }
        }

        for(String key : bankCodeMap.keySet()){
            if (key.contains(bankName) || bankName.contains(key)){
                return bankCodeMap.get(key);
            }
        }

        return " , ";
    }

    private static void resMsgMapInit(){
        if (resMsgMap.isEmpty()){
            resMsgMap.put("000", "操作成功");
            resMsgMap.put("100", "商户号不存在");
            resMsgMap.put("101", "当前请求已过期（30秒）");
            resMsgMap.put("102", "时间戳格式不正确");
            resMsgMap.put("103", "验签失败");
            resMsgMap.put("104", "通道异常");
            resMsgMap.put("105", "低于签约汇率");
            resMsgMap.put("106", "订单失败");
            resMsgMap.put("107", "未支付");
            resMsgMap.put("108", "订单已存在");
            resMsgMap.put("109", "参数不合法");
            resMsgMap.put("110", "订单不存在");
            resMsgMap.put("111", "进件失败");
            resMsgMap.put("112", "绑卡失败");
            resMsgMap.put("113", "快捷支付失败");
            resMsgMap.put("510", "非法报文");
            resMsgMap.put("520", "通讯异常");
            resMsgMap.put("530", "接口升级中");
            resMsgMap.put("999", "失败");
            resMsgMap.put("0000", "交易成功");
            resMsgMap.put("1999", "需获取短信验证码,进行下一步确认操作 ");
            resMsgMap.put("2000", "交易已受理（需查询接口确认）");
            resMsgMap.put("0003", "交易异常,请查询交易");
            resMsgMap.put("3054", "交易异常,请查询交易");
            resMsgMap.put("3004", "卡号错误");
            resMsgMap.put("3012:", "原交易不允许撤销或退货");
            resMsgMap.put("3035", "原交易不存在");
            resMsgMap.put("3045", "协议不存在");
            resMsgMap.put("3046", "卡信息或手机号码错误");
            resMsgMap.put("3057", "请重新获取验证码");
            resMsgMap.put("3058", "短信验证码错误");
            resMsgMap.put("3059", "短信验证码发送失败");
            resMsgMap.put("3999", "其他错误");
        }

    }
    private static void areaCodeMapInit(){
        if (areaCodeMap.isEmpty()){
            areaCodeMap.put("临沧", "530900,530000");
            areaCodeMap.put("拉萨", "540100,540000");
            areaCodeMap.put("昌都", "540300,540000");
            areaCodeMap.put("山南", "540500,540000");
            areaCodeMap.put("日喀则", "540200,540000");
            areaCodeMap.put("那曲", "542400,540000");
            areaCodeMap.put("阿里", "542500,540000");
            areaCodeMap.put("林芝", "540400,540000");
            areaCodeMap.put("西安", "610100,610000");
            areaCodeMap.put("铜川", "610200,610000");
            areaCodeMap.put("宝鸡", "610300,610000");
            areaCodeMap.put("咸阳", "610400,610000");
            areaCodeMap.put("渭南", "610500,610000");
            areaCodeMap.put("延安", "610600,610000");
            areaCodeMap.put("汉中", "610700,610000");
            areaCodeMap.put("榆林", "610800,610000");
            areaCodeMap.put("安康", "610900,610000");
            areaCodeMap.put("商洛", "611000,610000");
            areaCodeMap.put("兰州", "620100,620000");
            areaCodeMap.put("嘉峪关", "620200,620000");
            areaCodeMap.put("金昌", "620300,620000");
            areaCodeMap.put("白银", "620400,620000");
            areaCodeMap.put("天水", "620500,620000");
            areaCodeMap.put("武威", "620600,620000");
            areaCodeMap.put("张掖", "620700,620000");
            areaCodeMap.put("平凉", "620800,620000");
            areaCodeMap.put("酒泉", "620900,620000");
            areaCodeMap.put("庆阳", "621000,620000");
            areaCodeMap.put("定西", "621100,620000");
            areaCodeMap.put("陇南", "621200,620000");
            areaCodeMap.put("临夏", "622900,620000");
            areaCodeMap.put("甘南", "623000,620000");
            areaCodeMap.put("西宁", "630100,630000");
            areaCodeMap.put("海东", "630200,630000");
            areaCodeMap.put("海北", "632200,630000");
            areaCodeMap.put("黄南", "632300,630000");
            areaCodeMap.put("海南", "632500,630000");
            areaCodeMap.put("果洛", "632600,630000");
            areaCodeMap.put("玉树", "632700,630000");
            areaCodeMap.put("海西", "632800,630000");
            areaCodeMap.put("银川", "640100,640000");
            areaCodeMap.put("石嘴山", "640200,640000");
            areaCodeMap.put("吴忠", "640300,640000");
            areaCodeMap.put("中卫", "640500,640000");
            areaCodeMap.put("固原", "640400,640000");
            areaCodeMap.put("乌鲁木齐", "650100,650000");
            areaCodeMap.put("克拉玛依", "650200,650000");
            areaCodeMap.put("吐鲁番", "650400,650000");
            areaCodeMap.put("五家渠", "659004,650000");
            areaCodeMap.put("哈密", "650500,650000");
            areaCodeMap.put("昌吉", "652300,650000");
            areaCodeMap.put("博尔塔拉", "652700,650000");
            areaCodeMap.put("巴音郭楞", "652800,650000");
            areaCodeMap.put("阿克苏", "652900,650000");
            areaCodeMap.put("克孜勒苏柯尔克孜", "653000,650000");
            areaCodeMap.put("喀什", "653100,650000");
            areaCodeMap.put("和田", "653200,650000");
            areaCodeMap.put("伊犁", "654000,650000");
            areaCodeMap.put("塔城", "654200,650000");
            areaCodeMap.put("阿勒泰", "654300,650000");
            areaCodeMap.put("石河子", "659001,650000");
            areaCodeMap.put("三沙", "460300,460000");
            areaCodeMap.put("新疆", "650000,000000");
            areaCodeMap.put("北京", "110000,000000");
            areaCodeMap.put("天津", "120000,000000");
            areaCodeMap.put("河北", "130000,000000");
            areaCodeMap.put("山西", "140000,000000");
            areaCodeMap.put("内蒙古", "150000,000000");
            areaCodeMap.put("辽宁", "210000,000000");
            areaCodeMap.put("吉林", "220000,000000");
            areaCodeMap.put("黑龙江", "230000,000000");
            areaCodeMap.put("上海", "310000,000000");
            areaCodeMap.put("江苏", "320000,000000");
            areaCodeMap.put("浙江", "330000,000000");
            areaCodeMap.put("安徽", "340000,000000");
            areaCodeMap.put("福建", "350000,000000");
            areaCodeMap.put("江西", "360000,000000");
            areaCodeMap.put("山东", "370000,000000");
            areaCodeMap.put("河南", "410000,000000");
            areaCodeMap.put("湖北", "420000,000000");
            areaCodeMap.put("湖南", "430000,000000");
            areaCodeMap.put("广东", "440000,000000");
            areaCodeMap.put("广西", "450000,000000");
            areaCodeMap.put("海南", "460000,000000");
            areaCodeMap.put("重庆", "500000,000000");
            areaCodeMap.put("四川", "510000,000000");
            areaCodeMap.put("贵州", "520000,000000");
            areaCodeMap.put("云南", "530000,000000");
            areaCodeMap.put("西藏", "540000,000000");
            areaCodeMap.put("陕西", "610000,000000");
            areaCodeMap.put("甘肃", "620000,000000");
            areaCodeMap.put("青海", "630000,000000");
            areaCodeMap.put("宁夏", "640000,000000");
            areaCodeMap.put("北京", "110100,110000");
            areaCodeMap.put("天津", "120100,120000");
            areaCodeMap.put("石家庄", "130100,130000");
            areaCodeMap.put("唐山", "130200,130000");
            areaCodeMap.put("秦皇岛", "130300,130000");
            areaCodeMap.put("邯郸", "130400,130000");
            areaCodeMap.put("邢台", "130500,130000");
            areaCodeMap.put("保定", "130600,130000");
            areaCodeMap.put("张家口", "130700,130000");
            areaCodeMap.put("承德", "130800,130000");
            areaCodeMap.put("沧州", "130900,130000");
            areaCodeMap.put("廊坊", "131000,130000");
            areaCodeMap.put("衡水", "131100,130000");
            areaCodeMap.put("太原", "140100,140000");
            areaCodeMap.put("大同", "140200,140000");
            areaCodeMap.put("阳泉", "140300,140000");
            areaCodeMap.put("长治", "140400,140000");
            areaCodeMap.put("晋城", "140500,140000");
            areaCodeMap.put("朔州", "140600,140000");
            areaCodeMap.put("晋中", "140700,140000");
            areaCodeMap.put("运城", "140800,140000");
            areaCodeMap.put("忻州", "140900,140000");
            areaCodeMap.put("临汾", "141000,140000");
            areaCodeMap.put("吕梁", "141100,140000");
            areaCodeMap.put("呼和浩特", "150100,150000");
            areaCodeMap.put("包头", "150200,150000");
            areaCodeMap.put("乌海", "150300,150000");
            areaCodeMap.put("赤峰", "150400,150000");
            areaCodeMap.put("通辽", "150500,150000");
            areaCodeMap.put("鄂尔多斯", "150600,150000");
            areaCodeMap.put("呼伦贝尔", "150700,150000");
            areaCodeMap.put("兴安盟", "152200,150000");
            areaCodeMap.put("锡林郭勒", "152500,150000");
            areaCodeMap.put("乌兰察布", "150900,150000");
            areaCodeMap.put("巴彦淖尔", "150800,150000");
            areaCodeMap.put("阿拉善盟", "152900,150000");
            areaCodeMap.put("沈阳", "210100,210000");
            areaCodeMap.put("大连", "210200,210000");
            areaCodeMap.put("鞍山", "210300,210000");
            areaCodeMap.put("抚顺", "210400,210000");
            areaCodeMap.put("本溪", "210500,210000");
            areaCodeMap.put("丹东", "210600,210000");
            areaCodeMap.put("锦州", "210700,210000");
            areaCodeMap.put("营口", "210800,210000");
            areaCodeMap.put("阜新", "210900,210000");
            areaCodeMap.put("辽阳", "211000,210000");
            areaCodeMap.put("盘锦", "211100,210000");
            areaCodeMap.put("铁岭", "211200,210000");
            areaCodeMap.put("朝阳", "211300,210000");
            areaCodeMap.put("葫芦岛", "211400,210000");
            areaCodeMap.put("长春", "220100,220000");
            areaCodeMap.put("吉林", "220200,220000");
            areaCodeMap.put("四平", "220300,220000");
            areaCodeMap.put("辽源", "220400,220000");
            areaCodeMap.put("通化", "220500,220000");
            areaCodeMap.put("白山", "220600,220000");
            areaCodeMap.put("松原", "220700,220000");
            areaCodeMap.put("白城", "220800,220000");
            areaCodeMap.put("延边", "222400,220000");
            areaCodeMap.put("哈尔滨", "230100,230000");
            areaCodeMap.put("齐齐哈尔", "230200,230000");
            areaCodeMap.put("鸡西", "230300,230000");
            areaCodeMap.put("鹤岗", "230400,230000");
            areaCodeMap.put("双鸭山", "230500,230000");
            areaCodeMap.put("大庆", "230600,230000");
            areaCodeMap.put("伊春", "230700,230000");
            areaCodeMap.put("佳木斯", "230800,230000");
            areaCodeMap.put("七台河", "230900,230000");
            areaCodeMap.put("牡丹江", "231000,230000");
            areaCodeMap.put("黑河", "231100,230000");
            areaCodeMap.put("绥化", "231200,230000");
            areaCodeMap.put("大兴安岭", "232700,230000");
            areaCodeMap.put("上海", "310100,310000");
            areaCodeMap.put("南京", "320100,320000");
            areaCodeMap.put("无锡", "320200,320000");
            areaCodeMap.put("徐州", "320300,320000");
            areaCodeMap.put("常州", "320400,320000");
            areaCodeMap.put("苏州", "320500,320000");
            areaCodeMap.put("南通", "320600,320000");
            areaCodeMap.put("连云港", "320700,320000");
            areaCodeMap.put("淮安", "320800,320000");
            areaCodeMap.put("盐城", "320900,320000");
            areaCodeMap.put("扬州", "321000,320000");
            areaCodeMap.put("镇江", "321100,320000");
            areaCodeMap.put("泰州", "321200,320000");
            areaCodeMap.put("宿迁", "321300,320000");
            areaCodeMap.put("杭州", "330100,330000");
            areaCodeMap.put("宁波", "330200,330000");
            areaCodeMap.put("温州", "330300,330000");
            areaCodeMap.put("嘉兴", "330400,330000");
            areaCodeMap.put("湖州", "330500,330000");
            areaCodeMap.put("绍兴", "330600,330000");
            areaCodeMap.put("金华", "330700,330000");
            areaCodeMap.put("衢州", "330800,330000");
            areaCodeMap.put("舟山", "330900,330000");
            areaCodeMap.put("台州", "331000,330000");
            areaCodeMap.put("丽水", "331100,330000");
            areaCodeMap.put("合肥", "340100,340000");
            areaCodeMap.put("芜湖", "340200,340000");
            areaCodeMap.put("蚌埠", "340300,340000");
            areaCodeMap.put("淮南", "340400,340000");
            areaCodeMap.put("马鞍山", "340500,340000");
            areaCodeMap.put("淮北", "340600,340000");
            areaCodeMap.put("铜陵", "340700,340000");
            areaCodeMap.put("安庆", "340800,340000");
            areaCodeMap.put("黄山", "341000,340000");
            areaCodeMap.put("滁州", "341100,340000");
            areaCodeMap.put("阜阳", "341200,340000");
            areaCodeMap.put("宿州", "341300,340000");
            areaCodeMap.put("六安", "341500,340000");
            areaCodeMap.put("亳州", "341600,340000");
            areaCodeMap.put("池州", "341700,340000");
            areaCodeMap.put("宣城", "341800,340000");
            areaCodeMap.put("福州", "350100,350000");
            areaCodeMap.put("厦门", "350200,350000");
            areaCodeMap.put("莆田", "350300,350000");
            areaCodeMap.put("三明", "350400,350000");
            areaCodeMap.put("泉州", "350500,350000");
            areaCodeMap.put("漳州", "350600,350000");
            areaCodeMap.put("南平", "350700,350000");
            areaCodeMap.put("龙岩", "350800,350000");
            areaCodeMap.put("宁德", "350900,350000");
            areaCodeMap.put("南昌", "360100,360000");
            areaCodeMap.put("景德镇", "360200,360000");
            areaCodeMap.put("萍乡", "360300,360000");
            areaCodeMap.put("九江", "360400,360000");
            areaCodeMap.put("新余", "360500,360000");
            areaCodeMap.put("鹰潭", "360600,360000");
            areaCodeMap.put("赣州", "360700,360000");
            areaCodeMap.put("吉安", "360800,360000");
            areaCodeMap.put("宜春", "360900,360000");
            areaCodeMap.put("抚州", "361000,360000");
            areaCodeMap.put("上饶", "361100,360000");
            areaCodeMap.put("济南", "370100,370000");
            areaCodeMap.put("青岛", "370200,370000");
            areaCodeMap.put("淄博", "370300,370000");
            areaCodeMap.put("枣庄", "370400,370000");
            areaCodeMap.put("东营", "370500,370000");
            areaCodeMap.put("烟台", "370600,370000");
            areaCodeMap.put("潍坊", "370700,370000");
            areaCodeMap.put("济宁", "370800,370000");
            areaCodeMap.put("泰安", "370900,370000");
            areaCodeMap.put("威海", "371000,370000");
            areaCodeMap.put("日照", "371100,370000");
            areaCodeMap.put("莱芜", "371200,370000");
            areaCodeMap.put("临沂", "371300,370000");
            areaCodeMap.put("德州", "371400,370000");
            areaCodeMap.put("聊城", "371500,370000");
            areaCodeMap.put("滨州", "371600,370000");
            areaCodeMap.put("菏泽", "371700,370000");
            areaCodeMap.put("郑州", "410100,410000");
            areaCodeMap.put("开封", "410200,410000");
            areaCodeMap.put("洛阳", "410300,410000");
            areaCodeMap.put("平顶山", "410400,410000");
            areaCodeMap.put("安阳", "410500,410000");
            areaCodeMap.put("鹤壁", "410600,410000");
            areaCodeMap.put("新乡", "410700,410000");
            areaCodeMap.put("焦作", "410800,410000");
            areaCodeMap.put("济源", "419001,410000");
            areaCodeMap.put("濮阳", "410900,410000");
            areaCodeMap.put("许昌", "411000,410000");
            areaCodeMap.put("漯河", "411100,410000");
            areaCodeMap.put("三门峡", "411200,410000");
            areaCodeMap.put("南阳", "411300,410000");
            areaCodeMap.put("商丘", "411400,410000");
            areaCodeMap.put("信阳", "411500,410000");
            areaCodeMap.put("周口", "411600,410000");
            areaCodeMap.put("驻马店", "411700,410000");
            areaCodeMap.put("武汉", "420100,420000");
            areaCodeMap.put("黄石", "420200,420000");
            areaCodeMap.put("十堰", "420300,420000");
            areaCodeMap.put("宜昌", "420500,420000");
            areaCodeMap.put("襄阳", "420600,420000");
            areaCodeMap.put("鄂州", "420700,420000");
            areaCodeMap.put("神农架", "429021,420000");
            areaCodeMap.put("荆门", "420800,420000");
            areaCodeMap.put("孝感", "420900,420000");
            areaCodeMap.put("荆州", "421000,420000");
            areaCodeMap.put("仙桃", "429004,420000");
            areaCodeMap.put("天门", "429006,420000");
            areaCodeMap.put("黄冈", "421100,420000");
            areaCodeMap.put("咸宁", "421200,420000");
            areaCodeMap.put("随州", "421300,420000");
            areaCodeMap.put("恩施", "422800,420000");
            areaCodeMap.put("潜江", "429005,420000");
            areaCodeMap.put("长沙", "430100,430000");
            areaCodeMap.put("株洲", "430200,430000");
            areaCodeMap.put("湘潭", "430300,430000");
            areaCodeMap.put("衡阳", "430400,430000");
            areaCodeMap.put("邵阳", "430500,430000");
            areaCodeMap.put("岳阳", "430600,430000");
            areaCodeMap.put("常德", "430700,430000");
            areaCodeMap.put("张家界", "430800,430000");
            areaCodeMap.put("益阳", "430900,430000");
            areaCodeMap.put("郴州", "431000,430000");
            areaCodeMap.put("永州", "431100,430000");
            areaCodeMap.put("怀化", "431200,430000");
            areaCodeMap.put("娄底", "431300,430000");
            areaCodeMap.put("湘西", "433100,430000");
            areaCodeMap.put("广州", "440100,440000");
            areaCodeMap.put("韶关", "440200,440000");
            areaCodeMap.put("深圳", "440300,440000");
            areaCodeMap.put("珠海", "440400,440000");
            areaCodeMap.put("汕头", "440500,440000");
            areaCodeMap.put("佛山", "440600,440000");
            areaCodeMap.put("江门", "440700,440000");
            areaCodeMap.put("湛江", "440800,440000");
            areaCodeMap.put("茂名", "440900,440000");
            areaCodeMap.put("肇庆", "441200,440000");
            areaCodeMap.put("惠州", "441300,440000");
            areaCodeMap.put("梅州", "441400,440000");
            areaCodeMap.put("汕尾", "441500,440000");
            areaCodeMap.put("河源", "441600,440000");
            areaCodeMap.put("阳江", "441700,440000");
            areaCodeMap.put("清远", "441800,440000");
            areaCodeMap.put("东莞", "441900,440000");
            areaCodeMap.put("中山", "442000,440000");
            areaCodeMap.put("潮州", "445100,440000");
            areaCodeMap.put("揭阳", "445200,440000");
            areaCodeMap.put("云浮", "445300,440000");
            areaCodeMap.put("南宁", "450100,450000");
            areaCodeMap.put("柳州", "450200,450000");
            areaCodeMap.put("桂林", "450300,450000");
            areaCodeMap.put("梧州", "450400,450000");
            areaCodeMap.put("北海", "450500,450000");
            areaCodeMap.put("防城港", "450600,450000");
            areaCodeMap.put("钦州", "450700,450000");
            areaCodeMap.put("贵港", "450800,450000");
            areaCodeMap.put("玉林", "450900,450000");
            areaCodeMap.put("百色", "451000,450000");
            areaCodeMap.put("贺州", "451100,450000");
            areaCodeMap.put("河池", "451200,450000");
            areaCodeMap.put("来宾", "451300,450000");
            areaCodeMap.put("崇左", "451400,450000");
            areaCodeMap.put("海口", "460100,460000");
            areaCodeMap.put("三亚", "460200,460000");
            areaCodeMap.put("重庆", "500100,500000");
            areaCodeMap.put("成都", "510100,510000");
            areaCodeMap.put("自贡", "510300,510000");
            areaCodeMap.put("攀枝花", "510400,510000");
            areaCodeMap.put("泸州", "510500,510000");
            areaCodeMap.put("德阳", "510600,510000");
            areaCodeMap.put("绵阳", "510700,510000");
            areaCodeMap.put("广元", "510800,510000");
            areaCodeMap.put("遂宁", "510900,510000");
            areaCodeMap.put("内江", "511000,510000");
            areaCodeMap.put("乐山", "511100,510000");
            areaCodeMap.put("南充", "511300,510000");
            areaCodeMap.put("眉山", "511400,510000");
            areaCodeMap.put("宜宾", "511500,510000");
            areaCodeMap.put("广安", "511600,510000");
            areaCodeMap.put("达州", "511700,510000");
            areaCodeMap.put("雅安", "511800,510000");
            areaCodeMap.put("巴中", "511900,510000");
            areaCodeMap.put("资阳", "512000,510000");
            areaCodeMap.put("阿坝", "513200,510000");
            areaCodeMap.put("甘孜", "513300,510000");
            areaCodeMap.put("凉山", "513400,510000");
            areaCodeMap.put("贵阳", "520100,520000");
            areaCodeMap.put("六盘水", "520200,520000");
            areaCodeMap.put("遵义", "520300,520000");
            areaCodeMap.put("安顺", "520400,520000");
            areaCodeMap.put("铜仁", "520600,520000");
            areaCodeMap.put("黔西南", "522300,520000");
            areaCodeMap.put("毕节", "520500,520000");
            areaCodeMap.put("黔东南", "522600,520000");
            areaCodeMap.put("黔南", "522700,520000");
            areaCodeMap.put("昆明", "530100,530000");
            areaCodeMap.put("曲靖", "530300,530000");
            areaCodeMap.put("玉溪", "530400,530000");
            areaCodeMap.put("保山", "530500,530000");
            areaCodeMap.put("昭通", "530600,530000");
            areaCodeMap.put("丽江", "530700,530000");
            areaCodeMap.put("普洱", "530800,530000");
            areaCodeMap.put("楚雄", "532300,530000");
            areaCodeMap.put("红河", "532500,530000");
            areaCodeMap.put("文山", "532600,530000");
            areaCodeMap.put("西双版纳", "532800,530000");
            areaCodeMap.put("大理", "532900,530000");
            areaCodeMap.put("德宏", "533100,530000");
            areaCodeMap.put("怒江", "533300,530000");
            areaCodeMap.put("迪庆", "533400,530000");
        }
    }
    private static void bankCodeMapInit(){
        if (bankCodeMap.isEmpty()){
            bankCodeMap.put("中国工商银行", "102100099996,ICBC");
            bankCodeMap.put("中国农业银行", "103100000026,ABC");
            bankCodeMap.put("中国银行", "104100000004,BOC");
            bankCodeMap.put("中国建设银行", "105100000017,CCB");
            bankCodeMap.put("交通银行", "301290000007,COMM");
            bankCodeMap.put("中信银行", "302100011000,CITIC");
            bankCodeMap.put("光大银行", "303100000006,CEB");
            bankCodeMap.put("华夏银行", "304100040000,HXB");
            bankCodeMap.put("民生银行", "305100000013,CMBC");
            bankCodeMap.put("广发银行", "306331003281,GDB");
            bankCodeMap.put("招商银行", "308584000013,CMB");
            bankCodeMap.put("兴业银行", "309391000011,CIB");
            bankCodeMap.put("浦发银行", "310290000013,SPDB");
            bankCodeMap.put("恒丰银行", "315456000105,EGB");
            bankCodeMap.put("浙商银行", "316331000018,CZB");
            bankCodeMap.put("徽商银行", "319361000013,HSB");
            bankCodeMap.put("上海农商银行", "322290000011,SHRCB");
            bankCodeMap.put("上海银行", "325290000012,SHB");
            bankCodeMap.put("中国邮政储蓄银行", "403100000004,PSBC");
            bankCodeMap.put("平安银行", "307584007998,SPAB");
            bankCodeMap.put("北京银行", "313100000013,BJB");
            bankCodeMap.put("云南省农村信用社", "402731057238,YNRCC");
            bankCodeMap.put("海南省农村信用社", "402641000014,HNB");
            bankCodeMap.put("广西农村信用社", "402611099974,GXRCU");
            bankCodeMap.put("东莞农村商业银行", "402602000018,DRCB");
            bankCodeMap.put("福建省农村信用社", "402391000068,FJNXB");
            bankCodeMap.put("安徽省农村信用社联合社", "402361018886,AHRCU");
            bankCodeMap.put("浙江省农村信用社", "402331000007,ZJRCC");
            bankCodeMap.put("江苏省农村信用社联合社", "402301099998,JSNX");
            bankCodeMap.put("吉林农村信用社", "402241000015,");
            bankCodeMap.put("北京农村商业银行", "402100000018,BJRCB");
            bankCodeMap.put("天津银行", "313110000017,");
            bankCodeMap.put("包商银行", "313192000013,BSB");
            bankCodeMap.put("重庆银行", "313653000013,CQB");
            bankCodeMap.put("广州银行", "313581003284,GCB");
            bankCodeMap.put("昆仑银行", "313882000012,KLB");
            bankCodeMap.put("广州农村商业银行", "314581000011,GRCB");
            bankCodeMap.put("浙江民泰商业银行", "313345400010,MTBANK");
            bankCodeMap.put("厦门银行", "313393080005,XMCCB");
            bankCodeMap.put("杭州银行", "313331000014,");
        }
    }
}
