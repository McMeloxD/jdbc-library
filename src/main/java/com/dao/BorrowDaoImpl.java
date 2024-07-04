package com.dao;

import com.pojo.Book;
import com.pojo.User;

import java.sql.*;
import java.util.*;


/**
 * --- Be Humble and Hungry ---
 *
 * @author McMeloxD
 * @date 2024/6/22
 * @desc
 */
public class BorrowDaoImpl implements BorrowDao {
    private boolean isLogin;// 是否登录
    private User user;// 当前登录的用户名

    // 返回当前用户信息
    public User getUser() {
        return user;
    }

    // 保存用户信息
    public void setUser(User user) {
        this.user = user;
    }

    // 返回当前登录状态
    public boolean isLogin() {
        return isLogin;
    }

    // 设置登录状态
    public void setLogin(boolean login) {
        isLogin = login;
    }


    // 实例化即创建数据库连接
    public BorrowDaoImpl() {
        user = new User();
        user.setUname("XXXX");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/library?characterEncoding=UTF-8",
                "root", "666888");
    }

    /*添加图书*/
    public void addBook(Book book) {
        // 先查询是否有已存在的图书
        String sql = "select bname from book where bname = ?";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql)) {
            ps1.setString(1, book.getBname());
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals(book.getBname())) {
                    System.out.println("亲，图书馆已经有这本书呢！~");
                    return;
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        // 如果不存在就插入
        String sql2 = "insert into book(bname,price,press,author,number) values(?,?,?,?,?)";
        try (Connection c = getConnection(); PreparedStatement ps2 = c.prepareStatement(sql2)) {
            ps2.setString(1, book.getBname());
            ps2.setFloat(2, book.getPrice());
            ps2.setString(3, book.getPress());
            ps2.setString(4, book.getAuthor());
            ps2.setInt(5, book.getNumber());
            int n = ps2.executeUpdate();
            if (n > 0)
                System.out.println("添加图书" + book.getBname() + "成功~");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    /*删除图书*/
    public void deleteBook(String bname) {
        // 先查询是否有人在借阅这本书
        String sql1 = "select w.* from borrow w,book k where w.bid = k.bid and k.bname = ? and w.rtime is null";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql1)) {
            ps1.setString(1, bname);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                System.out.println("这本书有人正在借阅无法删除哦！");
                return;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        // 没人正在借阅就可以删
        String sql2 = "delete from book where bname = ?";
        try (Connection c = getConnection(); PreparedStatement ps2 = c.prepareStatement(sql2)) {
            ps2.setString(1, bname);
            int n = ps2.executeUpdate();
            System.out.println(n);
            if (n > 0) {
                System.out.println("删除图书" + bname + "成功~");
            } else System.out.println("图书不存在哦");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    /*模糊查询图书*/
    public void findBook(String name) {
        // 根据输入的字符串模糊查询 这里模糊查询要用like
        String sql = "select * from book where bname like ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, '%' + name + '%');
            ResultSet rs = ps.executeQuery();
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(new Book(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
            }
            // 如果是空集合就说明没这书
            if (books.isEmpty()) {
                System.out.println("名称中包含" + name + "的图书不存在~");
            } else {
                // 打印
                System.out.println("书名包含" + name + "的图书有：");
                books.forEach(x -> System.out.println(x));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /*登录*/
    public boolean login(String name, String pwd) {
        // 先查询是否有这个用户
        String sql1 = "select * from user where uname = ?";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql1)) {
            ps1.setString(1, name);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) {
                System.out.println("用户名不存在！");
                return false;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        String sql2 = "select * from user where uname = ? and password = ?";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql2)) {
            ps1.setString(1, name);
            ps1.setString(2, pwd);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) {
                System.out.println("密码错误！");
                return false;
            } else {
                // 登录成功将当前用户信息保存
                System.out.println("登录成功!" + name + "欢迎回家~");
                user.setUid(rs.getInt(1));
                user.setUname(rs.getString(2));
                user.setPassword(rs.getString(3));
                // 返回
                return true;

            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    /*注册*/
    public void register(String name, String pwd1, String pwd2) {
        // 先判断两次输入的密码是否不同
        if (!pwd1.equals(pwd2)) {
            System.out.println("两次输入的密码不一致，请重试！");
            return;
        }
        // 再判断是否已经存在这个用户名
        // 先查询是否有人在借阅这本书
        String sql1 = "select * from user where uname = ?";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql1)) {
            ps1.setString(1, name);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                System.out.println("该用户名已经注册！请重试~");
                return;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        // 两次判断都没问题再注册
        // 如果不存在就插入
        String sql2 = "insert into user(uname,password) values(?,?)";
        try (Connection c = getConnection(); PreparedStatement ps2 = c.prepareStatement(sql2)) {
            ps2.setString(1, name);
            ps2.setString(2, pwd1);
            int n = ps2.executeUpdate();
            if (n > 0)
                System.out.println("用户" + name + "注册成功！请登录~");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    /*借阅*/
    public void borrow(String bookName) {
        // 先判断当前用户目前是否已经借阅了相同的图书
        String sql1 = "select w.* from borrow w,book k \n" +
                "where w.bid = k.bid and k.bname = ? and w.rtime is null and w.uid =(select uid from user where uname = ?)\n";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql1)) {
            ps1.setString(1, bookName);
            ps1.setString(2, user.getUname());
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                System.out.println("您之前借阅过这本" + bookName + "仍未归还,不能再借相同的书哦！");
                return;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        // 没借过这本书所以再判断数量是否有余
        String sql2 = "select bid,number from book where bname = ?";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql2)) {
            ps1.setString(1, bookName);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                if (rs.getInt(2) > 0) {
                    // 可以借阅，数量-1
                    // 再保存一下这本书的bid
                    int bid = rs.getInt(1);
                    String sql3 = "update book set number = number -1 where bname = ?";
                    try (Connection c2 = getConnection(); PreparedStatement ps3 = c2.prepareStatement(sql3)) {
                        ps3.setString(1, bookName);
                        ps3.executeUpdate();
                    } catch (SQLException e3) {
                        e3.printStackTrace();
                    }
                    // 再插入借阅记录
                    String sql4 = "insert into borrow(uid,bid,btime) values(?,?,?)";
                    try (Connection c3 = getConnection(); PreparedStatement ps4 = c3.prepareStatement(sql4)) {
                        // 用setDate只能传日期，时间未0，用这个Timestamp可以传日期+准确时间
                        Timestamp time = new java.sql.Timestamp(new java.util.Date().getTime());
                        ps4.setInt(1, this.getUser().getUid());
                        ps4.setInt(2, bid);
                        ps4.setTimestamp(3, time);
                        int n = ps4.executeUpdate();
                        if (n > 0) {
                            System.out.println("您已成功借阅" + bookName + ",记得按时归还哦~");
                        }
                    } catch (SQLException e3) {
                        e3.printStackTrace();
                    }
                } else System.out.println(bookName + "这本书已经被借阅完了，换一本吧~");
            } else System.out.println("没有这本书哦~");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    /*还书*/
    public void returnBook(String name) {
        // 先判断当前用户目前是否已经借阅了该书且未归还
        String sql1 = "select w.* from borrow w,book k \n" +
                "where w.bid = k.bid and k.bname = ? and w.rtime is null and w.uid =(select uid from user where uname = ?)\n";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql1)) {
            ps1.setString(1, name);
            ps1.setString(2, user.getUname());
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                // 获取一下借阅记录的id
                int brid = rs.getInt(1);
                // 借过没还就改数量++
                String sql2 = "update book set number = number + 1 where bname = ?";
                try (Connection c2 = getConnection(); PreparedStatement ps3 = c2.prepareStatement(sql2)) {
                    ps3.setString(1, name);
                    ps3.executeUpdate();
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
                // 再修改借阅记录
                String sql3 = "update borrow set rtime = ? where brid = ?";
                try (Connection c3 = getConnection(); PreparedStatement ps4 = c3.prepareStatement(sql3)) {
                    Timestamp time = new java.sql.Timestamp(new java.util.Date().getTime());
                    ps4.setTimestamp(1, time);
                    ps4.setInt(2, brid);
                    int n = ps4.executeUpdate();
                    if (n > 0) {
                        System.out.println("您已成功归还" + name + "~");
                    }
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
            } else {
                System.out.println("您当前没有在借阅该书哦~");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    /*所有借阅信息*/
    public List<String> borrowInfo() {
        // 先判断当前用户目前是否已经借阅了该书且未归还
        String sql = "select b.bid,b.bname,b.author,b.price,r.btime,if(r.rtime is null,'否','是') '是否归还' from book b,borrow r\n" +
                "where b.bid = r .bid and uid = ?";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql)) {
            ps1.setInt(1, user.getUid());
            ResultSet rs = ps1.executeQuery();
            ArrayList<String> notRInfo = new ArrayList<>();
            while (rs.next()) {
                notRInfo.add("图书ID：" + rs.getInt(1) + ", 书名：" + rs.getString(2) + ", 作者：" + rs.getString(3) +
                        ", 价格：" + rs.getFloat(4) + ", 借阅时间：" + rs.getDate(5) + ", 是否归还：" + rs.getString(6));
            }
            // 如果是空集合就说明没这书
            if (notRInfo.isEmpty()) {
                System.out.println("您目前没有来借过图书哦~");
            } else {
                // 打印
                System.out.println(user.getUname() + "您好，您在本图书馆借阅的记录为：");
                return notRInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*当前未还信息*/
    public List<String> notRuturn() {
        // 先判断当前用户目前是否已经借阅了该书且未归还
        String sql = "select b.bid,b.bname,b.author,b.price,r.btime from book b,borrow r\n" +
                "where b.`bid` = r.`bid` and uid = ? and rtime is null order by r.btime desc";
        try (Connection c = getConnection(); PreparedStatement ps1 = c.prepareStatement(sql)) {
            ps1.setInt(1, user.getUid());
            ResultSet rs = ps1.executeQuery();
            ArrayList<String> notRInfo = new ArrayList<>();
            while (rs.next()) {
                notRInfo.add("图书ID：" + rs.getInt(1) + ", 书名：" + rs.getString(2) + ", 作者：" + rs.getString(3) +
                        ", 价格：" + rs.getFloat(4) + ", 借阅日期：" + rs.getDate(5));
            }
            // 如果是空集合就说明没这书
            if (notRInfo.isEmpty()) {
                System.out.println("您目前没有未归还的图书哦~");
            } else {
                // 打印
                System.out.println(user.getUname() + "您好，您当前未归还的图书有：");
                return notRInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
