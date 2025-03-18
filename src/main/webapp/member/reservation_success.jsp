<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>予約完了 - ジム予約システム</title>
        <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
        <style>
            body {
                background: #f8f9fa;
            }
            .navbar {
                background: linear-gradient(135deg, #1890ff 0%, #3498db 100%);
                box-shadow: 0 2px 4px rgba(0,0,0,.1);
            }
            .main-content {
                padding: 40px 0;
            }
            .success-card {
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,.1);
                padding: 40px;
                text-align: center;
                margin-top: 40px;
            }
            .success-title {
                font-size: 24px;
                color: #333;
                margin-bottom: 16px;
            }
            .success-desc {
                color: #666;
                margin-bottom: 24px;
            }
            .btn-group {
                margin-top: 30px;
            }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=index">ジム予約システム</a>
                <div class="ml-auto">
                    <span class="text-white mr-3">ようこそ、${sessionScope.customer.name}様</span>
                    <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=myAppointments" class="btn btn-outline-light mr-2">予約確認</a>
                    <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=logout" class="btn btn-outline-light">ログアウト</a>
                </div>
            </div>
        </nav>

        <div class="main-content">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <div class="success-card">
                            <h2 class="success-title">ご予約完了！</h2>
                            <p class="success-desc">予約が正常に完了しました。「予約確認」から詳細をご確認いただけます。</p>
                            <div class="btn-group">
                                <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=index" class="btn btn-primary mr-3">トップページへ</a>
                                <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=myAppointments" class="btn btn-outline-primary">予約確認へ</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/libs/bootstrap.js"></script>
    </body>
</html>