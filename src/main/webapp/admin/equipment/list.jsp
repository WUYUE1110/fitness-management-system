<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
   <div class="toolbar">
       <div class="row">
           <div class="col">
               <h2 class="page-title">レッスン一覧</h2>
           </div>
           <div class="col text-right">
               <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=toAdd" class="btn btn-primary">
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
                           <th>カテゴリー</th>
                           <th>状態</th>
                           <th>説明</th>
                           <th class="text-center" width="180">操作</th>
                       </tr>
                   </thead>
                   <tbody>
                       <c:forEach items="${equipmentList}" var="equipment">
                           <tr>
                               <td class="text-center">${equipment.id}</td>
                               <td>${equipment.name}</td>
                               <td>${equipment.categoryName}</td>
                               <td>
                                   <c:choose>
                                       <c:when test="${equipment.status == 1}">
                                           <span class="badge badge-success">実施中</span>
                                       </c:when>
                                       <c:otherwise>
                                           <span class="badge badge-danger">未実施</span>
                                       </c:otherwise>
                                   </c:choose>
                               </td>
                               <td>${equipment.description}</td>
                               <td class="text-center">
                                   <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=toUpdate&id=${equipment.id}" 
                                      class="btn btn-primary btn-sm">
                                       編集
                                   </a>
                                   <a href="javascript:void(0)" 
                                      onclick="deleteEquipment(${equipment.id})"
                                      class="btn btn-danger btn-sm ml-2">
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
                       全 ${total} 件中 ${(pageNum-1)*pageSize+1} から ${pageNum*pageSize > total ? total : pageNum*pageSize} 件を表示
                   </div>
               </div>
               <div class="col-sm-12 col-md-7">
                   <div class="dataTables_paginate">
                       <ul class="pagination justify-content-end">
                           <li class="page-item ${pageNum <= 1 ? 'disabled' : ''}">
                               <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=list&pageNum=${pageNum-1}&pageSize=${pageSize}">前へ</a>
                           </li>
                           <c:forEach begin="1" end="${pages}" var="i">
                               <li class="page-item ${pageNum == i ? 'active' : ''}">
                                   <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=list&pageNum=${i}&pageSize=${pageSize}">${i}</a>
                               </li>
                           </c:forEach>
                           <li class="page-item ${pageNum >= pages ? 'disabled' : ''}">
                               <a class="page-link" href="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=list&pageNum=${pageNum+1}&pageSize=${pageSize}">次へ</a>
                           </li>
                       </ul>
                   </div>
               </div>
           </div>
       </div>
   </div>
</div>

<script>
function deleteEquipment(id) {
   if(confirm('このレッスンを削除してもよろしいですか？')) {
       window.location.href = '${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=delete&id=' + id;
   }
}
</script>