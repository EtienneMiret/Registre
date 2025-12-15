@file:JsModule("react-redux")

package redux.react

import redux.Action
import redux.Store

external fun <S, A : Action> useStore(): Store<S, A>
