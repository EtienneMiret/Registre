package redux

external interface Store<S, A : Action> {
  val dispatch: Dispatch<A>
  fun getState(): S
  fun subscribe(listener: () -> Unit): () -> Unit
  fun replaceReducer(nextReducer: Reducer<S, A>)
}
