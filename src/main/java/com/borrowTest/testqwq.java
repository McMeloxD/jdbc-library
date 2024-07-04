package com.borrowTest;

import com.dao.BorrowDao;
import com.dao.BorrowDaoImpl2;
import com.pojo.Book;

import java.util.List;
import java.util.Scanner;

/**
 * --- Be Humble and Hungry ---
 *
 * @author McMeloxD
 * @date 2024/6/22
 * @desc
 */
public class testqwq {
    public static void main(String[] args) {
        BorrowDaoImpl2 br = new BorrowDaoImpl2();
        while (true) {
            System.out.println("******************************");
            System.out.println("图书借阅系统  当前登录用户:" + br.getUser().getUname());
            System.out.println("******************************");
            System.out.println("1.添加图书");
            System.out.println("2.删除图书");
            System.out.println("3.搜索图书");
            System.out.println("4.用户注册");
            System.out.println("5.用户登录");
            System.out.println("6.产看已借阅图书信息");
            System.out.println("7.查看未归还图书信息");
            System.out.println("8.借书");
            System.out.println("9.还书");
            System.out.println("0.退出系统");
            System.out.println("******************************");
            System.out.print("请选择需要的功能:");
            Scanner sc = new Scanner(System.in);
            int input = sc.nextInt();
            switch (input) {
                case 0:
                    System.out.println("系统已退出~");
                    System.exit(0);
                    break;
                case 1:
                    System.out.print("请输入要添加的书名：");
                    String bname = sc.next();
                    System.out.print("请输入图书的价格：");
                    float price = sc.nextFloat();
                    System.out.print("请输入图书的出版社：");
                    String press = sc.next();
                    System.out.print("请输入图书的作者：");
                    String author = sc.next();
                    System.out.print("请输入图书的数量：");
                    int number = sc.nextInt();
                    br.addBook(new Book(bname, price, press, author, number));
                    break;
                case 2:
                    System.out.print("请输入要删除的图书名称：");
                    String deleteName = sc.next();
                    br.deleteBook(deleteName);
                    break;
                case 3:
                    System.out.print("请输入要查询的图书名：");
                    String findName = sc.next();
                    br.findBook(findName);
                    break;
                case 4:
                    System.out.print("请输入用户名：");
                    String rigName = sc.next();
                    System.out.print("请输入密码：");
                    String rigPwd1 = sc.next();
                    System.out.print("请再次输入密码：");
                    String rigPwd2 = sc.next();
                    br.register(rigName, rigPwd1, rigPwd2);
                    break;
                case 5:
                    System.out.print("请输入用户名：");
                    String loginName = sc.next();
                    System.out.print("请输入密码：");
                    String loginPwd = sc.next();
                    boolean b = br.login(loginName, loginPwd);
                    // 登陆成功保存登录信息
                    if (b) {
                        br.setLogin(true);
                        System.out.println(br.getUser().getUid());
                    }
                    break;
                case 6:
                    // 先判断是否登录
                    if (!br.isLogin()) {
                        System.out.println("请登陆后再使用查询功能~");
                        break;
                    } else {
                        // 获取结果集合
                        List<String> brInfo = br.borrowInfo();
                        // 打印
                        brInfo.forEach(x -> System.out.println(x));
                    }
                    break;
                case 7:
                    // 先判断是否登录
                    if (!br.isLogin()) {
                        System.out.println("请登陆后再使用查询功能~");
                        break;
                    } else {
                        // 获取结果集合
                        List<String> notBInfo = br.notRuturn();
                        // 打印
                        notBInfo.forEach(x -> System.out.println(x));
                    }
                    break;
                case 8:
                    // 先判断是否登录
                    if (!br.isLogin()) {
                        System.out.println("请登陆后再使用借阅功能~");
                        break;
                    } else {
                        System.out.print("请输入您要借阅的图书名：");
                        String bookName = sc.next();
                        br.borrow(bookName);
                    }
                    break;
                case 9:
                    // 先判断是否登录
                    if (!br.isLogin()) {
                        System.out.println("请登陆后再使用还书功能~");
                        break;
                    } else {
                        System.out.print("请输入您要还的图书名：");
                        String bookName = sc.next();
                        br.returnBook(bookName);
                    }
                    break;
            }
        }
    }
}
