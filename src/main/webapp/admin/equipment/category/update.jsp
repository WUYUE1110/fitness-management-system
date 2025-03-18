<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container-fluid">
    <div class="toolbar">
        <div class="row">
            <div class="col">
                <h2 class="page-title">レッスンカテゴリーの編集</h2>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/dispatch?className=EquipmentCategoryServlet&methodName=update" method="post">
                <input type="hidden" name="id" value="${category.id}">
                <div class="form-group">
                    <label>カテゴリー名</label>
                    <input type="text" name="name" class="form-control" value="${category.name}" required>
                </div>
                <div class="form-group">
                    <label>カテゴリーの説明</label>
                    <textarea name="description" class="form-control" rows="3" required>${category.description}</textarea>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">保存</button>
                    <a href="${pageContext.request.contextPath}/dispatch?className=EquipmentCategoryServlet&methodName=list" class="btn btn-secondary ml-2">戻る</a>
                </div>
            </form>
        </div>
    </div>
</div>