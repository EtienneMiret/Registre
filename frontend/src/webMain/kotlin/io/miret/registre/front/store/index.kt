package io.miret.registre.front.store

import io.miret.registre.front.store.reducers.rootReducer
import io.miret.registre.front.store.state.initialState
import redux.rtk.configureStore

val registreStore = configureStore(
  reducer = rootReducer,
  preloadedState = initialState,
  devTools = true,
)
