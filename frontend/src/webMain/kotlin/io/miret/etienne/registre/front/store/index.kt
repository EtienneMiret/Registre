package io.miret.etienne.registre.front.store

import io.miret.etienne.registre.front.store.reducers.rootReducer
import redux.rtk.configureStore

val registreStore = configureStore(
  reducer = rootReducer,
  devTools = true,
)
