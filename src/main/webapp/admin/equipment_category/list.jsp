<table class="table table-striped table-bordered table-hover">
   <thead>
       <tr>
           <th class="text-center" width="50">ID</th>
           <th>カテゴリー名</th>
           <th>説明</th>
           <th class="text-center" width="180">操作</th>
       </tr>
   </thead>
   <tbody>
       <c:forEach items="${categoryList}" var="category">
           <tr>
               <td class="text-center">${category.id}</td>
               <td>${category.name}</td>
               <td>${category.description}</td>
               <td class="text-center">
                   <!-- ... existing buttons ... -->
               </td>
           </tr>
       </c:forEach>
   </tbody>
</table>