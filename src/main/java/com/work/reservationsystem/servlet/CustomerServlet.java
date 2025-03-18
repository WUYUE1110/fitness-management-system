package com.work.reservationsystem.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.entity.Appointment;
import com.work.reservationsystem.entity.Customer;
import com.work.reservationsystem.entity.Equipment;
import com.work.reservationsystem.service.AppointmentService;
import com.work.reservationsystem.service.CoachService;
import com.work.reservationsystem.service.CustomerService;
import com.work.reservationsystem.service.EquipmentService;
import com.work.reservationsystem.vo.AppointmentVO;
import com.work.reservationsystem.vo.CoachVO;

public class CustomerServlet {
    private final CustomerService customerService = new CustomerService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final CoachService coachService = new CoachService();
    private final EquipmentService equipmentService = new EquipmentService();

    /**
     * ページングパラメータを取得し、顧客一覧を取得してリスト画面にフォワード
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
        int total = customerService.count();
        List<Customer> list = customerService.findPage(pageNum, pageSize);

        // 総ページ数を計算
        int pages = (total + pageSize - 1) / pageSize;

        // ページング関連パラメータを設定
        request.setAttribute("customerList", list);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);

        request.setAttribute("menu", "customer");
        request.setAttribute("page", "/admin/customer/list.jsp");
        request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
    }

    /**
     * 顧客情報を削除
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        customerService.delete(id);
        response.sendRedirect("dispatch?className=CustomerServlet&methodName=list");
    }

    /**
     * ログイン状態をチェックし、未ログインならログイン画面へリダイレクト
     */
    private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("dispatch?className=CustomerServlet&methodName=toLogin");
        }
    }

    /**
     * ログイン中の顧客の予約一覧を表示
     */
    public void myAppointments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            response.sendRedirect("dispatch?className=CustomerServlet&methodName=toLogin");
            return;
        }

        List<AppointmentVO> appointments = appointmentService.findByCustomerId(customer.getId());
        request.setAttribute("appointments", appointments);
        request.getRequestDispatcher("/member/my_appointments.jsp").forward(request, response);
    }

    /**
     * ログイン画面へフォワード
     */
    public void toLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/member/login.jsp").forward(request, response);
    }

    /**
     * ログイン処理
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            Customer customer = customerService.login(email, password);
            if (customer == null) {
                request.setAttribute("error", "メールアドレスまたはパスワードが間違っています");
                request.getRequestDispatcher("/member/login.jsp").forward(request, response);
                return;
            }
            
            request.getSession().setAttribute("customer", customer);
            response.sendRedirect("dispatch?className=CustomerServlet&methodName=index");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/member/login.jsp").forward(request, response);
        }
    }

    /**
     * ログアウト処理
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("customer");
        response.sendRedirect("dispatch?className=CustomerServlet&methodName=toLogin");
    }

    /**
     * メンバー用トップページへフォワード
     */
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/member/index.jsp").forward(request, response);
    }

    /**
     * 予約用の教練リスト画面へフォワード（ページング・日付・器具IDなどを考慮）
     */
    public void toReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNum = PageConstant.DEFAULT_PAGE_NUM;
        int pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        String equipmentId = request.getParameter("equipmentId");
        String dateStr = request.getParameter("date");
        Date selectedDate = null;

        try {
            String pageNumStr = request.getParameter("pageNum");
            if (pageNumStr != null && !pageNumStr.isEmpty()) {
                pageNum = Integer.parseInt(pageNumStr);
            }

            // 日付パラメータの解析を試みる
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = sdf.parse(dateStr);
            } else {
                selectedDate = new Date(); // デフォルトは本日
            }
        } catch (NumberFormatException e) {
            // デフォルト値を使用
        } catch (ParseException e) {
            selectedDate = new Date(); // 解析失敗の場合は本日を使用
        }

        List<Equipment> equipments = equipmentService.findAll();
        List<CoachVO> coaches;
        int total;

        if (equipmentId != null && !equipmentId.isEmpty() && !"all".equals(equipmentId)) {
            // 器具と日付に基づいてスケジュールがある教練をページング検索
            coaches = coachService.findByEquipmentIdAndDatePage(
                    Integer.parseInt(equipmentId),
                    selectedDate,
                    pageNum,
                    pageSize
            );
            total = coachService.countByEquipmentIdAndDate(
                    Integer.parseInt(equipmentId),
                    selectedDate
            );
        } else {
            // 日付に基づいてスケジュールがある全ての教練をページング検索
            coaches = coachService.findByDatePage(selectedDate, pageNum, pageSize);
            total = coachService.countByDate(selectedDate);
        }

        int pages = (total + pageSize - 1) / pageSize;

        request.setAttribute("equipments", equipments);
        request.setAttribute("coaches", coaches);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("equipmentId", equipmentId);
        request.setAttribute("selectedDate", new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));

        request.getRequestDispatcher("/member/reservation_list.jsp").forward(request, response);
    }

    /**
     * 会員登録画面へフォワード
     */
    public void toRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/member/register.jsp").forward(request, response);
    }

    /**
     * 会員登録処理
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            int gender = Integer.parseInt(request.getParameter("gender"));
            int age = Integer.parseInt(request.getParameter("age"));

            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhone(phone);
            customer.setPassword(password);
            customer.setEmail(email);
            customer.setGender(gender);
            customer.setAge(age);
            customer.setMemberFlag(0); // デフォルトでは非会員

            customerService.register(customer);

            // 登録が成功した後、自動的にログイン
            request.getSession().setAttribute("customer", customer);
            response.sendRedirect("dispatch?className=CustomerServlet&methodName=index");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            toRegister(request, response);
        }
    }

    /**
     * 予約手続きの画面へフォワード
     */
    public void reservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkLogin(request, response);
        Integer coachId = Integer.parseInt(request.getParameter("coachId"));
        String dateStr = request.getParameter("date");
        Date selectedDate = new Date();
        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = sdf.parse(dateStr);
            }
        } catch (ParseException e) {
            // デフォルト値を使用
        }

        CoachVO coach = coachService.findById(coachId);
        coach.setSchedules(coachService.findScheduleByDate(coachId, selectedDate));
        request.setAttribute("coach", coach);
        request.setAttribute("selectedDate", new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));

        request.getRequestDispatcher("/member/reservation.jsp").forward(request, response);
    }

    /**
     * 予約確定処理
     */
    public void makeAppointment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            response.sendRedirect("dispatch?className=CustomerServlet&methodName=toLogin");
            return;
        }
        Integer coachId = Integer.parseInt(request.getParameter("coachId"));
        Integer equipmentId = Integer.parseInt(request.getParameter("equipmentId"));
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        String remark = request.getParameter("remark");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Appointment appointment = new Appointment();
        appointment.setCustomerId(customer.getId());
        appointment.setCoachId(coachId);
        appointment.setEquipmentId(equipmentId);
        try {
            appointment.setStartTime(sdf.parse(startTimeStr));
            appointment.setEndTime(sdf.parse(endTimeStr));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        appointment.setRemark(remark);

        System.out.println(appointment);
        appointmentService.makeAppointment(appointment);

        request.getRequestDispatcher("/member/reservation_success.jsp").forward(request, response);
    }
}