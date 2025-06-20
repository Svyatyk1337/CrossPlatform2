@param com.example.demo.model.User user
@param String error = null

<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Реєстрація - Футбольні трансфери</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            min-height: 100vh;
            font-family: 'Arial', sans-serif;
        }
        .register-container {
            max-width: 450px;
            margin: 50px auto;
            padding: 40px;
            background: white;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
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
        .btn-register {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            border: none;
            border-radius: 50px;
            padding: 12px 30px;
            font-weight: bold;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-container">
            <div class="text-center mb-4">
                <div style="font-size: 3rem; color: #28a745;">⚽</div>
                <h2>Реєстрація</h2>
                <p class="text-muted">Створіть новий акаунт</p>
            </div>

            @if(error != null)
                <div class="alert alert-danger">${error}</div>
            @endif

            <form action="/register" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Ім'я користувача</label>
                    <input type="text" 
                           class="form-control" 
                           id="username" 
                           name="username"
                           value="${user.getUsername() != null ? user.getUsername() : ""}"
                           placeholder="Введіть ім'я користувача"
                           required>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" 
                           class="form-control" 
                           id="email" 
                           name="email"
                           value="${user.getEmail() != null ? user.getEmail() : ""}"
                           placeholder="Введіть email"
                           required>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Пароль</label>
                    <input type="password" 
                           class="form-control" 
                           id="password" 
                           name="password"
                           placeholder="Введіть пароль"
                           required>
                </div>

                <div class="mb-4">
                    <label for="confirmPassword" class="form-label">Підтвердіть пароль</label>
                    <input type="password" 
                           class="form-control" 
                           id="confirmPassword" 
                           name="confirmPassword"
                           placeholder="Підтвердіть пароль"
                           required>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-register">
                        Зареєструватися
                    </button>
                </div>

                <div class="text-center mt-3">
                    <p class="text-muted">
                        Вже є акаунт? 
                        <a href="/login" class="text-decoration-none">Увійти</a>
                    </p>
                    <a href="/" class="text-decoration-none">На головну</a>
                </div>
            </form>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
