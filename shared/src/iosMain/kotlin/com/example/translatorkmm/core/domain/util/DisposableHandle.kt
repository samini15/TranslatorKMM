package com.example.translatorkmm.core.domain.util

import kotlinx.coroutines.DisposableHandle

fun interface DisposableHandle: DisposableHandle

/**
 * Same as above
 */
/*fun DisposableHandle(block: () -> Unit): DisposableHandle {
    return DisposableHandle { block() }
}*/