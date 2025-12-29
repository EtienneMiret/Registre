package io.miret.etienne.registre.front.store.actions

import redux.rtk.createAction

val usernameLoadStarted = createAction("username/loadStarted")
val usernameLoaded = createAction<String>("username/loaded")
val usernameLoadFailed = createAction<Short>("username/loadFailed")
