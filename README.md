# the-met-impressionist-showcase
2022 Profolio Project: multi-module Jetpack Compose app showcasing The Met's impressionist collection. 

[Built with The Metropolitan Museum of Art Collection API](https://metmuseum.github.io/).

## Architecture
Follows a Redux-style architecture where an API call of `Result<T>` is reduced into a `UIState<T>`, which emits a `StateFlow<UIState>`. A generic `StatefulScreen<T: UIState>` Composable renders the `UIState` into success, loading, or error UI.

### Multi-Module
The implementation is modularized across infrastructure modules for a repository, viewmodel, reducer, and router; and put in front of interfaces to change the implementation. User-facing Screen destinations are implemented in feature modules with a reusable UI Component library".

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
