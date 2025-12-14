@file:JsModule("@reduxjs/toolkit")
@file:JsNonModule

package rtk

import redux.Action
import redux.Store

@JsName("configureStore")
external fun <S, A : Action> configureStoreRaw(
  options: ConfigureStoreOptions<S, A>,
): Store<S, A>
