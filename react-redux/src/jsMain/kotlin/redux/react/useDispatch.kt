@file:JsModule("react-redux")

package redux.react

import redux.Action
import redux.Dispatch

external fun <A : Action> useDispatch(): Dispatch<A>
