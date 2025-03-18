<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>ジム管理システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
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
        .navbar-brand {
            color: #fff !important;
            font-size: 18px;
            font-weight: 500;
        }
        .sidebar {
            position: fixed;
            left: 0;
            top: 50px;
            bottom: 0;
            width: 220px;
            background: #fff;
            box-shadow: 2px 0 6px rgba(0,21,41,.05);
            padding-top: 10px;
        }
        .main-content {
            margin-left: 220px;
            margin-top: 50px;
            padding: 20px;
            min-height: calc(100vh - 50px);
        }
        .nav-item {
            display: block;
            width: 100%;
        }
        .nav-link {
            color: #333;
            padding: 12px 20px;
            display: block;
            text-decoration: none;
            transition: all 0.3s;
        }
        .nav-link:hover {
            color: #1890ff;
            background: #e6f7ff;
            text-decoration: none;
        }
        .nav-link.active {
            color: #1890ff;
            background: #e6f7ff;
            border-right: 3px solid #1890ff;
        }
        .user-info {
            color: #fff;
            display: flex;
            align-items: center;
            height: 50px;
        }
        .welcome-text {
            margin-right: 15px;
        }
        .logout-btn {
            color: #fff;
            text-decoration: none;
            padding: 5px 10px;
            border-radius: 4px;
            transition: background 0.3s;
        }
        .logout-btn:hover {
            background: rgba(255,255,255,0.1);
            color: #fff;
            text-decoration: none;
        }
        .menu-title {
            padding: 12px 20px;
            color: #999;
            font-size: 13px;
            margin-top: 10px;
        }
        .nav-icon {
            margin-right: 10px;
            width: 14px;
            display: inline-block;
        }
        /* 表格相关样式 */
        .table {
            color: #606266;
            margin-bottom: 0;
            width: 100%;
        }

        .table thead th {
            background-color: #f8f9fa;
            color: #606266;
            font-weight: 500;
            border-bottom: 1px solid #ebeef5;
            padding: .75rem;
            vertical-align: middle;
        }

        .table td {
            padding: .75rem;
            vertical-align: middle;
            border-top: 1px solid #ebeef5;
        }

        .table-hover tbody tr:hover {
            background-color: #f5f7fa;
        }

        .table-bordered {
            border: 1px solid #ebeef5;
        }

        .table-bordered th,
        .table-bordered td {
            border: 1px solid #ebeef5;
        }

        .table-striped tbody tr:nth-of-type(odd) {
            background-color: #fafafa;
        }

        /* 按钮样式优化 */
        .btn-primary {
            background-color: #409eff;
            border-color: #409eff;
        }

        .btn-primary:hover {
            background-color: #66b1ff;
            border-color: #66b1ff;
        }

        .btn-danger {
            background-color: #f56c6c;
            border-color: #f56c6c;
        }

        .btn-danger:hover {
            background-color: #f78989;
            border-color: #f78989;
        }

        .btn-sm {
            padding: .25rem .5rem;
            font-size: .875rem;
        }

        /* 卡片样式 */
        .card {
            box-shadow: 0 1px 4px 0 rgba(0,0,0,.1);
            border: none;
            margin-bottom: 1rem;
        }

        .card-body {
            padding: 1.25rem;
        }

        /* 页面标题 */
        .page-title {
            color: #303133;
            font-size: 18px;
            font-weight: 500;
            margin-bottom: 1rem;
        }

        /* 工具栏 */
        .toolbar {
            margin-bottom: 1rem;
        }

        /* 表格内容区 */
        .table-responsive {
            overflow-x: auto;
        }

        /* 文本对齐 */
        .text-center {
            text-align: center;
        }

        .ml-2 {
            margin-left: .5rem;
        }
    </style>
</head>
<body>
    <!-- トップナビゲーションバー -->
    <nav class="navbar">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/index.jsp">フィットネスジム管理システム</a>
        <div class="user-info ml-auto">
            <span class="welcome-text">ようこそ、${sessionScope.admin.realName}さん</span>
            <a href="javascript:;" class="logout-btn">ログアウト</a>
        </div>
    </nav>

    <!-- サイドメニュー -->
    <div class="sidebar">
        <div class="menu-title">スタッフ管理</div>
        <div class="nav flex-column">
            <a class="nav-link ${param.menu == 'admin' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=AdminServlet&methodName=list">
                管理者管理
            </a>
            <a class="nav-link ${param.menu == 'coach' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=list">
                スタッフ管理
            </a>
        </div>
        <div class="menu-title">シフト管理</div>
        <div class="nav flex-column">
            <a class="nav-link ${param.menu == 'shift' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=ShiftServlet&methodName=list">
                シフト管理
            </a>
            <a class="nav-link ${param.menu == 'schedule' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=AdminScheduleServlet&methodName=list">
                スケジュール管理
            </a>
        </div>
        <div class="menu-title">会員管理</div>
        <div class="nav flex-column">
            <a class="nav-link ${param.menu == 'customer' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=list">
                会員管理
            </a>
            <a class="nav-link ${param.menu == 'appointment' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=AppointmentServlet&methodName=list">
                予約記録
            </a>
        </div>
        <div class="menu-title">レッスン管理</div>
        <div class="nav flex-column">
            <a class="nav-link ${param.menu == 'equipment' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=list">
                レッスン管理
            </a>
            <a class="nav-link ${param.menu == 'maintenance' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=list">
                レッスン進行管理
            </a>
            <a class="nav-link ${param.menu == 'equipment_category' ? 'active' : ''}" href="${pageContext.request.contextPath}/dispatch?className=EquipmentCategoryServlet&methodName=list">
                レッスンカテゴリー
            </a>
        </div>
    </div>

    <!-- メインコンテンツ領域 -->
    <div class="main-content">
        <c:choose>
            <c:when test="${not empty requestScope.page}">
                <jsp:include page="${requestScope.page}" />
            </c:when>
            <c:otherwise>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-body">
                                    <h2>ジム管理システムへようこそ</h2>
                                    <p>左側のメニューから操作を選択してください。</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script>
        document.querySelector('.logout-btn').addEventListener('click', function() {
            if(confirm('ログアウトしてもよろしいですか？')) {
                window.location.href = '${pageContext.request.contextPath}/admin/admin/login.jsp';
            }
        });
    </script>
</body>
</html>