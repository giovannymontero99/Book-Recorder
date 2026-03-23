# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build the app
./gradlew build

# Run unit tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.castor.bookrecorder.ExampleUnitTest"

# Run instrumented (Android) tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Assemble release APK
./gradlew assembleRelease
```

There is no separate lint command configured — Android's built-in lint runs as part of `build`.

## Architecture

**Clean Architecture + MVVM**, single `:app` module, all source under `com.castor.bookrecorder/core/`.

### Layers

| Layer | Package | Responsibility |
|---|---|---|
| Data | `core/data/` | Room (local) + Firebase Firestore (remote), DTOs, repository implementations |
| Domain | `core/domain/` | Domain models, repository interfaces, use cases |
| Presentation | `core/presentation/` | Jetpack Compose screens, ViewModels, navigation |
| DI | `core/di/` | Hilt modules wiring layers together |

### Data Flow

```
UI (Compose) ← StateFlow ← ViewModel ← UseCase ← RepositoryImpl ← (Room DAO | Firebase Service)
```

- **Repository impls** (`core/data/*RepositoryImpl.kt`) coordinate between local Room DB and remote Firestore.
- **Mappers** live in `core/domain/repository/mappers/` and convert: `DTO ↔ Entity ↔ Domain model`.
- Room entities are in `core/data/local/entity/`, DAOs in `core/data/local/dao/`.
- Firebase services are in `core/data/remote/service/`.

### Presentation

- **Atomic Design**: UI components are split into `atoms/` → `molecules/` → `organisms/` under `core/presentation/component/`.
- **Screens** (pages) live in `core/presentation/pages/<feature>/`, each typically containing a `*Screen.kt` and `*ViewModel.kt`.
- **Navigation**: Type-safe routes using `@Serializable` sealed/data classes in `core/presentation/navigation/Routes.kt`. NavHost is set up in the navigation package.
- ViewModels use `StateFlow` for state and sealed `*Event` interfaces for UI actions.

### Dependency Injection (Hilt)

- `@HiltAndroidApp` on `Application.kt`, `@AndroidEntryPoint` on `MainActivity`.
- Modules in `core/di/`: `DatabaseModule` (Room), `RemoteDatabaseModule` (Firebase), plus bind/provide modules for repositories and services.
- ViewModels use `@HiltViewModel` and are scoped with `hiltViewModel()` in Compose.

## Key Technologies

- **UI**: Jetpack Compose + Material 3
- **Navigation**: Navigation Compose (type-safe routes via Kotlinx Serialization)
- **Local DB**: Room 2.7.x — has a migration (`MIGRATION_1_2`) in `core/data/local/migrations/`
- **Remote**: Firebase Auth + Firestore
- **Auth**: Firebase email/password + Google Sign-In (Credential Manager API)
- **Images**: Coil 3.x
- **DI**: Hilt 2.57 + KSP
- **Min SDK**: 24 | **Target/Compile SDK**: 36 | **JDK**: 17
- **Kotlin**: 2.2.x | **AGP**: 9.0.x

## Adding New Features

Follow this pattern for a new feature:

1. **Domain**: Add model in `core/domain/model/`, use case(s) in `core/domain/usecase/<feature>/`, repository interface method in `core/domain/repository/`.
2. **Data**: Add mapper in `core/domain/repository/mappers/`, implement in `core/data/*RepositoryImpl.kt`. For new Room columns/tables, add a migration in `core/data/local/migrations/` and bump the `AppDatabase` version.
3. **Presentation**: Create `core/presentation/pages/<feature>/` with `<Feature>Screen.kt` and `<Feature>ViewModel.kt`. Add a route object/class in `Routes.kt` and wire it in the NavHost.
4. **DI**: If new services or repositories are introduced, provide/bind them in the appropriate `core/di/` module.

## Localization

String resources support Spanish (`es-rCO`). Add new strings to both `res/values/strings.xml` and `res/values-es-rCO/strings.xml`.

## Memory Table Pattern

Memory data uses a **master-detail schema** with a Factory Method pattern for type-safe persistence.

### Database schema

```
memories (master)
├── memory_strings  → value: String  (FK → memories.id ON DELETE CASCADE)
├── memory_ints     → value: Int     (FK → memories.id ON DELETE CASCADE)
└── memory_booleans → value: Boolean (FK → memories.id ON DELETE CASCADE)
```

- `MemoryEntity` is the master table — no foreign keys.
- Each detail table (`memory_strings`, `memory_ints`, `memory_booleans`) holds `memoryId` as FK + an indexed column.
- Room stores `Boolean` as `INTEGER` (0/1) automatically.

### Domain model

All value types extend `sealed class MemoryValue` in `core/domain/model/Memory.kt`:

```kotlin
sealed class MemoryValue { abstract val id: String; abstract val text: String }
data class MemoryStringValue(..., val value: String)  : MemoryValue()
data class MemoryIntValue(...,    val value: Int)     : MemoryValue()
data class MemoryBooleanValue(..., val value: Boolean): MemoryValue()
```

### Factory Method roles (`core/data/factory/`)

| File | Pattern role | Responsibility |
|---|---|---|
| `MemoryValueInserter` | Product | Interface: `suspend fun insert(memoryId, memoryValue)` |
| `MemoryStringInserter` | Concrete Product | Inserts into `memory_strings` |
| `MemoryIntInserter` | Concrete Product | Inserts into `memory_ints` |
| `MemoryBooleanInserter` | Concrete Product | Inserts into `memory_booleans` |
| `MemoryValueInserterFactory` | Creator | Interface with factory method `create(memoryValue)` |
| `MemoryValueInserterFactoryImpl` | Concrete Creator | `when` on `MemoryValue` type → returns correct inserter |

`MemoryRepositoryImpl` inserts the master row first, then delegates the detail insert to the factory.

### Adding a new Memory value type (checklist)

1. **Entity** — create `Memory<Type>Entity` in `core/data/local/entity/` with `id`, `memoryId` (FK + index), `text`, `value`.
2. **Migration** — create `Migration<N>to<N+1>.kt` in `core/data/local/migrations/` with the `CREATE TABLE` + `CREATE INDEX` SQL.
3. **Domain model** — add `Memory<Type>Value` subclass to `sealed class MemoryValue` in `Memory.kt`.
4. **Mapper** — add `Memory<Type>Value.toEntity(memoryId)` in `core/domain/repository/mappers/MemoryMapper.kt`.
5. **DAO** — add `insertMemory<Type>(entity)` to `MemoryDao`.
6. **Concrete Product** — create `Memory<Type>Inserter` in `core/data/factory/`.
7. **Concrete Creator** — add `is Memory<Type>Value -> Memory<Type>Inserter(memoryDao)` branch in `MemoryValueInserterFactoryImpl`.
8. **AppDatabase** — register the new entity class and bump the version number.
9. **DI** — add the new `MIGRATION_<N>_<N+1>` to `addMigrations(...)` in `DatabaseModule`.
10. **Presentation** — add the new type to `MemoryValueType` enum, handle it in `saveMemory()` and render the appropriate input in the Screen.
