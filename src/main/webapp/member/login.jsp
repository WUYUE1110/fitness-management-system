<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>会員ログイン - ジム予約システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #00c6fb 0%, #005bea 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            margin: 0;
        }
        .login-container {
            max-width: 400px;
            margin: 0 auto;
            padding: 30px;
        }
        .login-card {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 8px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            padding: 30px;
            backdrop-filter: blur(10px);
        }
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-header h1 {
            color: #1890ff;
            font-size: 28px;
            margin-bottom: 10px;
        }
        .form-control {
            border-radius: 8px;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            transition: all 0.3s ease;
        }
        .form-control:focus {
            border-color: #1890ff;
            box-shadow: 0 0 0 0.2rem rgba(24, 144, 255, 0.25);
        }
        .btn-primary {
            background: linear-gradient(135deg, #1890ff 0%, #3498db 100%);
            border: none;
            padding: 12px;
            font-weight: 500;
            letter-spacing: 1px;
            transition: all 0.3s ease;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="login-card">
                <div class="login-header">
                    <h1>会員ログイン</h1>
                    <p class="text-muted">ログインしてトレーナーを予約</p>
                </div>
                <div class="alert alert-danger" style="display: ${not empty error ? 'block' : 'none'}">
                    ${error}
                </div>
                <form action="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=login" method="post">
                    <div class="form-group">
                        <label>メールアドレス</label>
                        <input type="email" name="email" class="form-control" required placeholder="メールアドレスを入力してください">
                    </div>
                    <div class="form-group">
                        <label>パスワード</label>
                        <input type="password" name="password" class="form-control" required placeholder="パスワードを入力してください">
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">ログイン</button>
                </form>
                <div class="mt-3 text-center">
                    <p class="mb-0">アカウントをお持ちでない方</p>
                    <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toRegister" class="btn btn-link">新規会員登録</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>