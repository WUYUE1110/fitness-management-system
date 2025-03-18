package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.entity.Equipment;
import com.work.reservationsystem.entity.EquipmentCategory;
import com.work.reservationsystem.service.EquipmentCategoryService;
import com.work.reservationsystem.service.EquipmentService;
import com.work.reservationsystem.vo.EquipmentVO;

public class EquipmentServlet {
    private final EquipmentService equipmentService = new EquipmentService();
    private final EquipmentCategoryService categoryService = new EquipmentCategoryService();

    /**
     * 器具一覧を取得し、リスト画面にフォワードする
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

        int total = equipmentService.count();
        List<EquipmentVO> list = equipmentService.findPage(pageNum, pageSize);
        
        int pages = (total + pageSize - 1) / pageSize;
        
        request.setAttribute("equipmentList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        
        request.setAttribute("menu", "equipment");
        request.setAttribute("page", "/admin/equipment/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * 器具追加画面にフォワード
     */
    public void toAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 全てのカテゴリーを取得し、ドロップダウンに使用
        List<EquipmentCategory> categories = categoryService.findAll();
        request.setAttribute("categories", categories);
        
        request.setAttribute("menu", "equipment");
        request.setAttribute("page", "/admin/equipment/add.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * 新しい器具情報を追加
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Equipment equipment = new Equipment();
            equipment.setName(request.getParameter("name"));
            equipment.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            equipment.setDescription(request.getParameter("description"));
            equipment.setStatus(Integer.parseInt(request.getParameter("status")));
            
            equipmentService.add(equipment);
            response.sendRedirect("dispatch?className=EquipmentServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toAdd(request, response);
        }
    }

    /**
     * 器具情報の更新画面にフォワード
     */
    public void toUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            EquipmentVO equipment = equipmentService.findById(id);
            if (equipment == null) {
                throw new RuntimeException("実施しようとしているレッスンは存在しません");
            }
            
            // 全てのカテゴリーを取得し、ドロップダウンに使用
            List<EquipmentCategory> categories = categoryService.findAll();
            
            request.setAttribute("equipment", equipment);
            request.setAttribute("categories", categories);
            request.setAttribute("menu", "equipment");
            request.setAttribute("page", "/admin/equipment/update.jsp");
            request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }

    /**
     * 器具情報を更新
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Equipment equipment = new Equipment();
            equipment.setId(Integer.parseInt(request.getParameter("id")));
            equipment.setName(request.getParameter("name"));
            equipment.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            equipment.setDescription(request.getParameter("description"));
            equipment.setStatus(Integer.parseInt(request.getParameter("status")));
            
            equipmentService.update(equipment);
            response.sendRedirect("dispatch?className=EquipmentServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toUpdate(request, response);
        }
    }

    /**
     * 器具情報を削除
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            equipmentService.delete(id);
            response.sendRedirect("dispatch?className=EquipmentServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }
}