package redux.rtk

import redux.*

fun <S, A : Action> configureStore(
  reducer: Reducer<S, A>,
  middleware: Array<out Middleware<S, Dispatch<A>>>? = null,
  devTools: Boolean? = null,
  duplicateMiddlewareCheck: Boolean? = null,
  preloadedState: S? = null,
  enhancers: Array<out StoreEnhancer>? = null,
): Store<S, A> {
  var options = ConfigureStoreOptions(reducer)
  middleware?.let { options = ConfigureStoreOptions.copy(options, middleware = it) }
  devTools?.let { options = ConfigureStoreOptions.copy(options, devTools = it) }
  duplicateMiddlewareCheck?.let { options = ConfigureStoreOptions.copy(options, duplicateMiddlewareCheck = it) }
  preloadedState?.let { options = ConfigureStoreOptions.copy(options, preloadedState = it) }
  enhancers?.let { options = ConfigureStoreOptions.copy(options, enhancers = it) }
  return Rtk.configureStore(options)
}

@JsModule("@reduxjs/toolkit")
@JsNonModule
external object Rtk {
  fun <S, A : Action> configureStore(
    options: ConfigureStoreOptions<S, A>,
  ): Store<S, A>
}
