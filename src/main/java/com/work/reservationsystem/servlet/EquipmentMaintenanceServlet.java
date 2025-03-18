package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.entity.Admin;
import com.work.reservationsystem.entity.EquipmentMaintenance;
import com.work.reservationsystem.service.EquipmentMaintenanceService;
import com.work.reservationsystem.service.EquipmentService;
import com.work.reservationsystem.vo.EquipmentMaintenanceVO;

public class EquipmentMaintenanceServlet {
    private final EquipmentMaintenanceService maintenanceService = new EquipmentMaintenanceService();
    private final EquipmentService equipmentService = new EquipmentService();

    /**
     * メンテナンス一覧を取得して、リスト画面にフォワードする
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

        int total = maintenanceService.count();
        List<EquipmentMaintenanceVO> list = maintenanceService.findPage(pageNum, pageSize);
        
        int pages = (total + pageSize - 1) / pageSize;
        
        request.setAttribute("maintenanceList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        
        // 画面表示用
        request.setAttribute("menu", "maintenance");
        request.setAttribute("page", "/admin/equipment/maintenance/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * メンテナンス追加画面へフォワード
     */
    public void toAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 全ての器具情報を取得してリクエストに設定
        request.setAttribute("equipments", equipmentService.findAll());
        request.setAttribute("menu", "maintenance");
        request.setAttribute("page", "/admin/equipment/maintenance/add.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * メンテナンス情報を追加
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            EquipmentMaintenance maintenance = new EquipmentMaintenance();
            maintenance.setEquipmentId(Integer.parseInt(request.getParameter("equipmentId")));
            maintenance.setDescription(request.getParameter("description"));
            
            // 現在ログインしている管理者IDを設定
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            maintenance.setAdminId(admin.getId());
            
            maintenanceService.add(maintenance);
            response.sendRedirect("dispatch?className=EquipmentMaintenanceServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toAdd(request, response);
        }
    }

    /**
     * メンテナンス情報の更新画面へフォワード
     */
    public void toUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            EquipmentMaintenanceVO maintenance = maintenanceService.findById(id);
            if (maintenance == null) {
                throw new RuntimeException("実施対象の進行記録が存在しません");
            }
            
            request.setAttribute("maintenance", maintenance);
            request.setAttribute("equipments", equipmentService.findAll());
            request.setAttribute("menu", "maintenance");
            request.setAttribute("page", "/admin/equipment/maintenance/update.jsp");
            request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }

    /**
     * メンテナンス情報を更新
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            EquipmentMaintenance maintenance = new EquipmentMaintenance();
            maintenance.setId(Integer.parseInt(request.getParameter("id")));
            maintenance.setEquipmentId(Integer.parseInt(request.getParameter("equipmentId")));
            maintenance.setDescription(request.getParameter("description"));
            
            maintenanceService.update(maintenance);
            response.sendRedirect("dispatch?className=EquipmentMaintenanceServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toUpdate(request, response);
        }
    }

    /**
     * メンテナンスを完了扱いに変更
     */
    public void finish(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            maintenanceService.finish(id);
            response.sendRedirect("dispatch?className=EquipmentMaintenanceServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }
}