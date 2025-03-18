<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>トレーナー予約 - ジムご予約システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/libs/bootstrap-datepicker.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #1890ff 0%, #3498db 100%);
            box-shadow: 0 2px 4px rgba(0,0,0,.1);
        }
        .main-content {
            padding: 40px 0;
        }
        .coach-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,.1);
            padding: 20px;
            margin-bottom: 30px;
        }
        .coach-avatar {
            width: 200px;
            height: 200px;
            object-fit: cover;
            border-radius: 50%;
            margin-bottom: 20px;
        }
        .coach-name {
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .coach-info {
            margin-bottom: 20px;
        }
        .coach-description {
            color: #666;
            margin-bottom: 20px;
        }
        .reservation-section {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,.1);
            padding: 20px;
        }
        .time-slots {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 20px;
        }
        .time-slot {
            position: relative;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            transition: all 0.3s;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            width: 100%;
        }
        .time-slot:hover:not(.unavailable) {
            border-color: #007bff;
        }
        .time-slot.selected {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        .time-slot.unavailable {
            background-color: #f8f9fa;
            color: #999;
            cursor: not-allowed;
        }
        .time-slot input[type="radio"] {
            margin-right: 10px;
            cursor: pointer;
            width: 20px;
            height: 20px;
        }
        .time-slot input[type="radio"]:checked + label {
            color: #fff;
        }
        .time-slot label {
            margin: 0;
            cursor: pointer;
            flex: 1;
            padding: 5px 0;
        }
        .time-slot:not(.unavailable) label {
            cursor: pointer;
        }

    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=index">ジムご予約システム</a>
            <div class="ml-auto">
                <span class="text-white mr-3">ようこそ、${sessionScope.customer.name}様</span>
                <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=myAppointments" class="btn btn-outline-light mr-2">予約確認</a>
                <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=logout" class="btn btn-outline-light">ログアウト</a>
                <a href="${pageContext.request.contextPath}/admin/admin/login.jsp" class="btn btn-outline-light ml-2">管理者ログイン</a>
            </div>
        </div>
    </nav>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <div class="coach-card text-center">
                        <img src="${pageContext.request.contextPath}${coach.avatar}" class="coach-avatar" alt="${coach.name}">
                        <div class="coach-name">${coach.name}</div>
                        <div class="coach-info">
                            <p><i class="fas fa-dumbbell"></i> ${coach.equipmentName}</p>
                            <div class="coach-schedule mb-3">
                                <c:forEach items="${coach.schedules}" var="schedule">
                                    <span class="badge ${schedule.status == 1 ? 'badge-success' : 'badge-warning'} mr-2">
                                        ${schedule.shiftName} - ${schedule.startTime}-${schedule.endTime}
                                    </span>
                                </c:forEach>
                                <c:if test="${empty coach.schedules}">
                                    <p class="text-muted mb-0">スケジュールはありません</p>
                                </c:if>
                            </div>
                        </div>
                        <div class="coach-description">${coach.description}</div>
                    </div>
                </div>
                <div class="col-md-8">
                    <div class="reservation-section">
                        <h4 class="mb-4">予約時間の選択</h4>
                        <form action="${pageContext.request.contextPath}/dispatch" method="post">
                            <input type="hidden" name="className" value="CustomerServlet">
                            <input type="hidden" name="methodName" value="makeAppointment">
                            <input type="hidden" name="coachId" value="${coach.id}">
                            <input type="hidden" name="equipmentId" value="${coach.equipmentId}">
                            <input type="hidden" name="date" value="${selectedDate}">
                            
                            <div class="form-group">
                                <label>時間帯の選択：</label>
                                <div class="time-slots">
                                    <c:forEach items="${coach.schedules}" var="schedule">
                                        <div class="time-slot ${schedule.status == 1 ? '' : 'unavailable'}">
                                            <input type="radio" name="timeSlot"
                                                id="schedule_${schedule.id}" ${schedule.status == 1 ? '' : 'disabled'}>
                                            <input type="hidden" class="start-time" value="${selectedDate} ${schedule.startTime}:00">
                                            <input type="hidden" class="end-time" value="${selectedDate} ${schedule.endTime}:00">
                                            <label for="schedule_${schedule.id}">
                                                ${schedule.shiftName} (${schedule.startTime}-${schedule.endTime})
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label>備考：</label>
                                <textarea name="remark" class="form-control" rows="3" placeholder="備考情報を入力してください（任意）"></textarea>
                            </div>
                            
                            <div class="text-right mt-4">
                                <button type="button" class="btn btn-secondary mr-2" onclick="history.back()">戻る</button>
                                <button type="submit" class="btn btn-primary">予約確認</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/libs/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/libs/bootstrap-datepicker.js"></script>
    <script type="text/javascript">
        var selectedDate = '${selectedDate}';

        // 時間帯が期限切れかどうかを判断
        function isTimeSlotExpired(startTime) {
            var now = new Date();
            var slotTime = new Date(startTime);
            return slotTime < now;
        }
        
        $(document).ready(function() {
            // 期限切れの時間帯を確認して無効化
            $('.time-slot').each(function() {
                var startTime = $(this).find('.start-time').val();
                if (isTimeSlotExpired(startTime)) {
                    $(this).addClass('unavailable');
                    $(this).find('input[type="radio"]').prop('disabled', true);
                    $(this).find('label').append(' (期限切れ)');
                }
            });
            
            // フォーム送信前の検証
            $('form').submit(function(e) {
                if (!$('input[name="timeSlot"]:checked').length) {
                    alert('予約時間帯を選択してください');
                    e.preventDefault();
                    return false;
                }
                
                // 選択された時間帯が期限切れでないか再確認
                var selectedSlot = $('input[name="timeSlot"]:checked').closest('.time-slot');
                var startTime = selectedSlot.find('.start-time').val();
                if (isTimeSlotExpired(startTime)) {
                    alert('この時間帯は期限切れです。他の時間帯を選択してください');
                    e.preventDefault();
                    return false;
                }
                
                if (!confirm('この時間帯で予約を確定してもよろしいですか？')) {
                    e.preventDefault();
                    return false;
                }
                
                // 選択された時間帯の開始時間と終了時間を設定
                var startTime = selectedSlot.find('.start-time').val();
                var endTime = selectedSlot.find('.end-time').val();
                
                // フォームに時間フィールドを追加
                $('<input>').attr({
                    type: 'hidden',
                    name: 'startTime',
                    value: startTime
                }).appendTo($(this));
                
                $('<input>').attr({
                    type: 'hidden',
                    name: 'endTime',
                    value: endTime
                }).appendTo($(this));
            });

            // 時間帯選択のスタイル
            $('input[name="timeSlot"]').change(function() {
                $('.time-slot').removeClass('selected');
                $(this).closest('.time-slot').addClass('selected');
            });
            
            // 利用可能で期限切れでない時間帯が1つだけの場合、デフォルトで選択
            var availableSlots = $('.time-slot').filter(function() {
                return !$(this).hasClass('unavailable') && 
                       !isTimeSlotExpired($(this).find('.start-time').val());
            });
            if (availableSlots.length === 1) {
                availableSlots.find('input[name="timeSlot"]').prop('checked', true).trigger('change');
            }
        });
    </script>
</body>
</html>