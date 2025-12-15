package redux.rtk

import redux.Action
import redux.Reducer

external interface ActionReducerMapBuilder<S> {

  fun <ARG, A : Action> addCase(
    actionCreator: (ARG) -> A,
    reducer: Reducer<S, A>,
  ): ActionReducerMapBuilder<S>

  fun addCase(
    type: String,
    reducer: Reducer<S, Any>,
  ): ActionReducerMapBuilder<S>

  fun addDefaultCase(
    reducer: Reducer<S, Any>,
  ): ActionReducerMapBuilder<S>

}
