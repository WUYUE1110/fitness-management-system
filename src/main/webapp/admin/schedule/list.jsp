<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/libs/fullcalendar.css" rel="stylesheet">

<script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
<script src="${pageContext.request.contextPath}/libs/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/libs/fullcalendar.js"></script>
<div class="container-fluid">
   <div class="toolbar">
       <div class="row">
           <div class="col">
               <h2 class="page-title">スケジュール管理</h2>
           </div>
           <div class="col text-right">
               <div class="btn-group">
                   <button type="button" class="btn btn-primary" onclick="changeView('dayGridDay')">日表示</button>
                   <button type="button" class="btn btn-primary" onclick="changeView('dayGridWeek')">週表示</button>
                   <button type="button" class="btn btn-primary" onclick="changeView('dayGridTwoWeeks')">2週表示</button>
                   <button type="button" class="btn btn-primary" onclick="changeView('dayGridMonth')">月表示</button>
               </div>
           </div>
       </div>
   </div>

   <div class="calendar-container">
       <div id="calendar"></div>
   </div>
</div>

<!-- シフト詳細モーダル -->
<div class="modal fade" id="scheduleDetailModal" tabindex="-1">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <h5 class="modal-title">シフト詳細</h5>
               <button type="button" class="close" data-dismiss="modal">
                   <span>&times;</span>
               </button>
           </div>
           <div class="modal-body">
               <div id="scheduleList"></div>
           </div>
           <div class="modal-footer">
               <button type="button" class="btn btn-secondary" data-dismiss="modal">閉じる</button>
               <button type="button" id="helpBtn" class="btn btn-danger" onclick="toggleHelp()">ヘルプを追加</button>
           </div>
       </div>
   </div>
</div>

<!-- シフト割当モーダル -->
<div class="modal fade" id="assignShiftModal" tabindex="-1">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <h5 class="modal-title">シフト割当</h5>
               <button type="button" class="close" data-dismiss="modal">
                   <span>&times;</span>
               </button>
           </div>
           <div class="modal-body">
               <form id="assignShiftForm">
                   <input type="hidden" id="scheduleDate" name="date">
                   <div class="form-group">
                       <label>コーチを選択</label>
                       <select name="coachId" class="form-control" required>
                           <option value="">コーチを選択してください</option>
                           <c:forEach items="${coaches}" var="coach">
                               <option value="${coach.id}">${coach.name}</option>
                           </c:forEach>
                       </select>
                   </div>
                   <div class="form-group">
                       <label>シフトを選択</label>
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
               <button type="button" class="btn btn-primary" onclick="assignShift()">確定</button>
           </div>
       </div>
   </div>
</div>

<!-- 操作選択モーダル -->
<div class="modal fade" id="actionModal" tabindex="-1">
   <div class="modal-dialog">
       <div class="modal-content">
           <div class="modal-header">
               <h5 class="modal-title">操作を選択</h5>
               <button type="button" class="close" data-dismiss="modal">
                   <span>&times;</span>
               </button>
           </div>
           <div class="modal-body">
               <div class="list-group">
                   <button type="button" class="list-group-item list-group-item-action" onclick="viewSchedule()">
                       シフト状況を確認
                   </button>
                   <button type="button" class="list-group-item list-group-item-action" onclick="markNeedHelp()">
                       ヘルプが必要とマーク
                   </button>
                   <button type="button" class="list-group-item list-group-item-action" onclick="showAssignShiftModal()">
                       シフトを割り当て
                   </button>
               </div>
           </div>
       </div>
   </div>
</div>

<script>
// JavaScriptの変数名と関数名はそのまま維持
var calendar;
let selectedDate = null;

document.addEventListener('DOMContentLoaded', function() {
   var calendarEl = document.getElementById('calendar');
   calendar = new FullCalendar.Calendar(calendarEl, {
	   locale: 'ja',
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
       dateClick: function(info) {
           selectedDate = info.dateStr;
           $('#actionModal').modal('show');
       },
       events: function(info, successCallback, failureCallback) {
           $.get('${pageContext.request.contextPath}/dispatch', {
               className: 'AdminScheduleServlet',
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

function showDaySchedule(dateStr) {
   $.get('${pageContext.request.contextPath}/dispatch', {
       className: 'AdminScheduleServlet',
       methodName: 'getDaySchedules',
       date: dateStr
   }, function(result) {
       const scheduleList = $('#scheduleList');
       scheduleList.empty();
       
       if (result.schedules && result.schedules.length > 0) {
           const list = $('<div class="list-group"></div>');
           result.schedules.forEach(function(schedule) {
               list.append(
                   `<div class="list-group-item">
                       <h6 class="mb-1">${schedule.coachName}</h6>
                       <p class="mb-1">シフト：${schedule.shiftName}</p>
                       <small>状態：${schedule.status == 1 ? '確認済み' : '未確認'}</small>
                   </div>`
               );
           });
           scheduleList.append(list);
       } else {
           scheduleList.append('<p class="text-center">シフトの登録がありません</p>');
       }
       
       const helpBtn = $('#helpBtn');
       if (result.hasHelp) {
           helpBtn.text('ヘルプを解除');
           helpBtn.removeClass('btn-danger').addClass('btn-warning');
       } else {
           helpBtn.text('ヘルプを追加');
           helpBtn.removeClass('btn-warning').addClass('btn-danger');
       }
       
       $('#scheduleDetailModal').data('date', dateStr).modal('show');
   });
}

function toggleHelp() {
   const dateStr = $('#scheduleDetailModal').data('date');
   if (!dateStr) {
       alert('日付エラー');
       return;
   }
   
   const isAdding = $('#helpBtn').text() === 'ヘルプを追加';
   const message = isAdding ? 'この日付にヘルプマークを追加しますか？' : 'ヘルプマークを解除しますか？';
   if (!confirm(message)) {
       return;
   }
   
   $.post('${pageContext.request.contextPath}/dispatch', {
       className: 'AdminScheduleServlet',
       methodName: isAdding ? 'addHelp' : 'removeHelp',
       date: dateStr
   }, function(result) {
       if (result.success) {
           calendar.refetchEvents();
           showDaySchedule(dateStr);
       }
       alert(result.message);
   });
}

// その他の関数はそのまま維持
function changeView(viewName) {
   calendar.changeView(viewName);
}

function viewSchedule() {
   $('#actionModal').modal('hide');
   showDaySchedule(selectedDate);
}

function markNeedHelp() {
   $('#actionModal').modal('hide');
   toggleHelp();
}

function showAssignShiftModal() {
   $('#actionModal').modal('hide');
   $('#scheduleDate').val(selectedDate);
   $('#assignShiftModal').modal('show');
}

function assignShift() {
   var formData = $('#assignShiftForm').serialize();
   $.post('${pageContext.request.contextPath}/dispatch?className=AdminScheduleServlet&methodName=assignShift', 
       formData,
       function(result) {
           if (result.success) {
               alert('シフトを割り当てました');
               $('#assignShiftModal').modal('hide');
               calendar.refetchEvents();
           } else {
               alert(result.message || '操作に失敗しました');
           }
       }
   );
}
</script>

<style>
/* CSSはそのまま維持 */
.btn-group .btn {
   padding: 0.375rem 0.75rem;
   font-size: 0.875rem;
}

.btn-group .btn:not(:last-child) {
   margin-right: 5px;
}

.calendar-container {
   background: #fff;
   padding: 20px;
   border-radius: 4px;
   box-shadow: 0 1px 4px 0 rgba(0,0,0,.1);
   height: calc(100vh - 220px);
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
   white-space: pre-line !important;
   color: #ffffff !important;
}

.list-group-item-action:hover {
   background-color: #f8f9fa;
   cursor: pointer;
}

.list-group-item-action:active {
   background-color: #e9ecef;
}
</style>