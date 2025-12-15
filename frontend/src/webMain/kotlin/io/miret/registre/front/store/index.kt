package io.miret.registre.front.store

import io.miret.registre.front.store.reducers.usernameReducer
import io.miret.registre.front.store.state.HttpExchange
import rtk.configureStore

val store = configureStore(
  reducer = usernameReducer,
  preloadedState = HttpExchange.Idle(),
  devTools = true,
)
