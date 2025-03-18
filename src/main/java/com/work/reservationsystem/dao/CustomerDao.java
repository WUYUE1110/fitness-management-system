package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.work.reservationsystem.entity.Customer;
import com.work.reservationsystem.util.DBUtil;

public class CustomerDao {

    /**
     * 会員の総数を取得
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM customer";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("会員数の取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * ページングで会員リストを取得
     */
    public List<Customer> findPage(int offset, int pageSize) {
        String sql = "SELECT * FROM customer LIMIT ?, ?";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCustomer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("会員のページング取得に失敗しました", e);
        }
        return list;
    }

    /**
     * ResultSetからCustomerオブジェクトを抽出
     */
    private Customer extractCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setPhone(rs.getString("phone"));
        customer.setGender(rs.getInt("gender"));
        customer.setAge(rs.getInt("age"));
        customer.setMemberFlag(rs.getInt("member_flag"));
        customer.setMemberExpireTime(rs.getTimestamp("member_expire_time"));
        customer.setCreateTime(rs.getTimestamp("create_time"));
        customer.setUpdateTime(rs.getTimestamp("update_time"));
        customer.setPassword(rs.getString("password"));
        customer.setEmail(rs.getString("email"));
        return customer;
    }

    /**
     * 会員を削除
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("会員の削除に失敗しました", e);
        }
    }

    /**
     * 電話番号で会員を検索
     */
    public Customer findByPhone(String phone) {
        String sql = "SELECT * FROM customer WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("会員の取得に失敗しました", e);
        }
        return null;
    }

    /**
     * メールアドレスで会員を検索
     */
    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("会員の取得に失敗しました", e);
        }
        return null;
    }

    /**
     * IDによる会員を検索
     */
    public Customer findById(Integer id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("会員の取得に失敗しました", e);
        }
        return null;
    }

    /**
     * 新しい会員を登録
     */
    public void insert(Customer customer) {
        String sql = "INSERT INTO customer (name, phone, password, email, gender, age, member_flag, create_time, update_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getPassword());
            stmt.setString(4, customer.getEmail());
            stmt.setInt(5, customer.getGender());
            stmt.setInt(6, customer.getAge());
            stmt.setInt(7, 0); // デフォルトのmember_flag
            
            stmt.executeUpdate();
            
            // 生成されたIDを取得
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    customer.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("会員登録に失敗しました", e);
        }
    }

    /**
     * ログイン (メールアドレス + パスワード)
     */
    public Customer login(String email, String password) {
        String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("会員の取得に失敗しました", e);
        }
        return null;
    }
}