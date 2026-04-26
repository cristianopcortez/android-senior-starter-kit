# Android Senior Starter Kit

Boilerplate para projetos Android com arquitetura limpa e práticas de nível sênior, pronto para escalar. Inclui Jetpack Compose, MVVM, Clean Architecture, injeção de dependência com Hilt e camada de rede com Retrofit — tudo conectado e configurado para que você comece novos desafios e features focando apenas no que importa.

## Tech Stack

| Camada | Tecnologia |
|---|---|
| **Linguagem** | Kotlin |
| **UI** | Jetpack Compose · Material 3 · Dynamic Color (Android 12+) |
| **Arquitetura** | Clean Architecture · MVVM |
| **DI** | Hilt / Dagger |
| **Rede** | Retrofit 2 · OkHttp · Gson |
| **Async** | Coroutines · StateFlow |
| **Testes** | JUnit 4 · Espresso · MockK · Turbine |
| **Build** | Gradle (Kotlin DSL) · Version Catalog (`libs.versions.toml`) |
| **SDK** | Min 24 (Android 7.0) · Target/Compile 35 |

## Arquitetura

O projeto segue **Clean Architecture** dividida em camadas com responsabilidades bem definidas:

```
app/
├── di/                     # Módulos Hilt (NetworkModule, ...)
├── domain/
│   ├── model/              # Resource<T> (Loading / Success / Error)
│   ├── repository/         # Contratos de repositório
│   └── usecase/            # UseCase<P, R> + NoParams
├── presentation/
│   └── viewmodel/          # BaseViewModel<T> com StateFlow
└── ui/
    └── theme/              # Material 3 theme, cores, tipografia
```

### Fluxo de dados

```
View (Compose)
  │  observa StateFlow<Resource<T>>
  ▼
ViewModel  ──►  UseCase  ──►  Repository  ──►  API (Retrofit)
  │                                               │
  └──────────  Resource.Loading / Success / Error ◄┘
```

### Principais abstrações

- **`Resource<T>`** — sealed class que encapsula os três estados de uma operação assíncrona (`Loading`, `Success`, `Error`).
- **`UseCase<P, R>`** — contrato para casos de uso com `operator fun invoke`, mantendo uma ação por classe.
- **`Repository`** — interface-marcadora para repositórios do domínio.
- **`BaseViewModel<T>`** — ViewModel genérico que expõe `uiState: StateFlow<Resource<T>>` pronto para ser observado pela UI.

## Primeiros passos

### Pré-requisitos

- Android Studio Hedgehog (2023.1+) ou superior
- JDK 11+
- Android SDK com API 35 instalada

### Setup

```bash
# 1. Clone o repositório
git clone https://github.com/cristianopcortez/android-senior-starter-kit.git
cd android-senior-starter-kit

# 2. Crie o arquivo de propriedades locais
cp local.properties.example local.properties

# 3. Ajuste a URL base da API no local.properties
#    API_BASE_URL=https://sua-api.com/
```

Abra o projeto no Android Studio, sincronize o Gradle e execute no emulador ou dispositivo.

### Configuração da API

A URL base é lida de `local.properties` e injetada via `BuildConfig.API_BASE_URL`. Para alterar:

```properties
# local.properties
API_BASE_URL=https\://api.exemplo.com/
```

## Como usar como base para novos desafios

1. **Crie seu Service** — defina a interface Retrofit no pacote `data/api/`.
2. **Crie seu Repository** — implemente a interface do domínio em `data/repository/`.
3. **Crie seu UseCase** — implemente `UseCase<P, R>` em `domain/usecase/`.
4. **Crie seu ViewModel** — estenda `BaseViewModel<T>` e chame o use case.
5. **Crie sua Tela** — composable que observa `viewModel.uiState.collectAsState()`.
6. **Registre no Hilt** — adicione `@Binds` / `@Provides` no módulo DI correspondente.

## Estrutura de pastas

```
.
├── app/
│   ├── build.gradle.kts            # Dependências e config do módulo
│   ├── proguard-rules.pro
│   └── src/
│       ├── androidTest/             # Testes instrumentados
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/.../
│       │   │   ├── di/              # Hilt modules
│       │   │   ├── domain/          # Models, Repositories, UseCases
│       │   │   ├── presentation/    # ViewModels
│       │   │   └── ui/theme/        # Compose theming
│       │   └── res/                 # Drawables, strings, themes
│       └── test/                    # Testes unitários
├── build.gradle.kts                 # Config raiz do Gradle
├── gradle/
│   └── libs.versions.toml           # Version catalog centralizado
├── settings.gradle.kts
└── local.properties.example         # Template de configuração local
```

## Licença

Este projeto está disponível sob a licença [MIT](LICENSE).
