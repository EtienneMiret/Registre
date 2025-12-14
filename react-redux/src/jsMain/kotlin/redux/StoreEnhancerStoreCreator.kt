package redux

external interface StoreEnhancerStoreCreator {
  operator fun <S, A : Action> invoke(
    reducer : Reducer<S, A>,
    preloadedState: S? = definedExternally,
  ): Store<S, A>
}
