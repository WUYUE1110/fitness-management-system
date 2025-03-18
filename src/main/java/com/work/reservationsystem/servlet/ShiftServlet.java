package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.entity.Shift;
import com.work.reservationsystem.service.ShiftService;

public class ShiftServlet {
    private final ShiftService shiftService = new ShiftService();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

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

        int total = shiftService.count();
        List<Shift> list = shiftService.findPage(pageNum, pageSize);
        
        int pages = (total + pageSize - 1) / pageSize;
        
        request.setAttribute("shiftList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        
        request.setAttribute("menu", "shift");
        request.setAttribute("page", "/admin/shift/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    public void toAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("menu", "shift");
        request.setAttribute("page", "/admin/shift/add.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Shift shift = new Shift();
            shift.setName(request.getParameter("name"));
            shift.setStartTime(timeFormat.parse(request.getParameter("startTime")));
            shift.setEndTime(timeFormat.parse(request.getParameter("endTime")));
            shift.setSalary(new BigDecimal(request.getParameter("salary")));
            shift.setDescription(request.getParameter("description"));
            
            shiftService.add(shift);
            response.sendRedirect("dispatch?className=ShiftServlet&methodName=list");
        } catch (ParseException e) {
            request.setAttribute("error", "時間形式が正しくありません");
            toAdd(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toAdd(request, response);
        }
    }

    public void toUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            Shift shift = shiftService.findById(id);
            if (shift == null) {
                throw new RuntimeException("更新対象のシフトが存在しません");
            }
            
            request.setAttribute("shift", shift);
            request.setAttribute("menu", "shift");
            request.setAttribute("page", "/admin/shift/update.jsp");
            request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }

    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Shift shift = new Shift();
            shift.setId(Integer.parseInt(request.getParameter("id")));
            shift.setName(request.getParameter("name"));
            shift.setStartTime(timeFormat.parse(request.getParameter("startTime")));
            shift.setEndTime(timeFormat.parse(request.getParameter("endTime")));
            shift.setSalary(new BigDecimal(request.getParameter("salary")));
            shift.setDescription(request.getParameter("description"));
            
            shiftService.update(shift);
            response.sendRedirect("dispatch?className=ShiftServlet&methodName=list");
        } catch (ParseException e) {
            request.setAttribute("error", "時間形式が正しくありません");
            toUpdate(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toUpdate(request, response);
        }
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(request.getParameter("id"));
            shiftService.delete(id);
            response.sendRedirect("dispatch?className=ShiftServlet&methodName=list");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            list(request, response);
        }
    }
}