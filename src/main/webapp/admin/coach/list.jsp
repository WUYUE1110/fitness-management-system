<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
    <div class="toolbar">
        <div class="row">
            <div class="col">
                <h2 class="page-title">スタッフ管理</h2>
            </div>
            <div class="col text-right">
                <a href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=toAdd" class="btn btn-primary">
                    スタッフを追加
                </a>
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
                            <th>氏名</th>
                            <th>性別</th>
                            <th>電話番号</th>
                            <th>メールアドレス</th>
                            <th>担当レッスン</th>
                            <th>入社日</th>
                            <th>雇用形態</th>
                            <th>在職状態</th>
                            <th class="text-center" width="180">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${coachList}" var="coach">
                            <tr>
                                <td class="text-center">${coach.id}</td>
                                <td>
                                    <c:if test="${not empty coach.avatar}">
                                        <img src="${pageContext.request.contextPath}${coach.avatar}" 
                                             class="rounded-circle" 
                                             style="width: 30px; height: 30px; object-fit: cover; margin-right: 8px;">
                                    </c:if>
                                    ${coach.name}
                                </td>
                                <td>${coach.gender == 1 ? '男性' : '女性'}</td>
                                <td>${coach.phone}</td>
                                <td>${coach.email}</td>
                                <td>${coach.equipmentName}</td>
                                <td><fmt:formatDate value="${coach.hireDate}" pattern="yyyy-MM-dd"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${coach.type == 1}">
                                            <span class="badge badge-warning">アルバイト</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-success">正社員</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${coach.status == 1}">
                                            <span class="badge badge-success">在職中</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-danger">退職</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=toUpdate&id=${coach.id}" 
                                       class="btn btn-sm btn-primary">編集</a>
                                    <a href="javascript:void(0)" 
                                       onclick="deleteCoach(${coach.id})"
                                       class="btn btn-sm btn-danger ml-2">削除</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- ページネーション -->
            <div class="row mt-3">
                <div class="col-sm-12 col-md-5">
                    <div class="dataTables_info">
                        ${total}件中${(pageNum-1)*pageSize+1}から${pageNum*pageSize > total ? total : pageNum*pageSize}件を表示
                    </div>
                </div>
                <div class="col-sm-12 col-md-7">
                    <div class="dataTables_paginate">
                        <ul class="pagination justify-content-end">
                            <li class="page-item ${pageNum <= 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=list&pageNum=${pageNum-1}&pageSize=${pageSize}">前へ</a>
                            </li>
                            <c:forEach begin="1" end="${pages}" var="i">
                                <li class="page-item ${pageNum == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=list&pageNum=${i}&pageSize=${pageSize}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${pageNum >= pages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=list&pageNum=${pageNum+1}&pageSize=${pageSize}">次へ</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function deleteCoach(id) {
    if(confirm('このスタッフを削除してもよろしいですか？')) {
        window.location.href = '${pageContext.request.contextPath}/dispatch?className=CoachServlet&methodName=delete&id=' + id;
    }
}
</script>