# the-met-impressionist-showcase
2022 Profolio Project: multi-module Jetpack Compose app showcasing The Met's impressionist collection. 

[Built with The Metropolitan Museum of Art Collection API](https://metmuseum.github.io/).

ShowcaseScreen           |  PaintingScreen
:-------------------------:|:-------------------------:
<img src="https://github.com/vrickey123/the-met-impressionist-showcase/blob/main/docs/showcase-screen.png" width="360">  |  <img src="https://github.com/vrickey123/the-met-impressionist-showcase/blob/main/docs/painting-screen.png" width="360">

## Architecture
Follows a Redux-style architecture where the `Result<T>` of an API call of is reduced into a `UIState<T>`. A generic Composable `fun <T: UIState> StatefulScreen()` renders the `UIState` into success, loading, or error UI.

https://github.com/vrickey123/the-met-impressionist-showcase/blob/4210d2e654177939b88f728064effaa77fc209d8/infrastructure/screen/src/main/java/com/vrickey123/screen/StatefulScreen.kt#L10-L28

A `ViewModel` implements the `ScreenViewModel<T: UIState>` and `Reducer<T: UIState, Result<D: Any>>` interfaces to define the state management API's that emit an immutable `StateFlow<UIState>` on any change to the underlying data source. 

https://github.com/vrickey123/the-met-impressionist-showcase/blob/7565e573f349c780b7c9f1da89b5c8f45507258d/feature/showcase/src/main/java/com/vrickey123/showcase/ui/ShowcaseViewModelImpl.kt#L15-L44

### Multi-Module
Following the [Android Guide to Modularization](https://developer.android.com/topic/modularization), the implementation is modularized across `app`, `feature`, `infrastructure`, and `core` modules. The core modules define low-level API and `UIState` data models. The infrastructure modules provide reusable platform tools. User-facing `Screen` destinations are in feature modules and are implemented with a reusable `infrastructure:ui_component` library. The app module contains the `MainActivity` and coordinates the Compose `NavGraph` between Screens. The infrastructure and core modules are suitable for Kotlin multiplatform.

- **[app](app)**
   - [MainActivity](app/src/main/java/com/vrickey123/the_met_impressionist_showcase/MainActivity.kt)
   - [App.kt](app/src/main/java/com/vrickey123/the_met_impressionist_showcase/App.kt): the App Composable wraps the `Scaffold` to swap out Screen Composables when navigating between destinations, set the Toolbar, Bottom Nav, etc.
   - [navigation](app/src/main/java/com/vrickey123/the_met_impressionist_showcase/navigation): the Compose `NavHost` implementation for this app
- **feature**
   - met
      - [showcase](feature/met/showcase): the [ShowcaseScreen](feature/met/showcase/src/main/java/com/vrickey123/showcase/ui/ShowcaseScreen.kt), [ShowcaseUIState](feature/met/showcase/src/main/java/com/vrickey123/showcase/ui/ShowcaseUIState.kt), and [ShowcaseViewModel](feature/met/showcase/src/main/java/com/vrickey123/showcase/ui/ShowcaseViewModel.kt)
      - [painting](feature/met/painting): the [PaintingScreen](feature/met/painting/src/main/java/com/vrickey123/painting/ui/PaintingScreen.kt), [PaintingUIState](feature/met/painting/src/main/java/com/vrickey123/painting/ui/PaintingUIState.kt), and [PaintingViewModel](feature/met/painting/src/main/java/com/vrickey123/painting/ui/PaintingViewModel.kt)
- **infrastructure**
   - **[image](infrastructure/image)**: an [AsyncImageComponent(url)](infrastructure/image/src/main/java/com/vrickey123/image/ui/AsyncImageComponent.kt) with a [Coil Image](https://coil-kt.github.io/coil/) implementation
   - **[reducer](infrastructure/reducer)**: a [Reducer](infrastructure/reducer/src/main/java/com/vrickey123/reducer/Reducer.kt) that transforms an old `UIState` and `Result<D: Any>` into a new [UIState](core/state/src/main/java/com/vrickey123/state/UIState.kt)
   - **[router](infrastructure/router)**: a [Router](infrastructure/router/src/main/java/com/vrickey123/router/Router.kt) that implements navigation between Compose Screen [Route](infrastructure/router/src/main/java/com/vrickey123/router/uri/Route.kt)'s
   - **[screen](infrastructure/screen)**: generic Screen Composables for `LoadingScreen()`, `ErrorScreen()`, `EmptyScreen()`, and `<T: UIState> StatefulScreen()` to render UI based on a `UIState`
   - **[viewmodel](infrastructure/viewmodel)**: a `ScreenViewModel` interface that is implemented to render the `StateFlow<T: UIState>` in a `<T: UIState> StatefulScreen()`
   - met
      - [met_network](infrastructure/met/met_network): the [MetRepository](infrastructure/met/met_network/src/main/java/com/vrickey123/network/MetRepository.kt) implementation to fetch remote data from The Met Collection API or return local data from the database.
      - [met_route](infrastructure/met/met_route): the `Route` implementation for this app: i.e., the top-level [MetRoute](infrastructure/met/met_route/src/main/java/com/vrickey123/met_route/MetRoute.kt) Screen destinations for `MetRoute.Showcase` and `MetRoute.Painting`
      - [met_ui_component](infrastructure/met/met_ui_component): the "Met UI Component library" that can be used across feature models
- **core**
   - **[state](core/state)**: the [UIState](core/state/src/main/java/com/vrickey123/state/UIState.kt) model for `data` loaded, `loading`, and `error` to render UI
   - met
      - [met_api](core/met/met_api): The Met Collection API data models used in this app

## UI Hierarchy
- Material3 Theme
- Screen
- Card
- Component
- Material3 Design Token

## State Management
| UI Hierarchy  | State Holder  | State  |
|---|---|---|
| Screen  | Android [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)  | `StateFlow<UIState>` |
| Card  | Stateless or Compose [State](https://developer.android.com/reference/kotlin/androidx/compose/runtime/State)  | `StateFlow<T>` |
| Component  | Stateless or Compose [State](https://developer.android.com/reference/kotlin/androidx/compose/runtime/State)  | `StateFlow<T>` |

Guidance on Android vs Compose API's found in [State in Compose](https://developer.android.com/jetpack/compose/state) documentation.

## Automated Tests
The `met_network` module contains unit tests for the `MetRepository`. There are two implementations for example purposes:
1. [MetRepositoryImplTestWithMockWebServer](infrastructure/met/met_network/src/test/java/com/vrickey123/network/MetRepositoryTestWithMockWebServer.kt)
   - A [MetRepositoryImpl](infrastructure/met/met_network/src/main/java/com/vrickey123/network/MetRepositoryImpl.kt) test that uses Square's [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) in a [MetNetworkClient](infrastructure/met/met_network/src/main/java/com/vrickey123/network/remote/MetNetworkClient.kt) to drive test network responses with JSON files in the [resources](infrastructure/met/met_network/src/test/resources) folder. A [FakeMetDatabase](infrastructure/met/met_network/src/test/java/com/vrickey123/network/local/FakeMetObjectDao.kt) instance can set `FakeMetDatabase.setIsSuccess` to return a mocked successful or failure response.
2. [MetRepositoryImplTestWithMockk](infrastructure/met/met_network/src/test/java/com/vrickey123/network/MetRepositoryImplTestWithMockk.kt)
   - A [MetRepositoryImpl](infrastructure/met/met_network/src/main/java/com/vrickey123/network/MetRepositoryImpl.kt) test that uses [Mockk](https://mockk.io/) to mock [MetNetworkClient](infrastructure/met/met_network/src/main/java/com/vrickey123/network/remote/MetNetworkClient.kt) and [MetDatabase](infrastructure/met/met_network/src/main/java/com/vrickey123/network/local/MetDatabase.kt) instances and their functions.

## Notes on Real World Usage
[The Metropolitan Museum of Art Collection API](https://metmuseum.github.io/) is more of an academic resource than it is a production-ready API suitable for news feeds at scale. It can take 30-45 seconds for a list of our 70-ish `MetObject`'s - the data class representing a painting - to return. 

From a backend perspective, the response time could possibly be improved by (1) one batch collection API call that sends a list of ids as a query parameter, (2) a `MetObjectMetadata` type with a few top-level fields for the work's image link, title, and artist that could drive a Card UI. These would eliminate the need for 70 sequential one-shot `MetObject` API calls to the `/objects/[objectID]/ endpoint and reduce the time it takes to download the data for a list feed, respectively. 

Since we cache the results of the first API call in a Room SQL database, only the first launch to seed the database takes extra time and the user is informed with a loading screen message. For a portfolio sample app that's OK.

https://user-images.githubusercontent.com/4541078/193988456-c6347802-db6d-41ee-a6fe-87c7fb4222cd.mp4
