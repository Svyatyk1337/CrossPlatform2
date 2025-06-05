@param java.util.List<com.example.demo.model.User> users
@param org.springframework.security.core.Authentication authentication = null

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Адмін панель - Футбольні трансфери</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .dashboard-container {
            margin: 50px auto;
            max-width: 1200px;
            background: white;
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
        }
        .header-section {
            border-bottom: 2px solid #f8f9fa;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        .user-card {
            transition: transform 0.3s ease;
        }
        .user-card:hover {
            transform: translateY(-5px);
        }
    </style>
</head>
<body>
    @template.fragments.navbar(authentication = authentication)
    
    <div class="container">
        <div class="dashboard-container">
            <div class="header-section">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h1><span style="font-size: 2rem;">⚽</span> Адмін панель</h1>
                        <p class="text-muted">Керування користувачами системи</p>
                    </div>
                    <div>
                        <a href="/transfers/list" class="btn btn-outline-primary me-2">Трансфери</a>
                        <a href="/logout" class="btn btn-outline-danger">Вийти</a>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <h3>Користувачі системи</h3>
                    <div class="row">
                        @for(var user : users)
                            <div class="col-md-6 col-lg-4 mb-4">
                                <div class="card user-card h-100">
                                    <div class="card-body">
                                        <h5 class="card-title">
                                            ${user.getUsername()}
                                            @if(user.getRole().name().equals("ADMIN"))
                                                <span class="badge bg-danger ms-2">Адмін</span>
                                            @else
                                                <span class="badge bg-primary ms-2">Користувач</span>
                                            @endif
                                        </h5>
                                        <p class="card-text">
                                            <strong>Email:</strong> ${user.getEmail()}<br>
                                            <strong>ID:</strong> ${user.getId()}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        @endfor
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>