@file:JsModule("@reduxjs/toolkit")

package redux.rtk

external fun <P> createAction(
  type: String,
): (P) -> PayloadAction<P>
