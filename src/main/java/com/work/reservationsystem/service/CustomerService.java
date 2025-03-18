package com.work.reservationsystem.service;

import java.util.List;
import java.util.regex.Pattern;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.CustomerDao;
import com.work.reservationsystem.entity.Customer;

public class CustomerService {
    private final CustomerDao customerDao = new CustomerDao();
    // 携帯番号用の正規表現：^[0-9-]{10,13}$
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9-]{10,13}$");

    /**
     * 顧客の総数を取得
     */
    public int count() {
        return customerDao.count();
    }

    /**
     * 指定ページ番号とページサイズで顧客リストを取得
     */
    public List<Customer> findPage(int pageNum, int pageSize) {
        // パラメータ検証
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }

        // オフセットを計算
        int offset = (pageNum - 1) * pageSize;
        return customerDao.findPage(offset, pageSize);
    }

    /**
     * 顧客を削除
     */
    public void delete(Integer id) {
        customerDao.delete(id);
    }

    /**
     * IDによって顧客を検索
     */
    public Customer findById(Integer id) {
        return customerDao.findById(id);
    }

    /**
     * ログイン処理
     */
    public Customer login(String email, String password) {
        // パラメータ検証
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("メールアドレスは必須です");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("パスワードは必須です");
        }

        // メールアドレス形式を検証
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("メールアドレスの形式が正しくありません");
        }

        return customerDao.login(email, password);
    }

    /**
     * 会員登録処理
     */
    public void register(Customer customer) {
        // パラメータ検証
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new RuntimeException("氏名は必須です");
        }
        if (customer.getPhone() == null || !PHONE_PATTERN.matcher(customer.getPhone()).matches()) {
            throw new RuntimeException("正しい携帯番号を入力してください");
        }
        if (customer.getPassword() == null || customer.getPassword().length() < 6) {
            throw new RuntimeException("パスワードは6文字以上にしてください");
        }
        if (customer.getGender() != 0 && customer.getGender() != 1) {
            throw new RuntimeException("性別を正しく選択してください");
        }
        if (customer.getAge() < 1 || customer.getAge() > 120) {
            throw new RuntimeException("年齢を正しく入力してください");
        }

        // 携帯番号がすでに登録されているか確認
        if (customerDao.findByPhone(customer.getPhone()) != null) {
            throw new RuntimeException("この携帯番号は既に登録されています");
        }

        customerDao.insert(customer);
    }
}