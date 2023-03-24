package com.practical.products.data.common

/**
 * sealed class for different type of runtime exception
 */
sealed class DataSourceException(val messageResource: Int?) : RuntimeException() {
    class Connection(messageResource: Int) : DataSourceException(messageResource)
    class Unexpected(messageResource: Int) : DataSourceException(messageResource)
    class Client(messageResource: Int) : DataSourceException(messageResource)
    class Server(messageResource: Int?) : DataSourceException(messageResource)
}
