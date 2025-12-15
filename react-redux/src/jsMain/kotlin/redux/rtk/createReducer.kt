@file:JsModule("@reduxjs/toolkit")

package redux.rtk

import redux.Action
import redux.Reducer

external fun <S> createReducer(
  initialState: S,
  builderCallback: (ActionReducerMapBuilder<S>) -> Unit,
) : Reducer<S, Action>
