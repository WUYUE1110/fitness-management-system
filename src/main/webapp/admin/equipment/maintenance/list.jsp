<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
   <div class="toolbar">
       <div class="row">
           <div class="col">
               <h2 class="page-title">レッスン進行管理</h2>
           </div>
           <div class="col text-right">
               <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=toAdd" class="btn btn-primary">
                   レッスンを追加
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
                           <th>レッスン名</th>
                           <th>レッスン詳細</th>
                           <th>登録者</th>
                           <th>状態</th>
                           <!-- <th>終了日時</th>
                           <th>作成日時</th>
                           <th>更新日時</th> -->
                           <th class="text-center" width="180">操作</th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${maintenanceList}" var="maintenance">
                           <tr>
                               <td class="text-center">${maintenance.id}</td>
                               <td>${maintenance.equipmentName}</td>
                               <td>${maintenance.description}</td>
                               <td>${maintenance.adminName}</td>
                               <td>
                                   <c:choose>
                                       <c:when test="${maintenance.status == 0}">
                                           <span class="badge badge-warning">未開始</span>
                                       </c:when>
                                       <c:otherwise>
                                           <span class="badge badge-success">進行中</span>
                                       </c:otherwise>
                                   </c:choose>
                               </td><%-- 
                               <td><fmt:formatDate value="${maintenance.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                               <td><fmt:formatDate value="${maintenance.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                               <td><fmt:formatDate value="${maintenance.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
                               <td class="text-center">
                                   <c:if test="${maintenance.status == 0}">
                                       <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=toUpdate&id=${maintenance.id}" 
                                          class="btn btn-primary btn-sm">
                                           編集
                                       </a>
                                       <a href="javascript:void(0)" 
                                          onclick="finishMaintenance(${maintenance.id})"
                                          class="btn btn-success btn-sm ml-2">
                                           開始
                                       </a>
                                   </c:if>
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
                       全 ${total} 件のレッスンのうち、 ${(pageNum-1)*pageSize+1} から ${pageNum*pageSize > total ? total : pageNum*pageSize} 件を表示
                   </div>
               </div>
               <div class="col-sm-12 col-md-7">
                   <div class="dataTables_paginate">
                       <ul class="pagination justify-content-end">
                           <li class="page-item ${pageNum <= 1 ? 'disabled' : ''}">
                               <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=list&pageNum=${pageNum-1}&pageSize=${pageSize}">前へ</a>
                           </li>
                           <c:forEach begin="1" end="${pages}" var="i">
                               <li class="page-item ${pageNum == i ? 'active' : ''}">
                                   <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=list&pageNum=${i}&pageSize=${pageSize}">${i}</a>
                               </li>
                           </c:forEach>
                           <li class="page-item ${pageNum >= pages ? 'disabled' : ''}">
                               <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=list&pageNum=${pageNum+1}&pageSize=${pageSize}">次へ</a>
                           </li>
                       </ul>
                   </div>
               </div>
           </div>
       </div>
   </div>
</div>

<script>
function finishMaintenance(id) {
   if(confirm('このレッスンを開始してもよろしいですか？')) {
       window.location.href = '${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=finish&id=' + id;
   }
}
</script>