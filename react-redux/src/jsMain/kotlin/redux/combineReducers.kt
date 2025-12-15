@file:JsModule("redux")

package redux

external fun <S, A> combineReducers(reducers: Any): Reducer<S, A>
