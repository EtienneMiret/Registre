package redux

typealias Middleware<S, D> =
      (MiddlewareAPI<D, S>) -> ((Any?) -> Any?) -> (Any?) -> Any?
