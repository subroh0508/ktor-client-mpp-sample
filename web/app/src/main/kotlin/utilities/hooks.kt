package utilities

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import mainScope
import react.*

fun useEffectDidMount(effect: EffectBuilder.() -> Unit) = useEffect(effect)

fun <T> useAsyncEffect(
    value: T,
    coroutineScope: CoroutineScope = mainScope,
    effect: suspend () -> Unit
) {
    val scope = useRef(coroutineScope)

    useEffect(value) { scope.current?.launch { effect() } }
}

fun <T> useDebounceEffect(
    value: T,
    timeoutMillis: Long,
    coroutineScope: CoroutineScope = mainScope,
    effect: suspend (T) -> Unit
) {
    val scope = useRef(coroutineScope)
    val channelRef = useRef(Channel<T>().apply {
        scope.current?.launch {
            consumeAsFlow()
                .debounce(timeoutMillis)
                .collect { effect(it) }
        }
    })

    useEffect(value) { channelRef.current?.trySend(value) }
}
