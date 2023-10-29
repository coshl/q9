/**
 * Copyright 2019 bejson.com
 */
package com.summer.dao.entity.paixu;

/**
 * Auto-generated: 2019-01-15 15:29:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Score {

    private String name;
    private int score;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }
}