package com.work.reservationsystem.service;

import java.util.List;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.AdminDao;
import com.work.reservationsystem.entity.Admin;

public class AdminService {
    private final AdminDao adminDao = new AdminDao();

    /**
     * ログイン処理
     */
    public Admin login(String username, String password) {
        return adminDao.login(username, password);
    }

    /**
     * すべての管理者を取得
     */
    public List<Admin> findAll() {
        return adminDao.findAll();
    }

    /**
     * IDにより管理者を検索
     */
    public Admin findById(Integer id) {
        return adminDao.findById(id);
    }

    /**
     * 新しい管理者を追加
     */
    public void add(Admin admin) {
        adminDao.insert(admin);
    }

    /**
     * 管理者情報を更新
     */
    public void update(Admin admin) {
        // パラメータ検証
        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            throw new RuntimeException("ユーザー名は必須です");
        }
        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            throw new RuntimeException("パスワードは必須です");
        }
        if (admin.getRealName() == null || admin.getRealName().trim().isEmpty()) {
            throw new RuntimeException("名前は必須です");
        }
        if (admin.getPhone() == null || admin.getPhone().trim().isEmpty()) {
            throw new RuntimeException("電話番号は必須です");
        }
        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            throw new RuntimeException("メールアドレスは必須です");
        }
        
        adminDao.update(admin);
    }

    /**
     * 管理者を削除
     */
    public void delete(Integer id) {
        adminDao.delete(id);
    }

    /**
     * 管理者の総数をカウント
     */
    public int count() {
        return adminDao.count();
    }

    /**
     * ページングパラメータで管理者一覧を取得
     */
    public List<Admin> findPage(int pageNum, int pageSize) {
        // パラメータ検証
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }

        // オフセットを計算
        int offset = (pageNum - 1) * pageSize;
        return adminDao.findPage(offset, pageSize);
    }
}