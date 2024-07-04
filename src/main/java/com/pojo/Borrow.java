package com.pojo;


import java.util.Date;

/**
 * --- Be Humble and Hungry ---
 *
 * @author McMeloxD
 * @date 2024/6/22
 * @desc
 */
public class Borrow {
    private int brid;
    private int uid;
    private int bid;
    private Date ptime;

    public Borrow(){}
    public Borrow(int uid, int bid, Date ptime) {
        this.uid = uid;
        this.bid = bid;
        this.ptime = ptime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public Date getPtime() {
        return ptime;
    }

    public void setPtime(Date ptime) {
        this.ptime = ptime;
    }
}
