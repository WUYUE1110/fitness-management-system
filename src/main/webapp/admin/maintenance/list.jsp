<table class="table table-striped table-bordered table-hover">
   <thead>
       <tr>
           <th class="text-center" width="50">ID</th>
           <th>レッスン名</th>
           <th>メンテナンス日付</th>
           <th>メンテナンス内容</th>
           <th>メンテナンス結果</th>
           <th>状態</th>
           <th class="text-center" width="180">操作</th>
       </tr>
   </thead>
   <tbody>
       <c:forEach items="${maintenanceList}" var="maintenance">
           <tr>
               <td class="text-center">${maintenance.id}</td>
               <td>${maintenance.equipmentName}</td>
               <td><fmt:formatDate value="${maintenance.maintenanceDate}" pattern="yyyy-MM-dd"/></td>
               <td>${maintenance.content}</td>
               <td>${maintenance.result}</td>
               <td>
                   <c:choose>
                       <c:when test="${maintenance.status == 1}">
                           <span class="badge badge-success">完了</span>
                       </c:when>
                       <c:otherwise>
                           <span class="badge badge-warning">進行中</span>
                       </c:otherwise>
                   </c:choose>
               </td>
               <td class="text-center">
                   <!-- ... existing buttons ... -->
               </td>
           </tr>
       </c:forEach>
   </tbody>
</table>