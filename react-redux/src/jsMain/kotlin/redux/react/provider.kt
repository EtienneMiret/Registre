package redux.react

import react.FC
import react.PropsWithChildren
import react.ReactElement
import redux.Action
import redux.Store


fun <A: Action, S> provider() = FC<ProviderProps<A, S>>("Provider") {
  +ReactRedux.Provider(it)
}

external interface ProviderProps<A : Action, S>: PropsWithChildren {
  var store: Store<S, A>
}

@JsModule("react-redux")
@JsNonModule
external object ReactRedux {
  fun <A : Action, S> Provider(
    props: ProviderProps<A, S>,
  ): ReactElement<*>
}
