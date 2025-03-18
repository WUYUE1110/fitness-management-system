<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>予約一覧 - ジム予約システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #1890ff 0%, #3498db 100%);
            box-shadow: 0 2px 4px rgba(0,0,0,.1);
        }
        .table th {
            background-color: #f8f9fa;
            font-weight: 500;
        }
        .badge {
            padding: 0.5em 1em;
            font-weight: normal;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=index">ジム予約システム</a>
            <div class="ml-auto">
                <span class="text-white mr-3">ようこそ、${sessionScope.customer.name}様</span>
                <a href="javascript:;" onclick="logout()" class="btn btn-outline-light">ログアウト</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">予約履歴</h4>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>予約日</th>
                                <th>時間帯</th>
                                <th>トレーナー</th>
                                <th>レッスン</th>
                                <th>ステータス</th>
                                <th>備考</th>
                                <th>予約日時</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${appointments}" var="appointment">
                                <tr>
                                    <td>
                                        <fmt:formatDate value="${appointment.startTime}" pattern="yyyy年MM月dd日"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${appointment.startTime}" pattern="HH:mm"/>
                                        ～
                                        <fmt:formatDate value="${appointment.endTime}" pattern="HH:mm"/>
                                    </td>
                                    <td>${appointment.coachName}</td>
                                    <td>${appointment.equipmentName}</td>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${appointment.status == 0}">
                                                <span class="badge badge-warning">予約済</span>
                                            </c:when>
                                            <c:when test="${appointment.status == 1}">
                                                <span class="badge badge-success">完了</span>
                                            </c:when>
                                            <c:when test="${appointment.status == 2}">
                                                <span class="badge badge-danger">キャンセル</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${not empty appointment.remark}">
                                            ${appointment.remark}
                                        </c:if>
                                        <c:if test="${empty appointment.remark}">
                                            －
                                        </c:if>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${appointment.createTime}" pattern="yyyy年MM月dd日 HH:mm"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/libs/bootstrap.js"></script>
    <script>
    function logout() {
        if(confirm('ログアウトしてもよろしいですか？')) {
            location.href = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=logout';
        }
    }
    </script>
</body>
</html>