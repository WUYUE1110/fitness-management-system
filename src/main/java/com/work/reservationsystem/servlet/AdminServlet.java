package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.entity.Admin;
import com.work.reservationsystem.service.AdminService;

public class AdminServlet {
    private final AdminService adminService = new AdminService();

    /**
     * 管理者ログイン処理
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            // ログイン成功時、管理者情報をセッションに保存
            request.getSession().setAttribute("admin", admin);
            response.sendRedirect("admin/index.jsp");
        } else {
            request.setAttribute("error", "ユーザー名またはパスワードが間違っています");
            request.getRequestDispatcher("/admin/admin/login.jsp").forward(request, response);
        }
    }

    /**
     * 管理者一覧をページングで表示
     */
    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ページングパラメータを取得
        int pageNum = PageConstant.DEFAULT_PAGE_NUM;
        int pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        
        try {
            String pageNumStr = request.getParameter("pageNum");
            String pageSizeStr = request.getParameter("pageSize");
            
            if (pageNumStr != null && !pageNumStr.isEmpty()) {
                pageNum = Integer.parseInt(pageNumStr);
            }
            if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
                pageSize = Integer.parseInt(pageSizeStr);
            }
        } catch (NumberFormatException e) {
            // デフォルト値を使用
        }

        // 総レコード数とページングデータを取得
        int total = adminService.count();
        List<Admin> list = adminService.findPage(pageNum, pageSize);
        
        // 総ページ数を計算
        int pages = (total + pageSize - 1) / pageSize;
        
        // ページング関連パラメータをリクエストに設定
        request.setAttribute("adminList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        
        request.setAttribute("menu", "admin");
        request.setAttribute("page", "/admin/admin/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * 管理者追加画面へフォワード
     */
    public void toAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("menu", "admin");
        request.setAttribute("page", "/admin/admin/add.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * 新しい管理者を追加
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Admin admin = new Admin();
        admin.setUsername(request.getParameter("username"));
        admin.setPassword(request.getParameter("password"));
        admin.setRealName(request.getParameter("realName"));
        admin.setPhone(request.getParameter("phone"));
        admin.setEmail(request.getParameter("email"));
        
        adminService.add(admin);
        response.sendRedirect("dispatch?className=AdminServlet&methodName=list");
    }

    /**
     * 管理者編集画面へフォワード
     */
    public void toUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Admin admin = adminService.findById(id);
        request.setAttribute("admin", admin);
        
        request.setAttribute("menu", "admin");
        request.setAttribute("page", "/admin/admin/update.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * 管理者情報を更新
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Admin admin = new Admin();
        admin.setId(Integer.parseInt(request.getParameter("id")));
        admin.setUsername(request.getParameter("username"));
        admin.setPassword(request.getParameter("password"));
        admin.setRealName(request.getParameter("realName"));
        admin.setPhone(request.getParameter("phone"));
        admin.setEmail(request.getParameter("email"));
        
        adminService.update(admin);
        response.sendRedirect("dispatch?className=AdminServlet&methodName=list");
    }

    /**
     * 管理者を削除
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        adminService.delete(id);
        response.sendRedirect("dispatch?className=AdminServlet&methodName=list");
    }
}