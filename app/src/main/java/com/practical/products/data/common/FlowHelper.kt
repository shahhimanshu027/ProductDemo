package com.practical.products.data.common

import com.practical.products.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

/**
 *  extension function for Flow Class to emit loading state before the flow starts
 */
fun <T> Flow<HandleResult<T>>.onFlowStarts() =
    onStart { emit(HandleResult.Loading) }.catch { e: Throwable ->
        e.printStackTrace()
        emit(
            HandleResult.Error(
                DataSourceException
                    .Unexpected(R.string.error_client_unexpected_message),
            ),
        )
    }
