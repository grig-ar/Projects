package photocentre.executors

import photocentre.dao.OfficeItemDao
import photocentre.dataClasses.OfficeItem
import photocentre.main.PhotocentreDataSource

class OfficeItemExecutor(
        private val dataSource: PhotocentreDataSource,
        private val officeItemDao: OfficeItemDao
) {
    fun createOfficeItem(toCreate: OfficeItem): Long {
        return transaction(dataSource) {
            officeItemDao.createOfficeItem(toCreate)
        }
    }

    fun createOfficeItems(toCreate: Iterable<OfficeItem>): List<Long> {
        return transaction(dataSource) {
            officeItemDao.createOfficeItems(toCreate)
        }
    }

    fun findOfficeItem(id: Long): OfficeItem? {
        return transaction(dataSource) {
            officeItemDao.findOfficeItem(id)
        }
    }

    fun updateOfficeItem(officeItem: OfficeItem) {
        return transaction(dataSource) {
            officeItemDao.updateOfficeItem(officeItem)
        }
    }

    fun deleteOfficeItem(id: Long) {
        return transaction(dataSource) {
            officeItemDao.deleteOfficeItem(id)
        }
    }

    fun getAllOfficeItems(): List<OfficeItem> {
        return transaction(dataSource) {
            officeItemDao.gelAll()
        }
    }
}