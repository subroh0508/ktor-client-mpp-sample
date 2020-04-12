package utilities

import kotlinext.js.Object
import kotlinext.js.jsObject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import mainScope
import react.*
import react.router.dom.RouteResultMatch

fun useEffectDidMount(effect: () -> Unit) = useEffect(listOf(), effect)

fun <T> useAsyncEffect(
    value: T,
    coroutineScope: CoroutineScope = mainScope,
    effect: suspend () -> Unit
) {
    val scope = useRef(coroutineScope)

    useEffect(listOf(value)) { scope.current.launch { effect() } }
}

fun <T> useDebounceEffect(
    value: T,
    timeoutMillis: Long,
    coroutineScope: CoroutineScope = mainScope,
    effect: suspend (T) -> Unit
) {
    val scope = useRef(coroutineScope)
    val channelRef = useRef(Channel<T>().apply {
        scope.current.launch {
            consumeAsFlow()
                .debounce(timeoutMillis)
                .collect { effect(it) }
        }
    })

    useEffect(listOf(value)) { channelRef.current.offer(value) }
}

fun <T: RProps> useParams(): T? {
    val rawParams = rawUseParams()

    return if (Object.keys(rawParams as Any).isEmpty()) null else rawParams as T
}

fun <T: RProps> useRouteMatch(
    exact: Boolean = false,
    strict: Boolean = false,
    sensitive: Boolean = false,
    vararg path: String
): RouteResultMatch<T>? {
    val options: RouteMatchOptions = jsObject {
        this.path = path
        this.exact = exact
        this.strict = strict
        this.sensitive = sensitive
    }

    return rawRouteMatch(options)
}

external interface RouteMatchOptions {
    var path: Array<out String>
    var exact: Boolean
    var strict: Boolean
    var sensitive: Boolean
}
