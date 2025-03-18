package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.entity.EquipmentCategory;
import com.work.reservationsystem.service.EquipmentCategoryService;

public class EquipmentCategoryServlet {
    private final EquipmentCategoryService equipmentCategoryService = new EquipmentCategoryService();

    /**
     * カテゴリー一覧を取得して、リスト画面にフォワードする
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

        int total = equipmentCategoryService.count();
        List<EquipmentCategory> list = equipmentCategoryService.findPage(pageNum, pageSize);
        
        int pages = (total + pageSize - 1) / pageSize;
        
        request.setAttribute("categoryList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        
        request.setAttribute("menu", "equipment_category");
        request.setAttribute("page", "/admin/equipment/category/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * カテゴリー追加画面へフォワード
     */
    public void toAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("menu", "equipment_category");
        request.setAttribute("page", "/admin/equipment/category/add.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * 新しいカテゴリーを追加
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EquipmentCategory category = new EquipmentCategory();
        category.setName(request.getParameter("name"));
        category.setDescription(request.getParameter("description"));
        
        equipmentCategoryService.add(category);
        response.sendRedirect("dispatch?className=EquipmentCategoryServlet&methodName=list");
    }

    /**
     * カテゴリー編集画面へフォワード
     */
    public void toUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        EquipmentCategory category = equipmentCategoryService.findById(id);
        request.setAttribute("category", category);
        request.setAttribute("menu", "equipment_category");
        request.setAttribute("page", "/admin/equipment/category/update.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * カテゴリー情報を更新
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EquipmentCategory category = new EquipmentCategory();
        category.setId(Integer.parseInt(request.getParameter("id")));
        category.setName(request.getParameter("name"));
        category.setDescription(request.getParameter("description"));
        
        equipmentCategoryService.update(category);
        response.sendRedirect("dispatch?className=EquipmentCategoryServlet&methodName=list");
    }

    /**
     * カテゴリーを削除
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        equipmentCategoryService.delete(id);
        response.sendRedirect("dispatch?className=EquipmentCategoryServlet&methodName=list");
    }
}