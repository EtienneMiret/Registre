package redux.react

import react.FC
import redux.Action


fun <A: Action, S> provider() = FC<ProviderProps<A, S>>("Provider") {
  +providerRaw(it)
}
