package com.dao;

import com.pojo.Book;

import java.util.List;

/**
 * --- Be Humble and Hungry ---
 *
 * @author McMeloxD
 * @date 2024/6/22
 * @desc
 */
public interface BorrowDao {
    //1添加书名，图书名不能重复
    void addBook(Book book);

    //2删除图书，如果图书有人正在借阅就不能删除
    void deleteBook(String bname);

    //3搜索图书，根据书名模糊查询
    void findBook(String bname);

    //4用户登录
    boolean login(String uname,String pwd);

    //5用户注册
    void register(String name,String pwd1,String pwd2);

    //6已登录用户借阅图书
    void borrow(String name);

    //7已登录用户查看自己所有借阅信息
    List<String> borrowInfo();

    //8已登录用户查询未归还图书信息
    List<String> notRuturn();

    //9已登录用户还书
    void returnBook(String name);
}
