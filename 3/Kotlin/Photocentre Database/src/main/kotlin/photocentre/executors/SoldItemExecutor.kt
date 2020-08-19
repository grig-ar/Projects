package photocentre.executors

import photocentre.dao.SoldItemDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.SoldItem
import photocentre.main.PhotocentreDataSource
import java.sql.Date

class SoldItemExecutor(
        private val dataSource: PhotocentreDataSource,
        private val soldItemDao: SoldItemDao
) {
    fun createSoldItem(toCreate: SoldItem): Long {
        return transaction(dataSource) {
            soldItemDao.createSoldItem(toCreate)
        }
    }

    fun createSoldItems(toCreate: Iterable<SoldItem>): List<Long> {
        return transaction(dataSource) {
            soldItemDao.createSoldItems(toCreate)
        }
    }

    fun findSoldItem(id: Long): SoldItem? {
        return transaction(dataSource) {
            soldItemDao.findSoldItem(id)
        }
    }

    fun updateSoldItem(soldItem: SoldItem) {
        return transaction(dataSource) {
            soldItemDao.updateSoldItem(soldItem)
        }
    }

    fun deleteSoldItem(id: Long) {
        return transaction(dataSource) {
            soldItemDao.deleteSoldItem(id)
        }
    }

    fun getRevenueByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Float? {
        return transaction(dataSource) {
            soldItemDao.getRevenueByBranchOffice(branchOffice, dateBegin, dateEnd)
        }
    }

    fun getRevenueByDate(dateBegin: Date, dateEnd: Date): Float? {
        return transaction(dataSource) {
            soldItemDao.getRevenueByDate(dateBegin, dateEnd)
        }
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): List<Pair<SoldItem, Int>> {
        return transaction(dataSource) {
            soldItemDao.countByBranchOffice(branchOffice, dateBegin, dateEnd)
        }
    }

    fun countByDate(dateBegin: Date, dateEnd: Date): List<Pair<SoldItem, Int>> {
        return transaction(dataSource) {
            soldItemDao.countByDate(dateBegin, dateEnd)
        }
    }

    fun getAllSoldItems(): List<SoldItem> {
        return transaction(dataSource) {
            soldItemDao.gelAll()
        }
    }

}