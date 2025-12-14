@file:JsModule("@reduxjs/toolkit")

package rtk

external fun <P> createAction(
  type: String,
): (P) -> PayloadAction<P>
