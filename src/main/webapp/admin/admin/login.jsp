<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>管理者ログイン</title>
        <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #e0f7fa 0%, #80deea 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0;
            }
            .login-container {
                background: rgba(255, 255, 255, 0.9);
                border-radius: 20px;
                box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
                padding: 40px;
                width: 100%;
                max-width: 420px;
                backdrop-filter: blur(10px);
            }
            .login-header {
                text-align: center;
                margin-bottom: 35px;
            }
            .login-header h2 {
                color: #2c3e50;
                font-size: 28px;
                font-weight: 500;
                margin-bottom: 10px;
            }
            .login-header p {
                color: #7f8c8d;
                font-size: 16px;
            }
            .form-group {
                margin-bottom: 25px;
            }
            .form-group label {
                color: #34495e;
                font-weight: 500;
                margin-bottom: 8px;
            }
            .form-control {
                border-radius: 10px;
                padding: 12px 15px;
                border: 2px solid #e0e0e0;
                transition: all 0.3s ease;
            }
            .form-control:focus {
                border-color: #3498db;
                box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
            }
            .btn-login {
                width: 100%;
                padding: 12px;
                border-radius: 10px;
                background: #3498db;
                border: none;
                font-weight: 500;
                font-size: 16px;
                letter-spacing: 1px;
                margin-top: 15px;
                transition: all 0.3s ease;
            }
            .btn-login:hover {
                background: #2980b9;
                transform: translateY(-2px);
            }
            .alert {
                border-radius: 10px;
                margin-bottom: 25px;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <div class="login-header">
                <h2>ジム管理システム</h2>
                <p>アカウントにログインしてください</p>
            </div>
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            <form action="${pageContext.request.contextPath}/dispatch?className=AdminServlet&methodName=login" method="post">
                <div class="form-group">
                    <label>ユーザー名/メールアドレス</label>
                    <input type="text" name="username" class="form-control" required placeholder="ユーザー名またはメールアドレスを入力">
                </div>
                <div class="form-group">
                    <label>パスワード</label>
                    <input type="password" name="password" class="form-control" required placeholder="パスワードを入力">
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block">ログイン</button>
                </div>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/dispatch?className=CoachLoginServlet&methodName=toLogin" class="btn btn-outline-primary btn-block">
                        トレーナーログイン
                    </a>
                    <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=index" class="btn btn-outline-primary btn-block mt-2">
                        会員予約
                    </a>
                </div>
            </form>
        </div>
    </body>
</html>