@file:JsModule("@reduxjs/toolkit")

package redux.rtk

import redux.Action

external fun <P> createAction(
  type: String,
): (P) -> PayloadAction<P>

external fun createAction(
  type: String,
): () -> Action
