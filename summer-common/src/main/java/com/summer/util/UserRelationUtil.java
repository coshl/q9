package com.summer.util;

import java.util.LinkedHashMap;

public class UserRelationUtil {
    /**
     * 直系亲属联系人
     */
    public static final LinkedHashMap<String, String> CONTACTS_FAMILY = new LinkedHashMap<String, String>();
    /**
     * 其他联系人
     */
    public static final LinkedHashMap<String, String> CONTACTS_OTHER = new LinkedHashMap<String, String>();

    static {
        CONTACTS_FAMILY.put("1", "父亲");
        CONTACTS_FAMILY.put("2", "母亲");
        CONTACTS_FAMILY.put("3", "儿子");
        CONTACTS_FAMILY.put("4", "女儿");
        CONTACTS_FAMILY.put("5", "配偶");
        CONTACTS_FAMILY.put("6", "兄弟");
        CONTACTS_FAMILY.put("7", "姐妹");
        CONTACTS_OTHER.put("1", "同学");
        CONTACTS_OTHER.put("2", "亲戚");
        CONTACTS_OTHER.put("3", "同事");
        CONTACTS_OTHER.put("4", "朋友");
        CONTACTS_OTHER.put("5", "其他");
    }
}
