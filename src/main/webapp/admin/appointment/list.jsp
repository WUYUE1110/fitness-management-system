<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
    <div class="toolbar">
        <div class="row">
            <div class="col">
                <h2 class="page-title">予約履歴</h2>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <th class="text-center" width="50">ID</th>
                            <th>会員名</th>
                            <th>トレーナー</th>
                            <th>設備</th>
                            <th>開始時間</th>
                            <th>終了時間</th>
                            <th>完了時間</th>
                            <th>ステータス</th>
                            <th>備考</th>
                            <th>作成日時</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${appointmentList}" var="appointment">
                            <tr>
                                <td class="text-center">${appointment.id}</td>
                                <td>${appointment.customerName}</td>
                                <td>${appointment.coachName}</td>
                                <td>${appointment.equipmentName}</td>
                                <td><fmt:formatDate value="${appointment.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td><fmt:formatDate value="${appointment.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td>
                                    <c:if test="${not empty appointment.finishTime}">
                                        <fmt:formatDate value="${appointment.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </c:if>
                                    <c:if test="${empty appointment.finishTime}">
                                        -
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${appointment.status == 0}">
                                            <span class="badge badge-warning">開始待ち</span>
                                        </c:when>
                                        <c:when test="${appointment.status == 1}">
                                            <span class="badge badge-success">完了</span>
                                        </c:when>
                                        <c:when test="${appointment.status == 2}">
                                            <span class="badge badge-danger">キャンセル済み</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>${appointment.remark}</td>
                                <td><fmt:formatDate value="${appointment.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="row mt-4">
                <div class="col-sm-12 col-md-5">
                    <div class="dataTables_info">
                        ${total}件中${(pageNum-1)*pageSize+1}から${pageNum*pageSize > total ? total : pageNum*pageSize}件を表示
                    </div>
                </div>
                <div class="col-sm-12 col-md-7">
                    <div class="dataTables_paginate">
                        <ul class="pagination justify-content-end">
                            <li class="page-item ${pageNum <= 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=AppointmentServlet&methodName=list&pageNum=${pageNum-1}&pageSize=${pageSize}">前へ</a>
                            </li>
                            <c:forEach begin="1" end="${pages}" var="i">
                                <li class="page-item ${pageNum == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=AppointmentServlet&methodName=list&pageNum=${i}&pageSize=${pageSize}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${pageNum >= pages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=AppointmentServlet&methodName=list&pageNum=${pageNum+1}&pageSize=${pageSize}">次へ</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>