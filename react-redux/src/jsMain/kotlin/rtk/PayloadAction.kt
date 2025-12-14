package rtk

import redux.Action

external interface PayloadAction<P> : Action {
  val payload: P
}
