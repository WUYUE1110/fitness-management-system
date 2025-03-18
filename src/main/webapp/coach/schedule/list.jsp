<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>シフト登録システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/libs/fullcalendar.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            background: #f0f2f5;
        }
        .navbar {
            background: linear-gradient(135deg, #1890ff 0%, #3498db 100%);
            padding: 0 20px;
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 1000;
            height: 50px;
        }
         .navbar .container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            height: 100%;
            padding: 0 15px;
            max-width: 100%;
        }
         .navbar-brand {
            color: white;
            text-decoration: none;
            margin-right: 20px;
        }
        .navbar-right {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .main-content {
            margin-top: 50px;
            padding: 20px;
        }
        .view-buttons {
            margin-bottom: 10px;
        }
        .calendar-container {
            background: #fff;
            padding: 20px;
            border-radius: 4px;
            box-shadow: 0 1px 4px 0 rgba(0,0,0,.1);
            height: calc(100vh - 240px);
            
        }
        .fc {
            height: 100% !important;
            
        }
        .fc .fc-day-past {
            background-color: #f5f5f5;
            opacity: 0.6;
        }
        .fc .fc-day-future {
            background-color: #ffffff;
        }
        .fc .fc-daygrid-day-frame {
            padding: 4px !important;
        }
        .fc .fc-daygrid-day-events {
            margin-top: 4px !important;
        }
        .fc-event-title {
		    color: #ffffff !important;
		}
		.fc-event-time {
		    color: #ffffff !important;
		}
		.fc-event {
		    color: #ffffff !important;
		}
		.fc-event-main {
		    color: #ffffff !important;
		}
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=index">シフト登録システム</a>
            <div class="ml-auto">
                <span class="text-white mr-3">ようこそ、${sessionScope.coach.name}さん</span>
                <a href="${pageContext.request.contextPath}/dispatch?className=CoachLoginServlet&methodName=logout" class="btn btn-outline-light">ログアウト</a>
                <a href="${pageContext.request.contextPath}/admin/admin/login.jsp" class="btn btn-outline-light ml-2">管理者ログイン</a>
            </div>
        </div>
    </nav>

    <div class="main-content">
        <div class="container-fluid">
            <div class="view-buttons">
                <div class="btn-group">
                    <button type="button" class="btn btn-primary" onclick="changeView('dayGridDay')">日表示</button>
                    <button type="button" class="btn btn-primary" onclick="changeView('dayGridWeek')">週表示</button>
                    <button type="button" class="btn btn-primary" onclick="changeView('dayGridTwoWeeks')">2週間表示</button>
                    <button type="button" class="btn btn-primary" onclick="changeView('dayGridMonth')">月表示</button>
                </div>
            </div>

            <div class="calendar-container">
                <div id="calendar"></div>
            </div>
        </div>
    </div>

    <!-- シフト選択モーダル -->
    <div class="modal fade" id="scheduleModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">シフトを選択</h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div id="currentSchedule" class="mb-3" style="display: none;">
                        <div class="alert alert-info">
                            <h6 class="mb-1">現在のシフト</h6>
                            <p class="mb-1" id="currentShiftInfo"></p>
                            <small id="currentStatus"></small>
                        </div>
                    </div>
                    <form id="scheduleForm">
                        <input type="hidden" id="scheduleDate" name="scheduleDate">
                        <div class="form-group">
                            <label>シフト</label>
                            <select name="shiftId" class="form-control" required>
                                <option value="">シフトを選択してください</option>
                                <c:forEach items="${shifts}" var="shift">
                                    <option value="${shift.id}">${shift.name} (${shift.startTime}-${shift.endTime})</option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">キャンセル</button>
                    <button type="button" class="btn btn-primary" onclick="saveSchedule()">確定</button>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/libs/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/libs/fullcalendar.js"></script>
    <script>
        let calendar;
        
        document.addEventListener('DOMContentLoaded', function() {
            const calendarEl = document.getElementById('calendar');
            calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: ''
                },
                views: {
                    dayGridTwoWeeks: {
                        type: 'dayGrid',
                        duration: { weeks: 2 }
                    }
                },
                dayGridDay: {
                    type: 'dayGrid',
                    duration: { days: 1 }
                },
                dayGridWeek: {
                    type: 'dayGrid',
                    duration: { weeks: 1 }
                },
                firstDay: 1,
                titleFormat: {
                    year: 'numeric',
                    month: 'long'
                },
                buttonText: {
                    today: '今日',
                    month: '月',
                    week: '週',
                    day: '日'
                },
                locale: 'ja',
                dateClick: function(info) {
                	// ヘルプイベントかどうかを確認
                    const helpEvent = calendar.getEvents().find(event => 
                        event.start.toISOString().split('T')[0] === info.dateStr && event._def.extendedProps.isHelp
                    );
                    if (helpEvent) {
                    	// ヘルプイベントの場合、シフト選択を表示
                        showDaySchedule(info.dateStr);
                        return;
                    }

                    // 過去の日付かどうかを確認
                    const selectedDate = new Date(info.dateStr);
                    const today = new Date();
                    today.setHours(0, 0, 0, 0);
                    if (selectedDate < today) {
                        alert('過去の日付は選択できません');
                        return;
                    }
                    
                 	// 1ヶ月以上先の日付かどうかを確認
                    const oneMonthLater = new Date();
                    oneMonthLater.setMonth(oneMonthLater.getMonth() + 1);
                    if (selectedDate > oneMonthLater) {
                        alert('1ヶ月以内のシフトのみ選択できます');
                        return;
                    }
                    
                    showDaySchedule(info.dateStr);
                },
                events: function(info, successCallback, failureCallback) {
                    $.get('${pageContext.request.contextPath}/dispatch', {
                        className: 'CoachScheduleServlet',
                        methodName: 'listSchedules',
                        start: info.startStr,
                        end: info.endStr
                    }, function(result) {
                        successCallback(result);
                    });
                }
            });
            calendar.render();
        });

        function changeView(viewName) {
            calendar.changeView(viewName);
        }

        function saveSchedule() {
            const form = $('#scheduleForm');
            const shiftId = form.find('[name=shiftId]').val();
            const scheduleDate = form.find('[name=scheduleDate]').val();
            
            // シフトが選択されていない場合、その日付のシフトをキャンセル
            if (!shiftId) {
                $.post('${pageContext.request.contextPath}/dispatch', {
                    className: 'CoachScheduleServlet',
                    methodName: 'cancel',
                    scheduleDate: scheduleDate
                }, function(result) {
                    if (result.success) {
                        $('#scheduleModal').modal('hide');
                        calendar.refetchEvents();
                    } else {
                        alert(result.message);
                    }
                });
                return;
            }
            
            $.post('${pageContext.request.contextPath}/dispatch', {
                className: 'CoachScheduleServlet',
                methodName: 'add',
                scheduleDate: form.find('[name=scheduleDate]').val(),
                shiftId: form.find('[name=shiftId]').val()
            }, function(result) {
                if (result.success) {
                    $('#scheduleModal').modal('hide');
                    calendar.refetchEvents();
                } else {
                    alert(result.message);
                }
            });
        }

        function logout() {
            if(confirm('ログアウトしますか？')) {
                window.location.href = '${pageContext.request.contextPath}/dispatch?className=CoachLoginServlet&methodName=logout';
            }
        }

        function showDaySchedule(dateStr) {
            $.get('${pageContext.request.contextPath}/dispatch', {
                className: 'CoachScheduleServlet',
                methodName: 'getDaySchedule',
                date: dateStr
            }, function(result) {
                const currentSchedule = $('#currentSchedule');
                const currentShiftInfo = $('#currentShiftInfo');
                const currentStatus = $('#currentStatus');
                
                if (result && result.shiftName) {
                    currentShiftInfo.text('シフト：' + result.shiftName);
                    currentStatus.text('状態：' + (result.status === 1 ? '確認済み' : '未確認'));
                    currentSchedule.show();
                } else {
                    currentSchedule.hide();
                }
                
                $('#scheduleDate').val(dateStr);
                $('#scheduleModal').modal('show');
            });
        }
    </script>
</body>
</html> 