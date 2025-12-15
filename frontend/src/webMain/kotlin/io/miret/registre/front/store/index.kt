package io.miret.registre.front.store

import io.miret.registre.front.store.reducers.rootReducer
import redux.rtk.configureStore

val registreStore = configureStore(
  reducer = rootReducer,
  devTools = true,
)
