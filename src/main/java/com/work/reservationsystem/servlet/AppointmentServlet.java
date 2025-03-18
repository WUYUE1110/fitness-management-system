package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.service.AppointmentService;
import com.work.reservationsystem.vo.AppointmentVO;

public class AppointmentServlet {
    private final AppointmentService appointmentService = new AppointmentService();

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
            // デフォルト値を使用する
        }

        int total = appointmentService.count();
        List<AppointmentVO> list = appointmentService.findPage(pageNum, pageSize);
        
        int pages = (total + pageSize - 1) / pageSize;
        
        request.setAttribute("appointmentList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        
        request.setAttribute("menu", "appointment");
        request.setAttribute("page", "/admin/appointment/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }
} 