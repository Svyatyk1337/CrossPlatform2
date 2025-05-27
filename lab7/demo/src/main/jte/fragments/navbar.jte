@param org.springframework.security.core.Authentication authentication = null

<nav class="navbar navbar-expand-lg navbar-dark" style="background: linear-gradient(135deg, #28a745 0%, #20c997 100%);">
    <div class="container">
        <a class="navbar-brand" href="/">
            <span style="font-size: 1.5rem;">⚽</span> Футбольні трансфери
        </a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/">Головна</a>
                </li>
                @if(authentication != null && authentication.isAuthenticated())
                    <li class="nav-item">
                        <a class="nav-link" href="/transfers">Трансфери</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/players">Гравці</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/teams">Команди</a>
                    </li>
                    @if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                        <li class="nav-item">
                            <a class="nav-link" href="/admin/dashboard">Адмін панель</a>
                        </li>
                    @endif
                @endif
            </ul>
            
            <ul class="navbar-nav">
                @if(authentication != null && authentication.isAuthenticated())
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" 
                           data-bs-toggle="dropdown">
                            Привіт, ${authentication.getName()}!
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="/profile">Профіль</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="/logout">Вийти</a></li>
                        </ul>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>