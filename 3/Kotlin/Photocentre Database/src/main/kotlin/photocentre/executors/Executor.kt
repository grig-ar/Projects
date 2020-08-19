package photocentre.executors

import photocentre.main.PhotocentreDataSource

fun <T> transaction(dataSource: PhotocentreDataSource, body: () -> T) : T {
    dataSource.realGetConnection().use {
        it.autoCommit = false
        dataSource.connection = it
        try {
            val res = body()
            it.commit()
            return res
        } catch (e: Exception) {
            it.rollback()
            throw e
        }
    }
}