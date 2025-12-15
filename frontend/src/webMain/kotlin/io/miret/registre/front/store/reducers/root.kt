package io.miret.registre.front.store.reducers

import io.miret.registre.front.store.state.State
import redux.Action
import redux.combineReducers

val rootReducer = combineReducers<State, Action>(ReducerSlices(
  username = usernameReducer,
))
