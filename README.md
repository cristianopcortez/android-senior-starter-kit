# Android Senior Starter Kit

*[README em Português](README.pt-BR.md)*

Production-ready boilerplate for Android projects built with clean architecture and senior-level best practices, designed to scale. Includes Jetpack Compose, MVVM, Clean Architecture, dependency injection with Hilt, a networking layer with Retrofit, and **SSL certificate pinning** (Network Security Config) for the demo API host — all wired up and configured so you can jump straight into new features and challenges.

**This branch (`feature/portfolio-hacker-news-demo`):** demo data comes from Algolia's **[Hacker News Search API](https://hn.algolia.com/api)** (public HTTPS endpoints, no API key), with **HTTPS to `hn.algolia.com` pinned** as described in [Network Security & SSL Pinning](#network-security--ssl-pinning) below.

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI** | Jetpack Compose · Material 3 · Dynamic Color (Android 12+) |
| **Architecture** | Clean Architecture · MVVM |
| **DI** | Hilt / Dagger |
| **Networking** | Retrofit 2 · OkHttp · Gson · Network Security Config (SSL pinning) |
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

When starting a brand-new app from this repo, also rename `applicationId`, namespace, Kotlin package (`br.com.ccortez.seniorstarterkitapplication`), and `rootProject.name` in `settings.gradle.kts`.

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

### Version catalog extras

[`libs.versions.toml`](gradle/libs.versions.toml) pins **Coil**, **MockK**, and **Turbine** alongside the libraries already wired in `:app`. Add them in `app/build.gradle.kts` when you enable image loading or those tests.

## Branches

Keep the **default branch** as this lean template so every clone stays a sane foundation for new apps. If you ship a fuller **portfolio demo** (remote API flows, richer UI), maintain it on a **separate branch** and mention it near the top of this README once it exists.

## Network Security & SSL Pinning

Connections to **`hn.algolia.com`** are protected with **certificate pinning** via `app/src/main/res/xml/network_security_config.xml`. The `<pin-set>` includes two SHA-256 pins:

1. **Leaf certificate** — matches the server certificate currently presented by Algolia. This gives a precise bind to the exact identity in use today.
2. **DigiCert intermediate CA** (backup pin) — matches an issuer in the chain. If Algolia **rotates the leaf** (renewal, reissue, new key) but **keeps the same public CA hierarchy** (DigiCert), the handshake can still satisfy the backup pin, so the app is less likely to break on a routine leaf rotation while **pinning remains meaningful** (we still constrain trust to a known CA path, not “any” system trust store entry for that host).

The pin set declares **`expiration="2027-01-01"`** as a reminder to **re-verify** hashes and the chain before that date; update pins (and the expiration) when certificates or the PKI strategy change.

**Debug builds:** `debug-overrides` trusts both **system** and **user** CAs so you can attach a debuggable build to tools like Charles or mitmproxy (user-installed CA) without turning off pinning logic for the rest of your workflow. Release builds rely on the configured pins for the Algolia host.

### Troubleshooting (demo / Hacker News not loading)

If the UI shows **errors or empty state** only for remote data, work through:

1. **Network and base URL** — confirm the device has internet and, if you customized `API_BASE_URL`, that it still targets **`https://hn.algolia.com/`** (or another host you have intentionally pinned).
2. **Certificate pinning** — `javax.net.ssl.SSLHandshakeException`, **pin verification failed**, or messages mentioning **NetworkSecurityPolicy** / **TrustManager** in Logcat often mean the **live certificate chain** no longer matches any pin in `network_security_config.xml` (e.g. Algolia switched CA or you are intercepting TLS with a proxy; a proxy-issued leaf will **not** match the production pins).
3. **Refresh the leaf pin** — extract the SHA-256 SPKI hash of the **current** server certificate and compare it with the first `<pin>` in the config. Example (Git Bash / Unix shell):

```bash
openssl s_client -connect hn.algolia.com:443 -servername hn.algolia.com < /dev/null 2>/dev/null \
  | openssl x509 -pubkey -noout \
  | openssl pkey -pubin -outform der \
  | openssl dgst -sha256 -binary \
  | openssl enc -base64
```

If the output differs from the committed leaf pin, update that pin (and extend `expiration` if needed). If **both** leaf and intermediate pins are wrong (e.g. provider moved off DigiCert), update the **intermediate** pin from the issuer you trust after inspecting the chain (`openssl s_client -showcerts`).

## License

This project is available under the [MIT](LICENSE) license.
