package io.miret.registre.front.store

import io.miret.registre.front.store.reducers.usernameReducer
import io.miret.registre.front.store.state.HttpExchange
import io.miret.registre.front.store.state.HttpExchanges
import redux.rtk.configureStore

val store = configureStore(
  reducer = usernameReducer,
  preloadedState = HttpExchanges.idle(),
  devTools = true,
)
