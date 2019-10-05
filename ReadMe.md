## Square Github Repos
This is a small application to browse the repositories of Square Inc. on Github.
### Application Features:
- List Repositories (Name, Stars count, and Bookmark)
- Display details of a repository (same info like in list plus description, and Add/Remove Bookmark button)
- Supports both portrait and landscape
- Landscape display the master detail layout
## Build and Run
Open the project in AndroidStudio then Gradle Sync and Run, it works fine on both emulator and device.
## Architecture & Frameworks
Application built using MVVM architecture pattern where RxJava2 is used for asynchronous tasks and data binding,
also RxRelays is used for bindings.
Repository pattern is used to manage the repos loading and bookmark operations with the help of small Room database
to store the bookmarks.
If we consider only this small task it might be better to use SharedPreferences to store the bookmarks, however i used Room
as the case of targeting a bigger scale application.
All application dependencies are injected using Dagger 2.
## Testing
Application is mostly covered with unit tests using JUnit4 and AndroidJUnit runner which is used only to test the database.
## Future Improvements
- Save/Restore current selected repo item in the Activity state when orientation changes
- UI Refinement
- Support Pagination
- Allow selection of Organisation for which repos user needs to browse. i.e square or other
- Write some Ui Automated Tests


