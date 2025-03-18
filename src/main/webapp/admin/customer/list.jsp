<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
    <div class="toolbar">
        <div class="row">
            <div class="col">
                <h2 class="page-title">会員一覧</h2>
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
                            <th>電話番号</th>
                            <th>メール</th>
                            <th>性別</th>
                            <th>年齢</th>
                            <th>会員状態</th>
                            <th>有効期限</th>
                            <th class="text-center" width="180">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${customerList}" var="customer">
                            <tr>
                                <td class="text-center">${customer.id}</td>
                                <td>${customer.name}</td>
                                <td>${customer.phone}</td>
                                <td>${customer.email}</td>
                                <td>${customer.gender == 1 ? '男性' : '女性'}</td>
                                <td>${customer.age}</td>
                                <td>${customer.memberFlag == 1 ? '有効' : '無効'}</td>
                                <td><fmt:formatDate value="${customer.memberExpireTime}" pattern="yyyy年MM月dd日"/></td>
                                <td class="text-center">
                                    <a href="javascript:void(0)" 
                                       onclick="deleteCustomer(${customer.id})"
                                       class="btn btn-danger btn-sm">
                                        削除
                                    </a>
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
                        全${total}件中 ${(pageNum-1)*pageSize+1}～${pageNum*pageSize > total ? total : pageNum*pageSize}件を表示
                    </div>
                </div>
                <div class="col-sm-12 col-md-7">
                    <div class="dataTables_paginate">
                        <ul class="pagination justify-content-end">
                            <li class="page-item ${pageNum <= 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=list&pageNum=${pageNum-1}&pageSize=${pageSize}">前へ</a>
                            </li>
                            <c:forEach begin="1" end="${pages}" var="i">
                                <li class="page-item ${pageNum == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=list&pageNum=${i}&pageSize=${pageSize}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${pageNum >= pages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=list&pageNum=${pageNum+1}&pageSize=${pageSize}">次へ</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div> 

<!-- 削除確認のJavaScript -->
<script>
function deleteCustomer(id) {
    if(confirm('この会員を削除してもよろしいですか？')) {
        window.location.href = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=delete&id=' + id;
    }
}
</script>