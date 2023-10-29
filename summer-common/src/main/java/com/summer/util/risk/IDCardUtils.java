package com.summer.util.risk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * @author sdcuike
 * <p>
 * Created on 2016年9月23日
 * <p>
 * 中华人民共和国国家标准《公民身份号码》（GB11643-1999），居民身份证号码国家标准。
 * 中国国家质量技术监督局于1999年7月1日发布国家标准：GB11643-1999《公民身份号码》。主要内容如下：
 * <p>
 * 一、范围 该标准规定了公民身份号码的编码对象、号码的结构和表现形式，使每个编码对象获得一个唯一的、不变的法定号码。
 * <p>
 * 二、编码对象 公民身份号码的编码对象是具有中华人民共和国国籍的公民。
 * <p>
 * 三、号码的结构和表示形式
 * <p>
 * 1、号码的结构 <br>
 * 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，
 * 三位数字顺序码和一位数字校验码。 <br>
 * 2、地址码 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。<br>
 * 3、出生日期码 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。<br>
 * 4、顺序码 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。<br>
 * 5、校验码 根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2中的校验码计算方法计算确定。
 * <p>
 * 四、较验码的计算 根据ISO 7064:1983.MOD 11-2，较验码的计算方法如下：<br>
 * （1）十七位数字本体码加权求和公式 <br>
 * S = Sum(Ai * Wi)； <br>
 * i = 0, ... , 16 ，分别对应身份证的前17位数字； <br>
 * Ai，表示第i位置上的身份证号码数字值； <br/>
 * Wi，表示第i位置上的加权因子，分别是 7，9，10，5，8，4，2，1，6，3，7，9，10，5，8，4，2； <br>
 * sum，求和。 <br>
 * （2）计算模 <br>
 * Y = mod(S, 11) <br>
 * （3）通过模得到对应的校验码 <br>
 * Y: 0 1 2 3 4 5 6 7 8 9 10 <br>
 * 校验码: 1 0 X 9 8 7 6 5 4 3 2<br>
 * <br>
 * <p>
 * 参考： <br>
 * 1、行政区划代码：http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/<br>
 * 2、身份证号码随机生成出处：http://shenfenzheng.293.net/<br>
 * 3、身份证号码查询归属地：http://qq.ip138.com/idsearch/<br>
 * 4、GB11643-1999《公民身份号码》：http://www.docin.com/p-247208446.html<br>
 * <p>
 * <p>
 * <p>
 * 18位身份证号码各位的含义: <br>
 * 1-2位省、自治区、直辖市代码； <br>
 * 3-4位地级市、盟、自治州代码； <br>
 * 5-6位县、县级市、区代码； <br>
 * 7-14位出生年月日，比如19670401代表1967年4月1日； <br>
 * 15-17位为顺序号，其中17位（倒数第二位）男为单数，女为双数； <br>
 * 18位为校验码，0-9和X。作为尾号的校验码，是由把前十七位数字带入统一的公式计算出来的，计算的结果是0-10，<br>
 * 如果某人的尾号是0－9，都不会出现X，但如果尾号是10，那么就得用X来代替，因为如果用10做尾号，那么此人的身份证就变成了19位。
 * X是罗马数字的10，用X来代替10。<br>
 */
@ThreadSafe
public final class IDCardUtils {
    private static final int IDCardLength15 = 15;
    private static final int IDCardLength18 = 18;
    /**
     * 加权因子
     */
    private static final int[] WI = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
            4, 2};
    /**
     * 对应的校验码
     */
    private static final char[] CheckBit = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    public static final Pattern PatternForIdCard15 = Pattern
            .compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");

    public static final Pattern PatternForIdCard18 = Pattern
            .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");

    public static final String DateFormatPattern = "yyyyMMdd";

    /**
     * 15位有效身份证 转化为18位身份证<br>
     * <p>
     * 15位身份证号码各位的含义:<br>
     * 1-2位省、自治区、直辖市代码； <br>
     * 3-4位地级市、盟、自治州代码； <br>
     * 5-6位县、县级市、区代码； <br>
     * 7-12位出生年月日;<br>
     * 13-15位为顺序号，其中15位男为单数，女为双数；<br>
     * <p>
     * 比如670401代表1967年4月1日,与18位的第一个区别； 13-15位为顺序号，其中15位男为单数，女为双数；<br>
     * 与18位身份证号的第二个区别：没有最后一位的验证码。
     *
     * @param idCard15 15位有效身份证
     * @return {@code String} 18位身份证
     */
    public static String transformIdCard15to18(String idCard15) {
        if (StringUtils.isBlank(idCard15) || idCard15.length() != IDCardLength15) {
            throw new IllegalArgumentException("15位身份证长度不合法");
        }
        StringBuilder idCard18 = new StringBuilder(idCard15);
        idCard18.insert(6, "19");

        char checkCode = getIdCardCheckCode(idCard18.toString());

        idCard18.append(checkCode);

        return idCard18.toString();
    }

    private static char getIdCardCheckCode(String idCard18) {

        int sum = 0;
        for (int i = 0, length = IDCardLength18 - 1; i < length; i++) {
            sum += Character.getNumericValue(idCard18.charAt(i)) * WI[i];
        }
        return CheckBit[sum % 11];
    }

    public static boolean isValidIdCard18(String idCard18) {
        if (idCard18.length() != IDCardLength18) {
            return false;
        }

        if (!PatternForIdCard18.matcher(idCard18).find()) {
            return false;
        }
        String province = getProvinceName(idCard18);
        if (StringUtils.isBlank(province)) {
            return false;
        }

        String birthDay = idCard18.substring(6, 14);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatPattern, Locale.ENGLISH);
        try {
            Date date = getBirthDay(idCard18);
            if (!birthDay.equals(dateFormat.format(date))) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }

        char idCardCheckCode = getIdCardCheckCode(idCard18);
        if (idCardCheckCode != idCard18.charAt(IDCardLength18 - 1)) {
            return false;
        }

        return true;
    }

    public static boolean isValidIdCard15(String idCard15) {
        if (idCard15.length() != IDCardLength15) {
            return false;
        }

        if (!PatternForIdCard15.matcher(idCard15).find()) {
            return false;
        }

        String province = getProvinceName(idCard15);
        if (StringUtils.isBlank(province)) {
            return false;
        }

        String birthDay = "19" + idCard15.substring(6, 12);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatPattern, Locale.ENGLISH);
        try {
            Date date = getBirthDay(idCard15);
            if (!birthDay.equals(dateFormat.format(date))) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static String getProvinceName(String idCard) {
        return ProvinceCode.get(idCard.substring(0, 2));
    }

    public static String getCityName(String idCard) {
        return CityCode.get(idCard.substring(0, 4));
    }

    /**
     * 获取性别信息
     *
     * @param idCard 15位or18位身份证
     * @return {@code String} F-女；M-男；N-未知
     */
    public static String getGender(String idCard) {
        if (idCard.length() != IDCardLength15 && idCard.length() != IDCardLength18) {
            throw new IllegalArgumentException("身份证长度不合法");
        }

        if (idCard.length() == IDCardLength15) {
            String gender = idCard.substring(IDCardLength15 - 1, IDCardLength15);
            if (Integer.parseInt(gender) % 2 == 0) {
                return "F";
            } else {
                return "M";
            }
        }
        if (idCard.length() == IDCardLength18) {
            String gender = idCard.substring(IDCardLength18 - 2, IDCardLength18 - 1);
            if (Integer.parseInt(gender) % 2 == 0) {
                return "F";
            } else {
                return "M";
            }
        }

        return "N";
    }

    public static Date getBirthDay(String idCard) throws ParseException {
        String birthDay;
        if (idCard.length() == 15) {
            birthDay = "19" + idCard.substring(6, 12);
        } else {
            birthDay = idCard.substring(6, 14);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatPattern, Locale.ENGLISH);
        return dateFormat.parse(birthDay);
    }

    /**
     * 获取详细地址内部类。采用内部类，避免资源预先初始化。<br>
     */
    @ThreadSafe
    public static class AreCodeUtil {
        private static Map<String, String> areCode;

        static {

            try (InputStream inputStream = AreCodeUtil.class.getResourceAsStream("areCode.json");) {

                ObjectMapper objectMapper = new ObjectMapper();
                areCode = objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static Set<Entry<String, String>> entrySet() {
            return new HashSet<>(areCode.entrySet());
        }

        /**
         * 根据地区码获取详细地区地址
         *
         * @param code
         * @return {@code String}
         */
        public static String getAreAddressByCode(String code) {
            return areCode.get(code);
        }

        /**
         * 从http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201608/t20160809_1386477.
         * html 获取 从官网获取后，对内容格式稍微调整放入areCodeSource.txt文件内，自动生成排序后的jsons各省市区数据
         *
         * @return {@code String} 排序后的json字符串
         * @throws IOException
         * @throws URISyntaxException
         */
        public static Map<String, String> formatFromAreCodeSource() throws IOException, URISyntaxException {
            Map<String, String> map = new LinkedHashMap<>();
            URI uri = AreCodeUtil.class.getResource("areCodeSource.txt").toURI();
            List<String> allLines = new ArrayList<>();
            List<String> allLinesT = Files.readAllLines(Paths.get(uri), StandardCharsets.UTF_8);

            for (String ct : allLinesT) {
                if (ct != null && ct.length() != 0) {
                    allLines.add(ct);
                }
            }

            Collections.sort(allLines, new Comparator<String>() {
                @Override
                public int compare(String a, String b) {
                    return a.substring(0, a.indexOf(",")).compareTo(b.substring(0, b.indexOf(",")));
                }
            });
            for (String ct : allLines) {
                String temp = ct.trim();
                int index = temp.indexOf(",");
                String key = temp.substring(0, index);
                String value = temp.substring(index + 1, temp.length());

                //省、自治区、直辖市代码
                String provinceKey = key.substring(0, 2);
                //地级市、盟、自治州代码
                String cityKey = key.substring(0, 4);

                if (key.endsWith("0000")) {
                    map.put(provinceKey, value);
                } else if (!key.endsWith("0000") && key.endsWith("00")) {

                    map.put(cityKey, map.get(provinceKey) + value);
                } else {
                    //县、县级市、区代码
                    map.put(key, map.get(cityKey) + value);
                }
            }

            return map;

        }

    }

    /**
     * 城市代码
     */
    private static final Map<String, String> ProvinceCode = new HashMap<>();

    static {

        ProvinceCode.put("11", "北京市");
        ProvinceCode.put("12", "天津市");
        ProvinceCode.put("13", "河北省");
        ProvinceCode.put("14", "山西省");
        ProvinceCode.put("15", "内蒙古自治区");
        ProvinceCode.put("21", "辽宁省");
        ProvinceCode.put("22", "吉林省");
        ProvinceCode.put("23", "黑龙江省");
        ProvinceCode.put("31", "上海市");
        ProvinceCode.put("32", "江苏省");
        ProvinceCode.put("33", "浙江省");
        ProvinceCode.put("34", "安徽省");
        ProvinceCode.put("35", "福建省");
        ProvinceCode.put("36", "江西省");
        ProvinceCode.put("37", "山东省");
        ProvinceCode.put("41", "河南省");
        ProvinceCode.put("42", "湖北省");
        ProvinceCode.put("43", "湖南省");
        ProvinceCode.put("44", "广东省");
        ProvinceCode.put("45", "广西壮族自治区");
        ProvinceCode.put("46", "海南省");
        ProvinceCode.put("50", "重庆市");
        ProvinceCode.put("51", "四川省");
        ProvinceCode.put("52", "贵州省");
        ProvinceCode.put("53", "云南省");
        ProvinceCode.put("54", "西藏自治区");
        ProvinceCode.put("61", "陕西省");
        ProvinceCode.put("62", "甘肃省");
        ProvinceCode.put("63", "青海省");
        ProvinceCode.put("64", "宁夏回族自治区");
        ProvinceCode.put("65", "新疆维吾尔自治区");
        ProvinceCode.put("71", "台湾省");
        ProvinceCode.put("81", "香港特别行政区");
        ProvinceCode.put("82", "澳门特别行政区");
    }

    /**
     * 市区代码
     */
    private static final Map<String, String> CityCode = new HashMap<>();

    static {
        CityCode.put("1101", "北京市市辖区");
        CityCode.put("1102", "北京市县");
        CityCode.put("1201", "天津市市辖区");
        CityCode.put("1202", "天津市县");
        CityCode.put("1301", "河北省石家庄市");
        CityCode.put("1302", "河北省唐山市");
        CityCode.put("1303", "河北省秦皇岛市");
        CityCode.put("1304", "河北省邯郸市");
        CityCode.put("1305", "河北省邢台市");
        CityCode.put("1306", "河北省保定市");
        CityCode.put("1307", "河北省张家口市");
        CityCode.put("1308", "河北省承德市");
        CityCode.put("1309", "河北省沧州市");
        CityCode.put("1310", "河北省廊坊市");
        CityCode.put("1311", "河北省衡水市");
        CityCode.put("1390", "河北省省直辖县级行政区划");
        CityCode.put("1401", "山西省太原市");
        CityCode.put("1402", "山西省大同市");
        CityCode.put("1403", "山西省阳泉市");
        CityCode.put("1404", "山西省长治市");
        CityCode.put("1405", "山西省晋城市");
        CityCode.put("1406", "山西省朔州市");
        CityCode.put("1407", "山西省晋中市");
        CityCode.put("1408", "山西省运城市");
        CityCode.put("1409", "山西省忻州市");
        CityCode.put("1410", "山西省临汾市");
        CityCode.put("1411", "山西省吕梁市");
        CityCode.put("1501", "内蒙古自治区呼和浩特市");
        CityCode.put("1502", "内蒙古自治区包头市");
        CityCode.put("1503", "内蒙古自治区乌海市");
        CityCode.put("1504", "内蒙古自治区赤峰市");
        CityCode.put("1505", "内蒙古自治区通辽市");
        CityCode.put("1506", "内蒙古自治区鄂尔多斯市");
        CityCode.put("1507", "内蒙古自治区呼伦贝尔市");
        CityCode.put("1508", "内蒙古自治区巴彦淖尔市");
        CityCode.put("1509", "内蒙古自治区乌兰察布市");
        CityCode.put("1522", "内蒙古自治区兴安盟");
        CityCode.put("1525", "内蒙古自治区锡林郭勒盟");
        CityCode.put("1529", "内蒙古自治区阿拉善盟");
        CityCode.put("2101", "辽宁省沈阳市");
        CityCode.put("2102", "辽宁省大连市");
        CityCode.put("2103", "辽宁省鞍山市");
        CityCode.put("2104", "辽宁省抚顺市");
        CityCode.put("2105", "辽宁省本溪市");
        CityCode.put("2106", "辽宁省丹东市");
        CityCode.put("2107", "辽宁省锦州市");
        CityCode.put("2108", "辽宁省营口市");
        CityCode.put("2109", "辽宁省阜新市");
        CityCode.put("2110", "辽宁省辽阳市");
        CityCode.put("2111", "辽宁省盘锦市");
        CityCode.put("2112", "辽宁省铁岭市");
        CityCode.put("2113", "辽宁省朝阳市");
        CityCode.put("2114", "辽宁省葫芦岛市");
        CityCode.put("2201", "吉林省长春市");
        CityCode.put("2202", "吉林省吉林市");
        CityCode.put("2203", "吉林省四平市");
        CityCode.put("2204", "吉林省辽源市");
        CityCode.put("2205", "吉林省通化市");
        CityCode.put("2206", "吉林省白山市");
        CityCode.put("2207", "吉林省松原市");
        CityCode.put("2208", "吉林省白城市");
        CityCode.put("2224", "吉林省延边朝鲜族自治州");
        CityCode.put("2301", "黑龙江省哈尔滨市");
        CityCode.put("2302", "黑龙江省齐齐哈尔市");
        CityCode.put("2303", "黑龙江省鸡西市");
        CityCode.put("2304", "黑龙江省鹤岗市");
        CityCode.put("2305", "黑龙江省双鸭山市");
        CityCode.put("2306", "黑龙江省大庆市");
        CityCode.put("2307", "黑龙江省伊春市");
        CityCode.put("2308", "黑龙江省佳木斯市");
        CityCode.put("2309", "黑龙江省七台河市");
        CityCode.put("2310", "黑龙江省牡丹江市");
        CityCode.put("2311", "黑龙江省黑河市");
        CityCode.put("2312", "黑龙江省绥化市");
        CityCode.put("2327", "黑龙江省大兴安岭地区");
        CityCode.put("3101", "上海市市辖区");
        CityCode.put("3102", "上海市县");
        CityCode.put("3201", "江苏省南京市");
        CityCode.put("3202", "江苏省无锡市");
        CityCode.put("3203", "江苏省徐州市");
        CityCode.put("3204", "江苏省常州市");
        CityCode.put("3205", "江苏省苏州市");
        CityCode.put("3206", "江苏省南通市");
        CityCode.put("3207", "江苏省连云港市");
        CityCode.put("3208", "江苏省淮安市");
        CityCode.put("3209", "江苏省盐城市");
        CityCode.put("3210", "江苏省扬州市");
        CityCode.put("3211", "江苏省镇江市");
        CityCode.put("3212", "江苏省泰州市");
        CityCode.put("3213", "江苏省宿迁市");
        CityCode.put("3301", "浙江省杭州市");
        CityCode.put("3302", "浙江省宁波市");
        CityCode.put("3303", "浙江省温州市");
        CityCode.put("3304", "浙江省嘉兴市");
        CityCode.put("3305", "浙江省湖州市");
        CityCode.put("3306", "浙江省绍兴市");
        CityCode.put("3307", "浙江省金华市");
        CityCode.put("3308", "浙江省衢州市");
        CityCode.put("3309", "浙江省舟山市");
        CityCode.put("3310", "浙江省台州市");
        CityCode.put("3311", "浙江省丽水市");
        CityCode.put("3401", "安徽省合肥市");
        CityCode.put("3402", "安徽省芜湖市");
        CityCode.put("3403", "安徽省蚌埠市");
        CityCode.put("3404", "安徽省淮南市");
        CityCode.put("3405", "安徽省马鞍山市");
        CityCode.put("3406", "安徽省淮北市");
        CityCode.put("3407", "安徽省铜陵市");
        CityCode.put("3408", "安徽省安庆市");
        CityCode.put("3410", "安徽省黄山市");
        CityCode.put("3411", "安徽省滁州市");
        CityCode.put("3412", "安徽省阜阳市");
        CityCode.put("3413", "安徽省宿州市");
        CityCode.put("3415", "安徽省六安市");
        CityCode.put("3416", "安徽省亳州市");
        CityCode.put("3417", "安徽省池州市");
        CityCode.put("3418", "安徽省宣城市");
        CityCode.put("3501", "福建省福州市");
        CityCode.put("3502", "福建省厦门市");
        CityCode.put("3503", "福建省莆田市");
        CityCode.put("3504", "福建省三明市");
        CityCode.put("3505", "福建省泉州市");
        CityCode.put("3506", "福建省漳州市");
        CityCode.put("3507", "福建省南平市");
        CityCode.put("3508", "福建省龙岩市");
        CityCode.put("3509", "福建省宁德市");
        CityCode.put("3601", "江西省南昌市");
        CityCode.put("3602", "江西省景德镇市");
        CityCode.put("3603", "江西省萍乡市");
        CityCode.put("3604", "江西省九江市");
        CityCode.put("3605", "江西省新余市");
        CityCode.put("3606", "江西省鹰潭市");
        CityCode.put("3607", "江西省赣州市");
        CityCode.put("3608", "江西省吉安市");
        CityCode.put("3609", "江西省宜春市");
        CityCode.put("3610", "江西省抚州市");
        CityCode.put("3611", "江西省上饶市");
        CityCode.put("3701", "山东省济南市");
        CityCode.put("3702", "山东省青岛市");
        CityCode.put("3703", "山东省淄博市");
        CityCode.put("3704", "山东省枣庄市");
        CityCode.put("3705", "山东省东营市");
        CityCode.put("3706", "山东省烟台市");
        CityCode.put("3707", "山东省潍坊市");
        CityCode.put("3708", "山东省济宁市");
        CityCode.put("3709", "山东省泰安市");
        CityCode.put("3710", "山东省威海市");
        CityCode.put("3711", "山东省日照市");
        CityCode.put("3712", "山东省莱芜市");
        CityCode.put("3713", "山东省临沂市");
        CityCode.put("3714", "山东省德州市");
        CityCode.put("3715", "山东省聊城市");
        CityCode.put("3716", "山东省滨州市");
        CityCode.put("3717", "山东省菏泽市");
        CityCode.put("4101", "河南省郑州市");
        CityCode.put("4102", "河南省开封市");
        CityCode.put("4103", "河南省洛阳市");
        CityCode.put("4104", "河南省平顶山市");
        CityCode.put("4105", "河南省安阳市");
        CityCode.put("4106", "河南省鹤壁市");
        CityCode.put("4107", "河南省新乡市");
        CityCode.put("4108", "河南省焦作市");
        CityCode.put("4109", "河南省濮阳市");
        CityCode.put("4110", "河南省许昌市");
        CityCode.put("4111", "河南省漯河市");
        CityCode.put("4112", "河南省三门峡市");
        CityCode.put("4113", "河南省南阳市");
        CityCode.put("4114", "河南省商丘市");
        CityCode.put("4115", "河南省信阳市");
        CityCode.put("4116", "河南省周口市");
        CityCode.put("4117", "河南省驻马店市");
        CityCode.put("4190", "河南省省直辖县级行政区划");
        CityCode.put("4201", "湖北省武汉市");
        CityCode.put("4202", "湖北省黄石市");
        CityCode.put("4203", "湖北省十堰市");
        CityCode.put("4205", "湖北省宜昌市");
        CityCode.put("4206", "湖北省襄阳市");
        CityCode.put("4207", "湖北省鄂州市");
        CityCode.put("4208", "湖北省荆门市");
        CityCode.put("4209", "湖北省孝感市");
        CityCode.put("4210", "湖北省荆州市");
        CityCode.put("4211", "湖北省黄冈市");
        CityCode.put("4212", "湖北省咸宁市");
        CityCode.put("4213", "湖北省随州市");
        CityCode.put("4228", "湖北省恩施土家族苗族自治州");
        CityCode.put("4290", "湖北省省直辖县级行政区划");
        CityCode.put("4301", "湖南省长沙市");
        CityCode.put("4302", "湖南省株洲市");
        CityCode.put("4303", "湖南省湘潭市");
        CityCode.put("4304", "湖南省衡阳市");
        CityCode.put("4305", "湖南省邵阳市");
        CityCode.put("4306", "湖南省岳阳市");
        CityCode.put("4307", "湖南省常德市");
        CityCode.put("4308", "湖南省张家界市");
        CityCode.put("4309", "湖南省益阳市");
        CityCode.put("4310", "湖南省郴州市");
        CityCode.put("4311", "湖南省永州市");
        CityCode.put("4312", "湖南省怀化市");
        CityCode.put("4313", "湖南省娄底市");
        CityCode.put("4331", "湖南省湘西土家族苗族自治州");
        CityCode.put("4401", "广东省广州市");
        CityCode.put("4402", "广东省韶关市");
        CityCode.put("4403", "广东省深圳市");
        CityCode.put("4404", "广东省珠海市");
        CityCode.put("4405", "广东省汕头市");
        CityCode.put("4406", "广东省佛山市");
        CityCode.put("4407", "广东省江门市");
        CityCode.put("4408", "广东省湛江市");
        CityCode.put("4409", "广东省茂名市");
        CityCode.put("4412", "广东省肇庆市");
        CityCode.put("4413", "广东省惠州市");
        CityCode.put("4414", "广东省梅州市");
        CityCode.put("4415", "广东省汕尾市");
        CityCode.put("4416", "广东省河源市");
        CityCode.put("4417", "广东省阳江市");
        CityCode.put("4418", "广东省清远市");
        CityCode.put("4419", "广东省东莞市");
        CityCode.put("4420", "广东省中山市");
        CityCode.put("4451", "广东省潮州市");
        CityCode.put("4452", "广东省揭阳市");
        CityCode.put("4453", "广东省云浮市");
        CityCode.put("4501", "广西壮族自治区南宁市");
        CityCode.put("4502", "广西壮族自治区柳州市");
        CityCode.put("4503", "广西壮族自治区桂林市");
        CityCode.put("4504", "广西壮族自治区梧州市");
        CityCode.put("4505", "广西壮族自治区北海市");
        CityCode.put("4506", "广西壮族自治区防城港市");
        CityCode.put("4507", "广西壮族自治区钦州市");
        CityCode.put("4508", "广西壮族自治区贵港市");
        CityCode.put("4509", "广西壮族自治区玉林市");
        CityCode.put("4510", "广西壮族自治区百色市");
        CityCode.put("4511", "广西壮族自治区贺州市");
        CityCode.put("4512", "广西壮族自治区河池市");
        CityCode.put("4513", "广西壮族自治区来宾市");
        CityCode.put("4514", "广西壮族自治区崇左市");
        CityCode.put("4601", "海南省海口市");
        CityCode.put("4602", "海南省三亚市");
        CityCode.put("4603", "海南省三沙市");
        CityCode.put("4690", "海南省省直辖县级行政区划");
        CityCode.put("5001", "重庆市市辖区");
        CityCode.put("5002", "重庆市县");
        CityCode.put("5101", "四川省成都市");
        CityCode.put("5103", "四川省自贡市");
        CityCode.put("5104", "四川省攀枝花市");
        CityCode.put("5105", "四川省泸州市");
        CityCode.put("5106", "四川省德阳市");
        CityCode.put("5107", "四川省绵阳市");
        CityCode.put("5108", "四川省广元市");
        CityCode.put("5109", "四川省遂宁市");
        CityCode.put("5110", "四川省内江市");
        CityCode.put("5111", "四川省乐山市");
        CityCode.put("5113", "四川省南充市");
        CityCode.put("5114", "四川省眉山市");
        CityCode.put("5115", "四川省宜宾市");
        CityCode.put("5116", "四川省广安市");
        CityCode.put("5117", "四川省达州市");
        CityCode.put("5118", "四川省雅安市");
        CityCode.put("5119", "四川省巴中市");
        CityCode.put("5120", "四川省资阳市");
        CityCode.put("5132", "四川省阿坝藏族羌族自治州");
        CityCode.put("5133", "四川省甘孜藏族自治州");
        CityCode.put("5134", "四川省凉山彝族自治州");
        CityCode.put("5201", "贵州省贵阳市");
        CityCode.put("5202", "贵州省六盘水市");
        CityCode.put("5203", "贵州省遵义市");
        CityCode.put("5204", "贵州省安顺市");
        CityCode.put("5205", "贵州省毕节市");
        CityCode.put("5206", "贵州省铜仁市");
        CityCode.put("5223", "贵州省黔西南布依族苗族自治州");
        CityCode.put("5226", "贵州省黔东南苗族侗族自治州");
        CityCode.put("5227", "贵州省黔南布依族苗族自治州");
        CityCode.put("5301", "云南省昆明市");
        CityCode.put("5303", "云南省曲靖市");
        CityCode.put("5304", "云南省玉溪市");
        CityCode.put("5305", "云南省保山市");
        CityCode.put("5306", "云南省昭通市");
        CityCode.put("5307", "云南省丽江市");
        CityCode.put("5308", "云南省普洱市");
        CityCode.put("5309", "云南省临沧市");
        CityCode.put("5323", "云南省楚雄彝族自治州");
        CityCode.put("5325", "云南省红河哈尼族彝族自治州");
        CityCode.put("5326", "云南省文山壮族苗族自治州");
        CityCode.put("5328", "云南省西双版纳傣族自治州");
        CityCode.put("5329", "云南省大理白族自治州");
        CityCode.put("5331", "云南省德宏傣族景颇族自治州");
        CityCode.put("5333", "云南省怒江傈僳族自治州");
        CityCode.put("5334", "云南省迪庆藏族自治州");
        CityCode.put("5401", "西藏自治区拉萨市");
        CityCode.put("5402", "西藏自治区日喀则市");
        CityCode.put("5403", "西藏自治区昌都市");
        CityCode.put("5404", "西藏自治区林芝市");
        CityCode.put("5422", "西藏自治区山南地区");
        CityCode.put("5424", "西藏自治区那曲地区");
        CityCode.put("5425", "西藏自治区阿里地区");
        CityCode.put("6101", "陕西省西安市");
        CityCode.put("6102", "陕西省铜川市");
        CityCode.put("6103", "陕西省宝鸡市");
        CityCode.put("6104", "陕西省咸阳市");
        CityCode.put("6105", "陕西省渭南市");
        CityCode.put("6106", "陕西省延安市");
        CityCode.put("6107", "陕西省汉中市");
        CityCode.put("6108", "陕西省榆林市");
        CityCode.put("6109", "陕西省安康市");
        CityCode.put("6110", "陕西省商洛市");
        CityCode.put("6201", "甘肃省兰州市");
        CityCode.put("6202", "甘肃省嘉峪关市");
        CityCode.put("6203", "甘肃省金昌市");
        CityCode.put("6204", "甘肃省白银市");
        CityCode.put("6205", "甘肃省天水市");
        CityCode.put("6206", "甘肃省武威市");
        CityCode.put("6207", "甘肃省张掖市");
        CityCode.put("6208", "甘肃省平凉市");
        CityCode.put("6209", "甘肃省酒泉市");
        CityCode.put("6210", "甘肃省庆阳市");
        CityCode.put("6211", "甘肃省定西市");
        CityCode.put("6212", "甘肃省陇南市");
        CityCode.put("6229", "甘肃省临夏回族自治州");
        CityCode.put("6230", "甘肃省甘南藏族自治州");
        CityCode.put("6301", "青海省西宁市");
        CityCode.put("6302", "青海省海东市");
        CityCode.put("6322", "青海省海北藏族自治州");
        CityCode.put("6323", "青海省黄南藏族自治州");
        CityCode.put("6325", "青海省海南藏族自治州");
        CityCode.put("6326", "青海省果洛藏族自治州");
        CityCode.put("6327", "青海省玉树藏族自治州");
        CityCode.put("6328", "青海省海西蒙古族藏族自治州");
        CityCode.put("6401", "宁夏回族自治区银川市");
        CityCode.put("6402", "宁夏回族自治区石嘴山市");
        CityCode.put("6403", "宁夏回族自治区吴忠市");
        CityCode.put("6404", "宁夏回族自治区固原市");
        CityCode.put("6405", "宁夏回族自治区中卫市");
        CityCode.put("6501", "新疆维吾尔自治区乌鲁木齐市");
        CityCode.put("6502", "新疆维吾尔自治区克拉玛依市");
        CityCode.put("6504", "新疆维吾尔自治区吐鲁番市");
        CityCode.put("6522", "新疆维吾尔自治区哈密地区");
        CityCode.put("6523", "新疆维吾尔自治区昌吉回族自治州");
        CityCode.put("6527", "新疆维吾尔自治区博尔塔拉蒙古自治州");
        CityCode.put("6528", "新疆维吾尔自治区巴音郭楞蒙古自治州");
        CityCode.put("6529", "新疆维吾尔自治区阿克苏地区");
        CityCode.put("6530", "新疆维吾尔自治区克孜勒苏柯尔克孜自治州");
        CityCode.put("6531", "新疆维吾尔自治区喀什地区");
        CityCode.put("6532", "新疆维吾尔自治区和田地区");
        CityCode.put("6540", "新疆维吾尔自治区伊犁哈萨克自治州");
        CityCode.put("6542", "新疆维吾尔自治区塔城地区");
        CityCode.put("6543", "新疆维吾尔自治区阿勒泰地区");
        CityCode.put("6590", "新疆维吾尔自治区自治区直辖县级行政区划");
    }

    /**
     * 身份证号掩码处理
     */
    public static String maskIdcard(String idCardNo) {
        if (StringUtils.isNotEmpty(idCardNo)) {
            // 把手机号中间4位掩码处理
            idCardNo = idCardNo.replaceAll("(\\d{4})\\d{11}(\\d{4})", "$1***********$2");
        }
        return idCardNo;
    }
}
