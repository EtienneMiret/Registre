package io.miret.registre.front.store.actions

import redux.rtk.createAction

val usernameLoadStarted = createAction<Unit>("username/loadStarted")
val usernameLoaded = createAction<String>("username/loaded")
val usernameLoadFailed = createAction<Short>("username/loadFailed")
