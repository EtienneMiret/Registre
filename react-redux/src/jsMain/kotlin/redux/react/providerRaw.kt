@file:JsModule("react-redux")
@file:JsNonModule

package redux.react

import react.PropsWithChildren
import react.ReactElement
import redux.Action
import redux.Store

external interface ProviderProps<A : Action, S>: PropsWithChildren {
  var store: Store<S, A>
}

@JsName("Provider")
external fun <A : Action, S> providerRaw(
  props: ProviderProps<A, S>,
): ReactElement<*>
