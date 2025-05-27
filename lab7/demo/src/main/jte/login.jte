@param String error = null
@param String logout = null
@param String registered = null

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Авторизація - Футбольні трансфери</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            min-height: 100vh;
            font-family: 'Arial', sans-serif;
        }
        .login-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 40px;
            background: white;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
        }
        .login-header {
            text-align: center;
            margin-bottom: 30px;
            color: #333;
        }
        .football-icon {
            font-size: 3rem;
            color: #28a745;
            margin-bottom: 15px;
        }
        .form-control {
            border-radius: 50px;
            padding: 12px 20px;
            border: 2px solid #e1e1e1;
            transition: all 0.3s ease;
        }
        .form-control:focus {
            border-color: #28a745;
            box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.25);
        }
        .btn-login {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            border: none;
            border-radius: 50px;
            padding: 12px 30px;
            font-weight: bold;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: all 0.3s ease;
        }
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 14px rgba(0, 0, 0, 0.18);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="login-header">
                <div class="football-icon">⚽</div>
                <h2>Футбольні трансфери</h2>
                <p class="text-muted">Увійдіть до системи</p>
            </div>

            @if(error != null)
                <div class="alert alert-danger">
                    ${error}
                </div>
            @endif

            @if(logout != null)
                <div class="alert alert-success">
                    ${logout}
                </div>
            @endif

            @if(registered != null)
                <div class="alert alert-success">
                    ${registered}
                </div>
            @endif

            <form action="/login" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Ім'я користувача</label>
                    <input type="text" 
                           class="form-control" 
                           id="username" 
                           name="username" 
                           placeholder="Введіть ім'я користувача"
                           required>
                </div>

                <div class="mb-4">
                    <label for="password" class="form-label">Пароль</label>
                    <input type="password" 
                           class="form-control" 
                           id="password" 
                           name="password" 
                           placeholder="Введіть пароль"
                           required>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-login">
                        Увійти
                    </button>
                </div>

                <div class="text-center mt-3">
                    <p class="text-muted">
                        Немає акаунту? 
                        <a href="/register" class="text-decoration-none">Зареєструватися</a>
                    </p>
                    <a href="/" class="text-decoration-none">На головну</a>
                </div>
            </form>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
