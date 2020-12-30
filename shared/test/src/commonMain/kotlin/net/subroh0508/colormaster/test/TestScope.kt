package net.subroh0508.colormaster.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Suppress("FunctionName")
fun TestScope() = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

inline fun <T> Flow<T>.collectOnTestScope(
    crossinline action: suspend (value: T) -> Unit,
) = TestScope().launch { collect(action) }
