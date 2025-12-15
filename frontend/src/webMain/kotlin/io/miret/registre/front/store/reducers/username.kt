package io.miret.registre.front.store.reducers

import io.miret.registre.front.store.actions.usernameLoadFailed
import io.miret.registre.front.store.actions.usernameLoadStarted
import io.miret.registre.front.store.actions.usernameLoaded
import io.miret.registre.front.store.state.HttpExchange
import rtk.createReducer

val usernameReducer = createReducer(HttpExchange.Idle()) { builder ->
  builder.addCase(usernameLoadStarted) { state, action ->
    HttpExchange.Pending()
  }
  builder.addCase(usernameLoaded) { state, action ->
    HttpExchange.Success(action.payload)
  }
  builder.addCase(usernameLoadFailed) { state, action ->
    HttpExchange.Error(action.payload)
  }
}
