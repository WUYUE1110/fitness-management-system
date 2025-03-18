package com.work.reservationsystem.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.entity.Coach;
import com.work.reservationsystem.service.CoachService;

public class CoachLoginServlet {
    private final CoachService coachService = new CoachService();

    /**
     * コーチのログイン処理
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        
        try {
            Coach coach = coachService.findByEmail(email);
            if (coach == null) {
                request.setAttribute("error", "メールアドレスが存在しません");
                request.getRequestDispatcher("/coach/login.jsp").forward(request, response);
                return;
            }

            // ログイン成功時、セッションにコーチ情報を保存
            request.getSession().setAttribute("coach", coach);
            response.sendRedirect("dispatch?className=CoachScheduleServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/coach/login.jsp").forward(request, response);
        }
    }

    /**
     * ログアウト処理
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        goToLogin(request, response);
    }

    /**
     * ログインページへリダイレクト
     */
    public void toLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        goToLogin(request, response);
    }

    /**
     * セッションを無効化してログイン画面へ移動
     */
    private void goToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/coach/login.jsp");
    }
}