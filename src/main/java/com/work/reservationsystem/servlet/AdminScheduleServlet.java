package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.work.reservationsystem.dao.HelpScheduleDao;
import com.work.reservationsystem.entity.Coach;
import com.work.reservationsystem.entity.Shift;
import com.work.reservationsystem.service.CoachScheduleService;
import com.work.reservationsystem.service.CoachService;
import com.work.reservationsystem.service.ShiftService;
import com.work.reservationsystem.vo.CoachScheduleVO;

public class AdminScheduleServlet {
    private final CoachScheduleService scheduleService = new CoachScheduleService();
    private final HelpScheduleDao helpScheduleDao = new HelpScheduleDao();
    private final CoachService coachService = new CoachService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final ShiftService shiftService = new ShiftService();
    
    // シフトIDに対応する色マッピングを追加
    private static final Map<Integer, String> SHIFT_ID_COLORS = new HashMap<Integer, String>() {{
        put(1, "#4CAF50");    // フォレストグリーン
        put(2, "#2196F3");    // スカイブルー
        put(3, "#FFA726");    // オレンジ
        put(4, "#7E57C2");    // ラベンダー
        put(5, "#26A69A");    // ティール
        put(6, "#5C6BC0");    // インディゴ
        put(7, "#66BB6A");    // ライトグリーン
        put(8, "#42A5F5");    // ブライトブルー
        put(9, "#FF7043");    // ダークオレンジ
        put(10, "#7986CB");   // グレイッシュパープル
    }};
    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// すべてのコーチを取得
        List<Coach> coaches = coachService.findAll();
        request.setAttribute("coaches", coaches);
        
        // すべてのシフトを取得
        List<Shift> shifts = shiftService.findAll();
        request.setAttribute("shifts", shifts);
        
        request.setAttribute("menu", "schedule");
        request.setAttribute("page", "/admin/schedule/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }
    public void listSchedules(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Date startDate = dateFormat.parse(request.getParameter("start"));
            Date endDate = dateFormat.parse(request.getParameter("end"));

            List<CoachScheduleVO> schedules = scheduleService.findByDateRange(startDate, endDate);
            List<Date> helpDates = helpScheduleDao.findByDateRange(startDate, endDate);
            
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (CoachScheduleVO schedule : schedules) {
                Map<String, Object> event = new HashMap<>();
                event.put("title", schedule.getShiftName() + " " + schedule.getCoachName());
                event.put("start", dateFormat.format(schedule.getScheduleDate()));
                
                String backgroundColor = SHIFT_ID_COLORS.getOrDefault(schedule.getShiftId(), "#67C23A");
                if (schedule.getStatus() != 1) {
                    backgroundColor = backgroundColor + "60";
                }
                
                event.put("backgroundColor", backgroundColor);
                event.put("borderColor", backgroundColor);
                event.put("textColor", "#000000");
                result.add(event);
            }

//    public void listSchedules(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            // 获取日期范围
//            Date startDate = dateFormat.parse(request.getParameter("start"));
//            Date endDate = dateFormat.parse(request.getParameter("end"));
//
//            // 查询所有教练的排班记录
//            List<CoachScheduleVO> schedules = scheduleService.findByDateRange(startDate, endDate);
//            // 查询Help记录
//            List<Date> helpDates = helpScheduleDao.findByDateRange(startDate, endDate);
//            
//            // 创建结果列表
//            List<Map<String, Object>> result = new ArrayList<>();
//            
//            // 添加排班记录
//            for (CoachScheduleVO schedule : schedules) {
//                Map<String, Object> event = new HashMap<>();
//                event.put("title", schedule.getShiftName() + " " + schedule.getCoachName());
//                event.put("start", dateFormat.format(schedule.getScheduleDate()));
//                event.put("backgroundColor", schedule.getStatus() == 1 ? "#67C23A" : "#E6A23C");
//                event.put("borderColor", schedule.getStatus() == 1 ? "#67C23A" : "#E6A23C");
//                event.put("textColor", "#000000");
//                result.add(event);
//            }
//            
            // Help 記録を追加
            for (Date helpDate : helpDates) {
                Map<String, Object> helpEvent = new HashMap<>();
                helpEvent.put("title", "HELP");
                helpEvent.put("start", dateFormat.format(helpDate));
                helpEvent.put("backgroundColor", "#F56C6C");
                helpEvent.put("borderColor", "#F56C6C");
                helpEvent.put("textColor", "#FFFFFF");
                helpEvent.put("isHelp", true);
                result.add(helpEvent);
            }
            
            // JSONデータを返す
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void getDaySchedules(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Date date = dateFormat.parse(request.getParameter("date"));
            
            // 当日のシフト記録を取得
            List<CoachScheduleVO> schedules = scheduleService.findByDate(date);
            // Help マークがあるか確認
            boolean hasHelp = helpScheduleDao.existsByDate(date);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("schedules", schedules);
            result.put("hasHelp", hasHelp);
            
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void addHelp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// 日付を解析
            Date scheduleDate = dateFormat.parse(request.getParameter("date"));
            
            // 既にシフトまたはHelpがあるか確認
            List<CoachScheduleVO> existingSchedules = scheduleService.findByDate(scheduleDate);
            if (!existingSchedules.isEmpty() || helpScheduleDao.existsByDate(scheduleDate)) {
                writeJson(response, "この日付にはすでにシフトまたはHelpマークがあります");
                return;
            }

            // Helpマークを追加
            helpScheduleDao.insert(scheduleDate);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Helpの追加に成功しました");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void removeHelp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// 日付を解析
            Date scheduleDate = dateFormat.parse(request.getParameter("date"));
            
            // Helpマークが存在するか確認
            if (!helpScheduleDao.existsByDate(scheduleDate)) {
                writeJson(response, "Helpマークが存在していません");
                return;
            }

            // Helpマークを削除
            helpScheduleDao.delete(scheduleDate);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Helpの削除に成功しました");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void assignShift(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String dateStr = request.getParameter("date");
            Integer coachId = Integer.parseInt(request.getParameter("coachId"));
            Integer shiftId = Integer.parseInt(request.getParameter("shiftId"));
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date scheduleDate = sdf.parse(dateStr);
            
            // サービス層を呼び出してシフトを追加
            scheduleService.assignShift(coachId, shiftId, scheduleDate);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "シフトの割り当てに成功しました");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    private void writeJson(HttpServletResponse response, String message) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
} 