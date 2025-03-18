<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">
    <div class="toolbar">
        <div class="row">
            <div class="col">
                <h2 class="page-title">シフトの追加</h2>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/dispatch?className=ShiftServlet&methodName=add" method="post">
                <div class="form-group">
                    <label>シフト名</label>
                    <input type="text" name="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>開始時間</label>
                    <input type="time" name="startTime" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>終了時間</label>
                    <input type="time" name="endTime" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>給与</label>
                    <input type="number" name="salary" class="form-control" step="0.01" min="0" required>
                </div>
                <div class="form-group">
                    <label>説明</label>
                    <textarea name="description" class="form-control" rows="3"></textarea>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">保存</button>
                    <a href="${pageContext.request.contextPath}/dispatch?className=ShiftServlet&methodName=list" class="btn btn-secondary ml-2">戻る</a>
                </div>
            </form>
        </div>
    </div>
</div>