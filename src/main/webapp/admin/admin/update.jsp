<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container-fluid">
    <div class="toolbar">
        <div class="row">
            <div class="col">
                <h2 class="page-title">管理者を編集</h2>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/dispatch?className=AdminServlet&methodName=update" method="post">
                <input type="hidden" name="id" value="${admin.id}">
                <div class="form-group">
                    <label>ユーザー名</label>
                    <input type="text" name="username" class="form-control" value="${admin.username}" required>
                </div>
                <div class="form-group">
                    <label>パスワード</label>
                    <input type="password" name="password" class="form-control" value="${admin.password}" required>
                </div>
                <div class="form-group">
                    <label>氏名</label>
                    <input type="text" name="realName" class="form-control" value="${admin.realName}" required>
                </div>
                <div class="form-group">
                    <label>電話番号</label>
                    <input type="text" name="phone" class="form-control" value="${admin.phone}" required>
                </div>
                <div class="form-group">
                    <label>メールアドレス</label>
                    <input type="email" name="email" class="form-control" value="${admin.email}" required>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">保存</button>
                    <a href="${pageContext.request.contextPath}/dispatch?className=AdminServlet&methodName=list" class="btn btn-secondary ml-2">戻る</a>
                </div>
            </form>
        </div>
    </div>
</div>