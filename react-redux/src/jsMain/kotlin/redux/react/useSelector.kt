@file:JsModule("react-redux")

package redux.react

external fun <S, T> useSelector(selector: (state: S) -> T): T
