package redux

external interface MiddlewareAPI<D : Dispatch<*>, S> {
  val dispatch: D
  fun getState(): S
}
