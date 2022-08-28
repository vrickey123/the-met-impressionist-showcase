# the-met-impressionist-showcase
2022 Profolio Project: multi-module Jetpack Compose app showcasing The Met's impressionist collection. 

[Built with The Metropolitan Museum of Art Collection API](https://metmuseum.github.io/).

## Architecture
Follows a Redux-style architecture where an API call of `Result<T>` is reduced into a `UIState<T>`, which emits a `StateFlow<UIState>`. A generic `StatefulScreen<T: UIState>` Composable renders the `UIState` into success, loading, or error UI. Advice on Android vs Compose API's found in [State in Compose](https://developer.android.com/jetpack/compose/state) documentation.

https://github.com/vrickey123/the-met-impressionist-showcase/blob/4210d2e654177939b88f728064effaa77fc209d8/infrastructure/screen/src/main/java/com/vrickey123/screen/StatefulScreen.kt#L10-L28

https://github.com/vrickey123/the-met-impressionist-showcase/blob/7565e573f349c780b7c9f1da89b5c8f45507258d/feature/showcase/src/main/java/com/vrickey123/showcase/ui/ShowcaseViewModelImpl.kt#L15-L44

### Multi-Module
Following the [Android Guide to Modularization](https://developer.android.com/topic/modularization), the implementation is modularized across app, feature, infrastructure, and core modules. The infrastructure modules for a `network`, `viewmodel`, `reducer`, and `router` set reusable platform tools; and put in front of interfaces to change the implementation. User-facing Screen destinations are implemented in feature modules with a reusable `ui_component` library.

## UI Hierarchy
- Material3 Theme
- Screen
- Card
- Component
- Material3 Design Token

## State Management
| UI Hierarchy  | SDK  | State  | Result  |
|---|---|---|---|
| Screen  | Android ViewModel  | `StateFlow<UIState>`  | `success<T>`, error, loading  |
| Card  | Compose StateHolder Class  | `StateFlow<T>`  | `Result<T>`  |
| Component  | Compose StateHolder Class  | `StateFlow<T>`  | `Result<T>`  |
