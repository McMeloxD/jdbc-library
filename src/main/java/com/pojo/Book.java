package com.pojo;

/**
 * --- Be Humble and Hungry ---
 *
 * @author McMeloxD
 * @date 2024/6/22
 * @desc
 */
public class Book {
    private int Bid;
    private String bname;
    private float price;
    private String press; //出版社
    private String author; //作者
    private int number; //可借阅数量

    public Book(){}
    public Book( String bname, float price, String press, String author, int number) {
        this.bname = bname;
        this.price = price;
        this.press = press;
        this.author = author;
        this.number = number;
    }
    public Book(int id, String bname, float price, String press, String author, int number) {
        this.Bid = id;
        this.bname = bname;
        this.price = price;
        this.press = press;
        this.author = author;
        this.number = number;
    }

    public int getBid() {
        return Bid;
    }

    public void setBid(int id) {
        this.Bid = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return
                " 图书名:" + bname +
                " ,价格:" + price +
                " ,出版社:" + press +
                " ,作者:" + author +
                " ,可借阅数量:" + number;
    }
}
