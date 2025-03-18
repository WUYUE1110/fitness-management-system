<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>新規会員登録 - フィットネスジム予約システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #00c6fb 0%, #005bea 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            margin: 0;
            padding: 40px 0;
        }
        .register-container {
            max-width: 500px;
            margin: 0 auto;
            padding: 30px;
        }
        .register-card {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 8px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            padding: 30px;
            backdrop-filter: blur(10px);
        }
        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .register-header h1 {
            color: #1890ff;
            font-size: 28px;
            margin-bottom: 10px;
        }
        .form-control {
            border-radius: 8px;
            padding: 8px 15px;
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
        select.form-control {
            height: 45px;
            padding: 0 15px;
        }
        .form-text {
            font-size: 0.875rem;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-container">
            <div class="register-card">
                <div class="register-header">
                    <h1>新規会員登録</h1>
                    <p class="text-muted">必要事項をご入力ください</p>
                </div>
                <div class="alert alert-danger" style="display: ${not empty error ? 'block' : 'none'}">
                    ${error}
                </div>
                <form action="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=register" method="post">
                    <div class="form-group">
                        <label>お名前</label>
                        <input type="text" name="name" class="form-control" required placeholder="姓名をご入力ください">
                    </div>
                    <div class="form-group">
                        <label>メールアドレス</label>
                        <input type="email" name="email" class="form-control" required 
                               placeholder="例：example@email.com" 
                               pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}">
                        <small class="form-text">有効なメールアドレスを入力してください</small>
                    </div>
                    <div class="form-group">
                        <label>電話番号</label>
                        <input type="tel" name="phone" class="form-control" pattern="^[0-9]{10,11}$" required placeholder="例：09012345678">
                        <small class="form-text">数字のみ10桁または11桁で入力してください</small>
                    </div>
                    <div class="form-group">
                        <label>パスワード</label>
                        <input type="password" name="password" class="form-control" required minlength="6" placeholder="6文字以上で入力してください">
                    </div>
                    <div class="form-group">
                        <label>パスワード（確認）</label>
                        <input type="password" name="confirmPassword" class="form-control" required minlength="6" placeholder="もう一度入力してください">
                    </div>
                    <div class="form-group">
                        <label>性別</label>
                        <select name="gender" class="form-control custom-select" required>
                            <option value="">選択してください</option>
                            <option value="1">男性</option>
                            <option value="0">女性</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>年齢</label>
                        <input type="number" name="age" class="form-control" required min="1" max="120" placeholder="年齢をご入力ください">
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">登録する</button>
                    <div class="mt-3 text-center">
                        <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toLogin" class="btn btn-link">ログインページへ戻る</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
    <script>
    $(document).ready(function() {
        $('form').on('submit', function(e) {
            var password = $('input[name="password"]').val();
            var confirmPassword = $('input[name="confirmPassword"]').val();
            var email = $('input[name="email"]').val();
            
            // パスワード一致チェック
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('パスワードが一致しません。もう一度ご確認ください。');
                return false;
            }
            
            // メールアドレス形式チェック
            var emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
            if (!emailPattern.test(email)) {
                e.preventDefault();
                alert('有効なメールアドレスを入力してください。');
                return false;
            }
        });
    });
    </script>
</body>
</html>