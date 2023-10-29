package com.summer.util;

import java.util.Comparator;
import java.util.Map;

public class ValueComparatorUtil implements Comparator<String> {

    Map<String, Float> base;

    public ValueComparatorUtil(Map<String, Float> base) {
        this.base = base;
    }

    @Override
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }

}
