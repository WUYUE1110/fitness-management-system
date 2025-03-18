package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.entity.Coach;
import com.work.reservationsystem.entity.Equipment;
import com.work.reservationsystem.service.CoachService;
import com.work.reservationsystem.service.EquipmentService;
import com.work.reservationsystem.vo.CoachVO;

public class CoachServlet {
    private final CoachService coachService = new CoachService();
    private final EquipmentService equipmentService = new EquipmentService();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * コーチ一覧を取得してリストページへ転送
     */
    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        int total = coachService.count();
        List<CoachVO> list = coachService.findPage(pageNum, pageSize);
        
        int pages = (total + pageSize - 1) / pageSize;
        
        request.setAttribute("coachList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        
        // 画面表示用
        request.setAttribute("menu", "coach");
        request.setAttribute("page", "/admin/coach/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * コーチ登録画面へ転送
     */
    public void toAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 利用可能な器具を全て取得し、ドロップダウンに使用
        List<Equipment> equipments = equipmentService.findAll();
        request.setAttribute("equipments", equipments);
        
        request.setAttribute("menu", "coach");
        request.setAttribute("page", "/admin/coach/add.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * コーチ情報を新規登録
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Coach coach = new Coach();
            coach.setName(request.getParameter("name"));
            coach.setGender(Integer.parseInt(request.getParameter("gender")));
            
            coach.setPhone(request.getParameter("phone"));
            coach.setEmail(request.getParameter("email"));
            
            String equipmentId = request.getParameter("equipmentId");
            if (equipmentId != null && !equipmentId.isEmpty()) {
                coach.setEquipmentId(Integer.parseInt(equipmentId));
            }
            coach.setHireDate(DATE_FORMAT.parse(request.getParameter("hireDate")));
            coach.setType(Integer.parseInt(request.getParameter("type")));
            coach.setStatus(Integer.parseInt(request.getParameter("status")));
            coach.setDescription(request.getParameter("description"));
            coach.setAvatar(request.getParameter("avatar"));
            
            coachService.add(coach);
            response.sendRedirect("dispatch?className=CoachServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toAdd(request, response);
        }
    }

    /**
     * コーチ情報更新画面へ転送
     */
    public void toUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            CoachVO coach = coachService.findById(id);
            if (coach == null) {
                throw new RuntimeException("従業員が存在しません");
            }
            
            // コーチが使用可能な器具一覧を取得
            List<Equipment> equipments = equipmentService.findAll();
            
            request.setAttribute("coach", coach);
            request.setAttribute("equipments", equipments);
            request.setAttribute("menu", "coach");
            request.setAttribute("page", "/admin/coach/update.jsp");
            request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }

    /**
     * コーチ情報を更新
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Coach coach = new Coach();
            coach.setId(Integer.parseInt(request.getParameter("id")));
            coach.setName(request.getParameter("name"));
            coach.setGender(Integer.parseInt(request.getParameter("gender")));
            
            coach.setPhone(request.getParameter("phone"));
            coach.setEmail(request.getParameter("email"));
            coach.setEquipmentId(Integer.parseInt(request.getParameter("equipmentId")));
            coach.setHireDate(DATE_FORMAT.parse(request.getParameter("hireDate")));
            coach.setType(Integer.parseInt(request.getParameter("type")));
            coach.setStatus(Integer.parseInt(request.getParameter("status")));
            coach.setDescription(request.getParameter("description"));
            
            // 新しいアバターが指定されている場合のみ更新
            String avatar = request.getParameter("avatar");
            if (avatar != null && !avatar.trim().isEmpty()) {
                coach.setAvatar(avatar);
            }
            
            coachService.update(coach);
            response.sendRedirect("dispatch?className=CoachServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toUpdate(request, response);
        }
    }

    /**
     * コーチ情報を削除
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            coachService.delete(id);
            response.sendRedirect("dispatch?className=CoachServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }
}