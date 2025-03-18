<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <title>ジム予約システム</title>
    <link href="${pageContext.request.contextPath}/libs/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #1890ff 0%, #3498db 100%);
            box-shadow: 0 2px 4px rgba(0,0,0,.1);
        }
        .hero {
            background: linear-gradient(135deg, #1890ff 0%, #3498db 100%);
            color: white;
            padding: 100px 0;
            margin-bottom: 60px;
            position: relative;
            overflow: hidden;
        }
        .hero::after {
            content: '';
            position: absolute;
            bottom: -50px;
            left: 0;
            width: 100%;
            height: 100px;
            background: #f8f9fa;
            transform: skewY(-3deg);
        }
        .hero h1 {
            font-size: 3rem;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .hero p {
            font-size: 1.2rem;
            opacity: 0.9;
        }
        .feature-section {
            padding: 60px 0;
            background: white;
            margin-bottom: 60px;
        }
        .feature-card {
            text-align: center;
            padding: 30px;
            transition: transform 0.3s;
        }
        .feature-card:hover {
            transform: translateY(-10px);
        }
        .feature-icon {
            width: 80px;
            height: 80px;
            margin: 0 auto 20px;
            background: #1890ff;
            border-radius: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 2rem;
        }
        .footer {
            background: #2c3e50;
            color: white;
            padding: 40px 0;
            margin-top: 60px;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=index">フィットネスジム予約システム</a>
            <div class="ml-auto">
                <c:choose>
                    <c:when test="${empty sessionScope.customer}">
                        <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toLogin" class="btn btn-outline-light mr-2">会員ログイン</a>
                        <a href="${pageContext.request.contextPath}/admin/admin/login.jsp" class="btn btn-outline-light">管理者ログイン</a>
                    </c:when>
                    <c:otherwise>
                        <span class="text-white mr-3">ようこそ、${sessionScope.customer.name}様</span>
                        <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=myAppointments" class="btn btn-outline-light mr-2">予約一覧</a>
                        <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=logout" class="btn btn-outline-light">ログアウト</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>

    <div class="hero">
        <div class="container text-center">
            <h1>健康な毎日をはじめよう</h1>
            <p class="lead mb-4">最新のマシン、専門トレーナーによる一流のフィットネス体験を</p>
            <a href="${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=toReservation" class="btn btn-light btn-lg">予約する</a>
        </div>
    </div>

    <div class="feature-section">
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-dumbbell"></i>
                        </div>
                        <h3>最新マシン</h3>
                        <p>輸入された最新のフィットネスマシンで、あらゆるトレーニングニーズに対応</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-user-tie"></i>
                        </div>
                        <h3>専門トレーナー</h3>
                        <p>資格を持つ専門トレーナーによる、マンツーマン指導</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-clock"></i>
                        </div>
                        <h3>柔軟な時間</h3>
                        <p>オンライン予約で、いつでもどこでもトレーニング時間の設定が可能</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="footer">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h4>お問い合わせ</h4>
                    <p>住所：〒XXX-XXXX XX県XX市XX区XX町X-X-X</p>
                    <p>電話：03-XXXX-XXXX</p>
                    <p>メール：contact@gym.com</p>
                </div>
                <div class="col-md-6">
                    <h4>営業時間</h4>
                    <p>平日：10:00 - 22:00</p>
                    <p>土日祝：11:00 - 21:00</p>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/libs/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/libs/bootstrap.js"></script>
    <script>
    function logout() {
        if(confirm('ログアウトしてもよろしいですか？')) {
            location.href = '${pageContext.request.contextPath}/dispatch?className=CustomerServlet&methodName=logout';
        }
    }
    </script>
</body>
</html>