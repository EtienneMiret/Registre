package io.miret.registre.front.store.reducers

import io.miret.registre.front.store.actions.usernameLoadFailed
import io.miret.registre.front.store.actions.usernameLoadStarted
import io.miret.registre.front.store.actions.usernameLoaded
import io.miret.registre.front.store.state.HttpExchanges
import io.miret.registre.front.store.state.initialState
import redux.rtk.createReducer

val usernameReducer = createReducer(initialState.username) { builder ->
  builder.addCase(usernameLoadStarted) { state, action ->
    HttpExchanges.pending()
  }
  builder.addCase(usernameLoaded) { state, action ->
    HttpExchanges.success(action.payload)
  }
  builder.addCase(usernameLoadFailed) { state, action ->
    HttpExchanges.error(action.payload)
  }
}
