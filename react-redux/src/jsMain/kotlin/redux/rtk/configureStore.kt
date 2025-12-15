package redux.rtk

import redux.Action
import redux.Dispatch
import redux.Middleware
import redux.Reducer
import redux.Store
import redux.StoreEnhancer

fun <S, A : Action> configureStore(
  reducer: Reducer<S, A>,
  middleware: Array<out Middleware<S, Dispatch<A>>>? = null,
  devTools: Boolean? = null,
  duplicateMiddlewareCheck: Boolean? = null,
  preloadedState: S? = null,
  enhancers: Array<out StoreEnhancer>? = null,
): Store<S, A> {
  val options = object : ConfigureStoreOptions<S, A> {
    override val reducer: Reducer<S, A> = reducer
    override val middleware: Array<out Middleware<S, Dispatch<A>>>? = middleware
    override val devTools: Boolean? = devTools
    override val duplicateMiddlewareCheck: Boolean? = duplicateMiddlewareCheck
    override val preloadedState: S? = preloadedState
    override val enhancers: Array<out StoreEnhancer>? = enhancers
  }
  return Rtk.configureStore(options)
}

@JsModule("@reduxjs/toolkit")
@JsNonModule
external object Rtk {
  fun <S, A : Action> configureStore(
    options: ConfigureStoreOptions<S, A>,
  ): Store<S, A>
}
