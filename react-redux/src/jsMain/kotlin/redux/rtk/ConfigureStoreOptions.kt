package redux.rtk

import redux.Action
import redux.Dispatch
import redux.Middleware
import redux.Reducer
import redux.StoreEnhancer

external interface ConfigureStoreOptions<S, A : Action> {
  val reducer: Reducer<S, A>
  val middleware: Array<out Middleware<S, Dispatch<A>>>?
  val devTools: Boolean?
  val duplicateMiddlewareCheck: Boolean?
  val preloadedState: S?
  val enhancers: Array<out StoreEnhancer>?
}
