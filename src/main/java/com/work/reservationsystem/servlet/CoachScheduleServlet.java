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
import com.work.reservationsystem.entity.CoachSchedule;
import com.work.reservationsystem.service.CoachScheduleService;
import com.work.reservationsystem.service.ShiftService;
import com.work.reservationsystem.vo.CoachScheduleVO;

public class CoachScheduleServlet {
    private final CoachScheduleService scheduleService = new CoachScheduleService();
    private final HelpScheduleDao helpScheduleDao = new HelpScheduleDao();
    private final ShiftService shiftService = new ShiftService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // シフトIDに対応する色のマッピングを追加
    private static final Map<Integer, String> SHIFT_ID_COLORS = new HashMap<Integer, String>() {{
        put(1, "#4CAF50");    // フォレストグリーン
        put(2, "#2196F3");    // スカイブルー
        put(3, "#FFA726");    // オレンジ
        put(4, "#7E57C2");    // ライトパープル
        put(5, "#26A69A");    // ターコイズ
        put(6, "#5C6BC0");    // インディゴ
        put(7, "#66BB6A");    // ライトグリーン
        put(8, "#42A5F5");    // ブライトブルー
        put(9, "#FF7043");    // ディープオレンジ
        put(10, "#7986CB");   // パープルグレー
    }};

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// 選択可能なすべてのシフトを取得
        request.setAttribute("shifts", shiftService.findAll());
        request.setAttribute("page", "/coach/schedule/list.jsp");
        request.getRequestDispatcher("/coach/schedule/list.jsp").forward(request, response);
    }

    public void listSchedules(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// 現在ログインしているコーチを取得
            Coach coach = (Coach) request.getSession().getAttribute("coach");
            if (coach == null) {
                writeJson(response, "未登录");
                return;
            }

            // 日付範囲を取得
            Date startDate = dateFormat.parse(request.getParameter("start"));
            Date endDate = dateFormat.parse(request.getParameter("end"));

            // スケジュール記録を検索
            List<CoachScheduleVO> schedules = scheduleService.findByCoachAndDateRange(coach.getId(), startDate, endDate);
            // Help記録を検索
            List<Date> helpDates = helpScheduleDao.findByDateRange(startDate, endDate);
            
            // 結果リストを作成
            List<Map<String, Object>> result = new ArrayList<>();
            
            // スケジュール記録を追加
            for (CoachScheduleVO schedule : schedules) {
                Map<String, Object> event = new HashMap<>();
                event.put("title", schedule.getShiftName());
                event.put("start", dateFormat.format(schedule.getScheduleDate()));
                
                // シフトIDに対応する色を使用、対応する色が見つからない場合はデフォルト色を使用
                String backgroundColor = SHIFT_ID_COLORS.getOrDefault(schedule.getShiftId(), "#67C23A");
                // 未確認の場合、透明度を追加
                if (schedule.getStatus() != 1) {
                    backgroundColor = backgroundColor + "60";
                }
                
                event.put("backgroundColor", backgroundColor);
                event.put("borderColor", backgroundColor);
                event.put("textColor", "#000000");
                event.put("scheduleId", schedule.getId());
                result.add(event);
            }
            
            // スケジュールされていない日付のHelp記録を追加
            for (Date helpDate : helpDates) {
            	// その日付にスケジュールが既にあるかチェック
                boolean hasSchedule = false;
                for (CoachScheduleVO schedule : schedules) {
                    if (dateFormat.format(schedule.getScheduleDate()).equals(dateFormat.format(helpDate))) {
                        hasSchedule = true;
                        break;
                    }
                }
                // スケジュールがない場合のみHelpを表示
                if (!hasSchedule) {
                    Map<String, Object> helpEvent = new HashMap<>();
                    helpEvent.put("title", "HELP");
                    helpEvent.put("start", dateFormat.format(helpDate));
                    helpEvent.put("backgroundColor", "#F56C6C");
                    helpEvent.put("borderColor", "#F56C6C");
                    helpEvent.put("textColor", "#FFFFFF");
                    helpEvent.put("isHelp", true);
                    result.add(helpEvent);
                }
            }
            
            // JSONデータを返す
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (ParseException e) {
            writeJson(response, "日期格式不正确");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// 現在ログインしているコーチを取得
            Coach coach = (Coach) request.getSession().getAttribute("coach");
            if (coach == null) {
                writeJson(response, "未登录");
                return;
            }

            // パラメータを解析
            CoachSchedule schedule = new CoachSchedule();
            schedule.setCoachId(coach.getId());
            schedule.setShiftId(Integer.parseInt(request.getParameter("shiftId")));
            schedule.setScheduleDate(dateFormat.parse(request.getParameter("scheduleDate")));

            // スケジュールを保存
            scheduleService.add(schedule);
            writeJson(response, true, "スケジュール登録成功");
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// 現在ログインしているコーチを取得
            Coach coach = (Coach) request.getSession().getAttribute("coach");
            if (coach == null) {
                writeJson(response, "ログインしていません");
                return;
            }

            Integer id = Integer.parseInt(request.getParameter("id"));
            scheduleService.confirm(id);
            writeJson(response, true, "確認成功");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// 現在ログインしているコーチを取得
            Coach coach = (Coach) request.getSession().getAttribute("coach");
            if (coach == null) {
                writeJson(response, "ログインしていません");
                return;
            }

            // 日付に基づいてスケジュールを削除
            String scheduleDateStr = request.getParameter("scheduleDate");
            if (scheduleDateStr == null || scheduleDateStr.trim().isEmpty()) {
                writeJson(response, "日付は空にできません");
                return;
            }
            
            Date scheduleDate = dateFormat.parse(scheduleDateStr);
            scheduleService.cancelByCoachAndDate(coach.getId(), scheduleDate);
            writeJson(response, true, "キャンセル成功");
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }

    public void getDaySchedule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// 現在ログインしているコーチを取得
            Coach coach = (Coach) request.getSession().getAttribute("coach");
            if (coach == null) {
                writeJson(response, "ログインしていません");
                return;
            }

            Date date = dateFormat.parse(request.getParameter("date"));
            List<CoachScheduleVO> schedules = scheduleService.findByCoachAndDate(coach.getId(), date);
            
            Map<String, Object> result = new HashMap<>();
            if (!schedules.isEmpty()) {
                CoachScheduleVO schedule = schedules.get(0);
                result.put("shiftName", schedule.getShiftName());
                result.put("status", schedule.getStatus());
            }
            
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (ParseException e) {
            writeJson(response, "日付フォーマットが正しくありません");
        } catch (Exception e) {
            writeJson(response, e.getMessage());
        }
    }
    
    private void writeJson(HttpServletResponse response, String message) throws IOException {
        writeJson(response, false, message);
    }

    private void writeJson(HttpServletResponse response, boolean success, String message) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);
        
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
