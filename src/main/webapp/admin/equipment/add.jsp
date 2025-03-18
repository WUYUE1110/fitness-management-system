<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">
   <div class="toolbar">
       <div class="row">
           <div class="col">
               <h2 class="page-title">レッスンの追加</h2>
           </div>
       </div>
   </div>

   <div class="card">
       <div class="card-body">
           <form action="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=add" method="post">
               <div class="form-group">
                   <label>レッスン名</label>
                   <input type="text" name="name" class="form-control" required>
               </div>
               <div class="form-group">
                   <label>カテゴリー</label>
                   <select name="categoryId" class="form-control" required>
                       <option value="">カテゴリーを選択</option>
                       <c:forEach items="${categories}" var="category">
                           <option value="${category.id}">${category.name}</option>
                       </c:forEach>
                   </select>
               </div>
               <div class="form-group">
                   <label>レッスンの説明</label>
                   <textarea name="description" class="form-control" rows="3" required></textarea>
               </div>
               <div class="form-group">
                   <label>状態</label>
                   <select name="status" class="form-control" required>
                       <option value="1">実施中</option>
                       <option value="0">未実施</option>
                   </select>
               </div>
               <div class="form-group">
                   <button type="submit" class="btn btn-primary">保存</button>
                   <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentServlet&methodName=list" class="btn btn-secondary ml-2">戻る</a>
               </div>
           </form>
       </div>
   </div>
</div>