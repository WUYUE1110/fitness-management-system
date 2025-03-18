<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">
   <div class="toolbar">
       <div class="row">
           <div class="col">
               <h2 class="page-title">レッスン実施記録を追加</h2>
           </div>
       </div>
   </div>

   <div class="card">
       <div class="card-body">
           <form action="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=add" method="post">
               <div class="form-group">
                   <label>レッスン</label>
                   <select name="equipmentId" class="form-control" required>
                       <option value="">レッスンを選択</option>
                       <c:forEach items="${equipments}" var="equipment">
                           <option value="${equipment.id}">${equipment.name}</option>
                       </c:forEach>
                   </select>
               </div>
               <div class="form-group">
                   <label>実施内容</label>
                   <textarea name="description" class="form-control" rows="3" required></textarea>
               </div>
               <div class="form-group">
                   <button type="submit" class="btn btn-primary">保存</button>
                   <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentMaintenanceServlet&methodName=list" class="btn btn-secondary ml-2">戻る</a>
               </div>
           </form>
       </div>
   </div>
</div>