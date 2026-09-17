# Pokédex Kotlin

Aplicativo Android de estudo e portfólio que explora Pokémon usando a PokéAPI.

## Funcionalidades

- Lista em grade com imagem, número, nome e tipos
- Busca por nome com debounce, carregamento e mensagens de erro
- Paginação progressiva de 20 Pokémon
- Tela de detalhes com atributos, habilidades e barras de estatísticas
- Estados de carregamento, vazio, erro e nova tentativa

## Tecnologias

Kotlin, Jetpack Compose, Material 3, Navigation Compose, ViewModel, StateFlow, Coroutines, Retrofit, OkHttp, Kotlinx Serialization e Coil.

## Arquitetura

O fluxo é `UI → ViewModel → Repository → PokéAPI`. DTOs ficam em `data/remote`, os modelos de apresentação ficam em `domain/model` e cada tela mantém seu ViewModel.

## PokéAPI

Dados fornecidos por [PokéAPI](https://pokeapi.co/), usando `https://pokeapi.co/api/v2/`.

## Como executar

1. Abra o projeto no Android Studio.
2. Configure um Android SDK com API 35.
3. Execute `gradlew.bat test` e `gradlew.bat assembleDebug`, ou use as tarefas equivalentes da IDE.

## Estrutura do projeto

```
app/src/main/java/com/pokedex/app/
├── data/remote, data/repository
├── domain/model
├── navigation
└── ui/home, ui/details, ui/components, ui/theme
```
