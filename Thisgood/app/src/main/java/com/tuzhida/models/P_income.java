package com.tuzhida.models;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Paul-Sartre on 2015/12/9.
 */
public class P_income extends DataSupport{
    private int id;
    private String name;
    private String type;
    private double coin;
    private long time;
    private String mark;
    private String creates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCreates() {
        return creates;
    }

    public void setCreates(String creates) {
        this.creates = creates;
    }
}
