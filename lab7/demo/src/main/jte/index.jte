@param org.springframework.security.core.Authentication authentication = null

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Футбольні трансфери</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            min-height: 100vh;
        }
        .hero-section {
            padding: 100px 0;
            text-align: center;
            color: white;
        }
        .hero-title {
            font-size: 3.5rem;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .hero-subtitle {
            font-size: 1.5rem;
            margin-bottom: 40px;
            opacity: 0.9;
        }
        .btn-hero {
            padding: 15px 40px;
            font-size: 1.2rem;
            border-radius: 50px;
            margin: 10px;
            transition: all 0.3s ease;
        }
        .btn-hero:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }
        .features {
            background: white;
            padding: 80px 0;
        }
        .feature-card {
            text-align: center;
            padding: 40px 20px;
            border-radius: 15px;
            margin: 20px 0;
            transition: transform 0.3s ease;
        }
        .feature-card:hover {
            transform: translateY(-10px);
        }
        .feature-icon {
            font-size: 4rem;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="/">
                <span style="font-size: 1.5rem;">⚽</span> Футбольні трансфери
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    @if(authentication != null && authentication.isAuthenticated())
                        <li class="nav-item">
                            <a class="nav-link" href="/transfers">Трансфери</a>
                        </li>
                        @if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                            <li class="nav-item">
                                <a class="nav-link" href="/admin/dashboard">Адмін панель</a>
                            </li>
                        @endif
                        <li class="nav-item">
                            <span class="navbar-text me-3">Привіт, ${authentication.getName()}!</span>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/logout">Вийти</a>
                        </li>
                    @else
                        <li class="nav-item">
                            <a class="nav-link" href="/login">Увійти</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/register">Реєстрація</a>
                        </li>
                    @endif
                </ul>
            </div>
        </div>
    </nav>

    <div class="hero-section">
        <div class="container">
            <h1 class="hero-title">⚽ Футбольні трансфери</h1>
            <p class="hero-subtitle">Ваш надійний джерело інформації про трансфери у світі футболу</p>
            
            @if(authentication != null && authentication.isAuthenticated())
                <a href="/transfers" class="btn btn-primary btn-hero">Переглянути трансфери</a>
                @if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                    <a href="/admin/dashboard" class="btn btn-warning btn-hero">Адмін панель</a>
                @endif
            @else
                <a href="/login" class="btn btn-primary btn-hero">Увійти</a>
                <a href="/register" class="btn btn-outline-light btn-hero">Реєстрація</a>
            @endif
        </div>
    </div>

    <div class="features">
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon">🏆</div>
                        <h3>Актуальна інформація</h3>
                        <p>Отримуйте найсвіжіші новини про трансфери ваших улюблених команд</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon">👥</div>
                        <h3>База гравців</h3>
                        <p>Повна база даних гравців з детальною інформацією та статистикою</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon">🔒</div>
                        <h3>Безпека</h3>
                        <p>Ваші дані під надійним захистом завдяки сучасним технологіям безпеки</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
