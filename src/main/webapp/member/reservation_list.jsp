<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>トレーナー予約 - ジム予約システム</title>
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
        .filter-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,.1);
            margin-bottom: 30px;
        }
        .equipment-buttons {
            display: flex;
            flex-wrap: wrap;
            gap: 5px;
            margin-bottom: -0.5rem;
        }
        .equipment-buttons .btn {
            white-space: normal;
            text-align: center;
            min-height: 38px;
            height: auto;
            padding: 8px 15px;
            flex: 0 0 auto;
        }
        .coach-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,.1);
            overflow: hidden;
            margin-bottom: 30px;
            transition: transform 0.3s;
        }
        .coach-card:hover {
            transform: translateY(-5px);
        }
        .coach-avatar {
            width: 100%;
            height: 250px;
            object-fit: cover;
        }
        .coach-info {
            padding: 20px;
        }
        .coach-name {
            font-size: 1.25rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .coach-equipment {
            color: #666;
            margin-bottom: 10px;
            word-wrap: break-word;
            word-break: break-all;
            white-space: normal;
            line-height: 1.4;
        }
        .coach-description {
            color: #666;
            margin-bottom: 20px;
            max-height: 100px;
            overflow: hidden;
        }
        .coach-details {
            display: flex;
            justify-content: flex-start;
            margin-bottom: 15px;
            color: #666;
        }
        .coach-detail-item {
            display: flex;
            align-items: center;
            margin-right: 20px;
        }
        .coach-detail-item i {
            margin-right: 5px;
            color: #005bea;
        }
        .pagination {
            margin-top: 30px;
            justify-content: center;
        }
        .modal-calendar {
            max-width: 400px;
        }
        .datepicker-inline {
            width: 100%;
        }
        .datepicker table {
            width: 100%;
        }
        .time-slot {
            padding: 10px;
            margin: 5px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            cursor: pointer;
        }
        .time-slot:hover {
            background-color: #f8f9fa;
        }
        .time-slot.selected {
            background-color: #007bff;
            color: white;
        }
        .schedule-items {
            display: flex;
            flex-wrap: wrap;
            gap: 5px;
        }
        .schedule-items .badge {
            font-size: 0.85rem;
            padding: 5px 10px;
        }
        #filterDate {
            background-color: #fff;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=index">ジム予約システム</a>
            <div class="ml-auto">
                <c:choose>
                    <c:when test="${empty sessionScope.customer}">
                        <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toLogin" class="btn btn-outline-light">ログイン</a>
                    </c:when>
                    <c:otherwise>
                        <span class="text-white mr-3">ようこそ、${sessionScope.customer.name}様</span>
                        <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=myAppointments" class="btn btn-outline-light mr-2">予約一覧</a>
                        <a href="javascript:;" onclick="logout()" class="btn btn-outline-light">ログアウト</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>

    <div class="main-content">
        <div class="container">
            <div class="filter-section">
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <h4 class="mb-3">トレーニング種目を選択</h4>
                        <div class="equipment-buttons">
                            <button type="button" class="btn btn-primary mb-2 mr-2" onclick="filterCoaches('all')">すべて</button>
                            <c:forEach items="${equipments}" var="equipment">
                                <button type="button" class="btn btn-outline-primary mb-2 mr-2" onclick="filterCoaches(${equipment.id})">
                                    ${equipment.name}
                                </button>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <h4 class="mb-3">日付を選択</h4>
                        <div class="input-group">
                            <input type="text" class="form-control" id="filterDate" readonly>
                            <div class="input-group-append">
                                <button class="btn btn-outline-secondary" type="button">
                                    <i class="fas fa-calendar-alt"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <c:forEach items="${coaches}" var="coach">
                    <div class="col-md-4 coach-item" data-equipment="${coach.equipmentId}">
                        <div class="coach-card">
                            <img src="${pageContext.request.contextPath}${coach.avatar}" 
                                class="coach-avatar" 
                                alt="${coach.name}">
                            <div class="coach-info">
                                <div class="coach-name">${coach.name}</div>
                                <div class="coach-equipment mb-2">
                                    <i class="fas fa-dumbbell"></i> ${coach.equipmentName}
                                </div>
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
                                <div class="coach-description">${coach.description}</div>
                                <button class="btn btn-primary btn-block" onclick="selectCoach(${coach.id})">
                                    このトレーナーを予約
                                </button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:if test="${pages > 0}">
                <nav>
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${pageNum <= 1 ? 'disabled' : ''}">
                            <a class="page-link" href="javascript:;" onclick="goPage(${pageNum - 1})">前へ</a>
                        </li>
                        <c:forEach begin="1" end="${pages}" var="p">
                            <li class="page-item ${pageNum == p ? 'active' : ''}">
                                <a class="page-link" href="javascript:;" onclick="goPage(${p})">${p}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${pageNum >= pages ? 'disabled' : ''}">
                            <a class="page-link" href="javascript:;" onclick="goPage(${pageNum + 1})">次へ</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
    <!-- 予約カレンダーモーダル -->
    <div class="modal fade" id="reservationModal" tabindex="-1">
        <div class="modal-dialog modal-calendar">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">予約日時を選択</h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div id="datepicker"></div>
                    <div id="timeSlots" class="mt-3" style="display:none;">
                        <h6>予約可能な時間帯：</h6>
                        <div id="timeSlotList"></div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">キャンセル</button>
                    <button type="button" class="btn btn-primary" id="confirmReservation" disabled>予約を確定</button>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/libs/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/libs/bootstrap-datepicker.js"></script>
    <script type="text/javascript">
        var selectedDate = '${selectedDate}';

        function selectCoach(coachId) {
            if (${empty sessionScope.customer}) {
                if (confirm('予約にはログインが必要です。ログインページに移動しますか？')) {
                    location.href = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toLogin';
                }
            } else {
                location.href = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=reservation&coachId=' + coachId + '&date=' + selectedDate;
            }
        }

        function filterCoaches(equipmentId) {
            var url = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toReservation';
            if (equipmentId !== 'all') {
                url += '&equipmentId=' + equipmentId;
            }
            url += '&date=' + selectedDate;
            if (${not empty param.pageNum}) {
                url += '&pageNum=${param.pageNum}';
            }
            location.href = url;
        }

        function logout() {
            if(confirm('ログアウトしますか？')) {
                $.get('${pageContext.request.contextPath}/dispatch', {
                    className: 'CustomerServlet',
                    methodName: 'logout'
                }, function() {
                    location.href = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toLogin';
                });
            }
        }

        function goPage(page) {
            var url = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toReservation&pageNum=' + page;
            if (${not empty param.equipmentId}) {
                url += '&equipmentId=${param.equipmentId}';
            }
            url += '&date=' + selectedDate;
            location.href = url;
        }

        $(document).ready(function() {
            $('#filterDate').datepicker({
                format: 'yyyy-mm-dd',
                language: 'ja',  
                autoclose: true,
                startDate: new Date(),
                todayHighlight: true,
                clearBtn: false,
                forceParse: false
            }).datepicker('setDate', selectedDate)
            .on('changeDate', function(e) {
                selectedDate = e.format('yyyy-mm-dd');
                filterCoaches('${param.equipmentId == null ? "all" : param.equipmentId}');
            });
        });
    </script>
</body>
</html