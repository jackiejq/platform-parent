package com.slljr.finance.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/28.
 * @time: 16:44.
 */
public class HtmlUtil {

    /**
     * 得到网页中第一张图片的地址
     *
     * @param html
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2019/2/28 17:01
     * @version 1.0
     */
    public static String getFirstImgStr(String html) {
        // 获取图片src
        String first = "";
        //匹配字符串中的img标签
        Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");
        Matcher matcher = p.matcher(html);
        boolean hasPic = matcher.find();
        //判断是否含有图片
        if (hasPic) {
            //如果含有图片，那么持续进行查找，直到匹配不到
            while (hasPic) {
                //获取第二个分组的内容，也就是 (.*?)匹配到的
                String group = matcher.group(2);
                //匹配图片的地址
                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher matcher2 = srcText.matcher(group);
                if (matcher2.find()) {
                    //把获取到的图片地址添加到列表中
                    first = matcher2.group(3);
                    break;
                }
                //判断是否还有img标签
                hasPic = matcher.find();
            }

        }

        return first;
    }

    /**
     * 替换img标签
     *
     * @author uncle.quentin
     * @date   2019/2/28 20:34
     * @param   html
     * @return java.lang.String
     * @version 1.0
     */
    public static String replaceAllImgToEmpty(String html){
        return html.replaceAll("<img[^>]*/>", " ");
    }

}
