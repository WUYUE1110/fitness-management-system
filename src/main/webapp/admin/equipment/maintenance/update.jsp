<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">
   <div class="toolbar">
       <div class="row">
           <div class="col">
               <h2 class="page-title">レッスン進行を編集</h2>
           </div>
       </div>
   </div>

   <div class="card">
       <div class="card-body">
           <form action="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=update" method="post">
               <input type="hidden" name="id" value="${maintenance.id}">
               <div class="form-group">
                   <label>レッスン</label>
                   <select name="equipmentId" class="form-control" required>
                       <option value="">レッスンを選択</option>
                       <c:forEach items="${equipments}" var="equipment">
                           <option value="${equipment.id}" ${maintenance.equipmentId == equipment.id ? 'selected' : ''}>${equipment.name}</option>
                       </c:forEach>
                   </select>
               </div>
               <div class="form-group">
                   <label>レッスン内容</label>
                   <textarea name="description" class="form-control" rows="3" required>${maintenance.description}</textarea>
               </div>
               <div class="form-group">
                   <button type="submit" class="btn btn-primary">保存</button>
                   <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=list" class="btn btn-secondary ml-2">戻る</a>
               </div>
           </form>
       </div>
   </div>
</div>