<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>シフト登録システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .login-container {
            width: 100%;
            max-width: 400px;
            padding: 20px;
        }
        .login-card {
            background: #fff;
            padding: 30px;
            border-radius: 4px;
            box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
        }
        .login-title {
            text-align: center;
            margin-bottom: 30px;
            font-size: 24px;
            color: #303133;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <h2 class="login-title">シフト登録システム</h2>
            <form action="${pageContext.request.contextPath}/dispatch?className=CoachLoginServlet&methodName=login" method="post">
                <div class="form-group">
                    <label>メールアドレス</label>
                    <input type="email" name="email" class="form-control" required placeholder="メールアドレスを入力してください">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block">ログイン</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>