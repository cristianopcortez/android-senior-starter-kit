# Android Senior Starter Kit

*[English README](README.md)*

Boilerplate pronta para produção para projetos Android com arquitetura limpa e práticas de nível sênior, pensada para escalar. Inclui Jetpack Compose, MVVM, Clean Architecture, injeção de dependências com Hilt, camada de rede com Retrofit e **SSL certificate pinning** (Network Security Config) para o host da API de demo — tudo configurado para você partir direto para novas funcionalidades e desafios.

Na branch de demo do Hacker News, o tráfego HTTPS para **`hn.algolia.com`** usa pinning; os detalhes estão na secção **Segurança de rede e SSL Pinning** abaixo.

## Stack tecnológica

| Camada | Tecnologia |
|---|---|
| **Linguagem** | Kotlin |
| **UI** | Jetpack Compose · Material 3 · Dynamic Color (Android 12+) |
| **Arquitetura** | Clean Architecture · MVVM |
| **DI** | Hilt / Dagger |
| **Rede** | Retrofit 2 · OkHttp · Gson · Network Security Config (SSL pinning) |
| **Assincronismo** | Coroutines · StateFlow |
| **Testes** | JUnit 4 · Espresso · MockK · Turbine |
| **Build** | Gradle (Kotlin DSL) · Version Catalog (`libs.versions.toml`) |
| **SDK** | Min 24 (Android 7.0) · Target/Compile 35 |

## Arquitetura

O projeto segue **Clean Architecture** organizada em camadas com responsabilidades bem definidas:

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
    └── theme/              # Tema Material 3, cores, tipografia
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

### Abstrações principais

- **`Resource<T>`** — classe selada que representa os três estados de uma operação assíncrona (`Loading`, `Success`, `Error`).
- **`UseCase<P, R>`** — contrato para casos de uso com `operator fun invoke`, reforçando uma ação por classe.
- **`Repository`** — interface marcadora para repositórios de domínio.
- **`BaseViewModel<T>`** — ViewModel genérica que expõe `uiState: StateFlow<Resource<T>>` pronta para a UI observar.

## Como começar

### Pré-requisitos

- Android Studio Hedgehog (2023.1+) ou mais recente
- JDK 11+
- Android SDK com API 35 instalada

### Configuração

```bash
# 1. Clone o repositório
git clone https://github.com/cristianopcortez/android-senior-starter-kit.git
cd android-senior-starter-kit

# 2. Crie o arquivo de propriedades locais
cp local.properties.example local.properties

# 3. Defina a URL base da API no local.properties
#    API_BASE_URL=https://sua-api.com/
```

Abra o projeto no Android Studio, sincronize o Gradle e execute em um emulador ou dispositivo.

### Configuração da API

A URL base é lida de `local.properties` e injetada via `BuildConfig.API_BASE_URL`. Para alterar:

```properties
# local.properties
API_BASE_URL=https\://api.example.com/
```

## Usando como base para novas features

1. **Crie o Service** — defina a interface Retrofit no pacote `data/api/`.
2. **Crie o Repository** — implemente a interface de domínio em `data/repository/`.
3. **Crie o UseCase** — implemente `UseCase<P, R>` em `domain/usecase/`.
4. **Crie o ViewModel** — estenda `BaseViewModel<T>` e chame o use case.
5. **Crie a Screen** — composable que observa `viewModel.uiState.collectAsState()`.
6. **Registre no Hilt** — adicione `@Binds` / `@Provides` no módulo DI correspondente.

Ao iniciar um app totalmente novo a partir deste repositório, renomeie também `applicationId`, namespace, pacote Kotlin (`br.com.ccortez.seniorstarterkitapplication`) e `rootProject.name` em `settings.gradle.kts`.

## Estrutura do projeto

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
│       │   │   ├── di/              # Módulos Hilt
│       │   │   ├── domain/          # Models, Repositories, UseCases
│       │   │   ├── presentation/    # ViewModels
│       │   │   └── ui/theme/        # Tema Compose
│       │   └── res/                 # Drawables, strings, temas
│       └── test/                    # Testes unitários
├── build.gradle.kts                 # Config Gradle raiz
├── gradle/
│   └── libs.versions.toml           # Catálogo centralizado de versões
├── settings.gradle.kts
└── local.properties.example         # Modelo de configuração local
```

### Extensões no catálogo de versões

O [`libs.versions.toml`](gradle/libs.versions.toml) também fixa versões para **Coil**, **MockK** e **Turbine** além das bibliotecas já ligadas ao `:app`. Declare-as em `app/build.gradle.kts` quando for usar carregamento de imagens ou esses tipos de teste.

## Branches

Mantenha a **branch padrão** como este template enxuto, para cada clone continuar sendo uma base estável para novos apps. Se você publicar uma **demo de portfolio** mais completa (fluxos com API remota, UI mais rica), mantenha isso numa **branch separada** e cite o nome dela perto do início deste README quando existir — para quem clone saber onde está o exemplo executável.

## Segurança de rede e SSL Pinning

Conexões com **`hn.algolia.com`** usam **certificate pinning** via `app/src/main/res/xml/network_security_config.xml`. O `<pin-set>` traz dois pins SHA-256:

1. **Certificado leaf** — corresponde ao certificado de servidor que a Algolia entrega hoje, amarrando a identidade exata em uso.
2. **CA intermediária DigiCert** (pin de backup) — corresponde a um emissor na cadeia. Se a Algolia **rotacionar o leaf** (renovação, reemissão, nova chave), mas **mantiver a mesma hierarquia de CA pública** (DigiCert), o handshake ainda pode satisfazer o pin de backup; o app tende a continuar funcionando em rotações rotineiras de leaf, enquanto **o pinning continua com significado** (ainda restringimos confiança a um caminho de PKI conhecido para aquele host, e não a “qualquer” entrada da trust store do sistema).

O pin set declara **`expiration="2027-01-01"`** como lembrete para **revalidar** os hashes e a cadeia antes dessa data; atualize os pins (e a expiração) quando certificados ou a estratégia de PKI mudarem.

**Builds de debug:** `debug-overrides` confia nas CAs **system** e **user** para você usar ferramentas como Charles ou mitmproxy (CA instalada pelo usuário) sem abrir mão do restante do fluxo de debug. Em release, o host da Algolia segue os pins configurados.

### Solução de problemas (demo / notícias não carregam)

Se a UI mostra **erro ou lista vazia** só para dados remotos, vale checar:

1. **Rede e URL base** — confirme internet no dispositivo e, se você alterou `API_BASE_URL`, que ela ainda aponta para **`https://hn.algolia.com/`** (ou outro host que você tenha pins configurados).
2. **Certificate pinning** — `javax.net.ssl.SSLHandshakeException`, falha de **pin** ou mensagens com **NetworkSecurityPolicy** / **TrustManager** no Logcat costumam indicar que a **cadeia de certificados ao vivo** não bate mais com nenhum pin em `network_security_config.xml` (por exemplo a Algolia mudou de CA ou há **proxy MITM**; o leaf emitido pelo proxy **não** coincide com os pins de produção).
3. **Atualizar o pin do leaf** — extraia o hash SHA-256 da SPKI pública do certificado **atual** do servidor e compare com o primeiro `<pin>` do XML. Exemplo (Git Bash / shell Unix):

```bash
openssl s_client -connect hn.algolia.com:443 -servername hn.algolia.com < /dev/null 2>/dev/null \
  | openssl x509 -pubkey -noout \
  | openssl pkey -pubin -outform der \
  | openssl dgst -sha256 -binary \
  | openssl enc -base64
```

Se o resultado for diferente do pin de leaf versionado, atualize esse pin (e o `expiration` se fizer sentido). Se **leaf e intermediária** deixarem de bater (ex.: troca de CA raiz/intermediária), atualize também o pin da **intermediária** a partir do emissor que você aceitar, inspecionando a cadeia (`openssl s_client -showcerts`).

## Licença

Este projeto está disponível sob a licença [MIT](LICENSE).
