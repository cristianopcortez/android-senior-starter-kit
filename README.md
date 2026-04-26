# Android Senior Starter Kit

Production-ready boilerplate for Android projects built with clean architecture and senior-level best practices, designed to scale. Includes Jetpack Compose, MVVM, Clean Architecture, dependency injection with Hilt, and a networking layer with Retrofit — all wired up and configured so you can jump straight into new features and challenges.

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI** | Jetpack Compose · Material 3 · Dynamic Color (Android 12+) |
| **Architecture** | Clean Architecture · MVVM |
| **DI** | Hilt / Dagger |
| **Networking** | Retrofit 2 · OkHttp · Gson |
| **Async** | Coroutines · StateFlow |
| **Testing** | JUnit 4 · Espresso · MockK · Turbine |
| **Build** | Gradle (Kotlin DSL) · Version Catalog (`libs.versions.toml`) |
| **SDK** | Min 24 (Android 7.0) · Target/Compile 35 |

## Architecture

The project follows **Clean Architecture** split into layers with well-defined responsibilities:

```
app/
├── di/                     # Hilt modules (NetworkModule, ...)
├── domain/
│   ├── model/              # Resource<T> (Loading / Success / Error)
│   ├── repository/         # Repository contracts
│   └── usecase/            # UseCase<P, R> + NoParams
├── presentation/
│   └── viewmodel/          # BaseViewModel<T> with StateFlow
└── ui/
    └── theme/              # Material 3 theme, colors, typography
```

### Data Flow

```
View (Compose)
  │  observes StateFlow<Resource<T>>
  ▼
ViewModel  ──►  UseCase  ──►  Repository  ──►  API (Retrofit)
  │                                               │
  └──────────  Resource.Loading / Success / Error ◄┘
```

### Key Abstractions

- **`Resource<T>`** — sealed class that wraps the three states of an async operation (`Loading`, `Success`, `Error`).
- **`UseCase<P, R>`** — contract for use cases with `operator fun invoke`, enforcing a single action per class.
- **`Repository`** — marker interface for domain repositories.
- **`BaseViewModel<T>`** — generic ViewModel that exposes `uiState: StateFlow<Resource<T>>` ready to be observed by the UI.

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1+) or later
- JDK 11+
- Android SDK with API 35 installed

### Setup

```bash
# 1. Clone the repository
git clone https://github.com/cristianopcortez/android-senior-starter-kit.git
cd android-senior-starter-kit

# 2. Create the local properties file
cp local.properties.example local.properties

# 3. Set your API base URL in local.properties
#    API_BASE_URL=https://your-api.com/
```

Open the project in Android Studio, sync Gradle, and run on an emulator or device.

### API Configuration

The base URL is read from `local.properties` and injected via `BuildConfig.API_BASE_URL`. To change it:

```properties
# local.properties
API_BASE_URL=https\://api.example.com/
```

## Using as a Base for New Features

1. **Create your Service** — define the Retrofit interface in the `data/api/` package.
2. **Create your Repository** — implement the domain interface in `data/repository/`.
3. **Create your UseCase** — implement `UseCase<P, R>` in `domain/usecase/`.
4. **Create your ViewModel** — extend `BaseViewModel<T>` and call the use case.
5. **Create your Screen** — composable that observes `viewModel.uiState.collectAsState()`.
6. **Register in Hilt** — add `@Binds` / `@Provides` in the corresponding DI module.

## Project Structure

```
.
├── app/
│   ├── build.gradle.kts            # Module dependencies and config
│   ├── proguard-rules.pro
│   └── src/
│       ├── androidTest/             # Instrumented tests
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/.../
│       │   │   ├── di/              # Hilt modules
│       │   │   ├── domain/          # Models, Repositories, UseCases
│       │   │   ├── presentation/    # ViewModels
│       │   │   └── ui/theme/        # Compose theming
│       │   └── res/                 # Drawables, strings, themes
│       └── test/                    # Unit tests
├── build.gradle.kts                 # Root Gradle config
├── gradle/
│   └── libs.versions.toml           # Centralized version catalog
├── settings.gradle.kts
└── local.properties.example         # Local configuration template
```

## License

This project is available under the [MIT](LICENSE) license.
