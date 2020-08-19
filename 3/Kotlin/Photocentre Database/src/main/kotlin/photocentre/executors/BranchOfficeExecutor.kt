package photocentre.executors

import photocentre.dao.BranchOfficeDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.FilteredBranchOffice
import photocentre.dataClasses.Kiosk
import photocentre.main.PhotocentreDataSource

class BranchOfficeExecutor(
        private val dataSource: PhotocentreDataSource,
        private val branchOfficeDao: BranchOfficeDao
) {
    fun createBranchOffice(toCreate: BranchOffice): Long {
        return transaction(dataSource) {
            branchOfficeDao.createBranchOffice(toCreate)
        }
    }

    fun createBranchOffices(toCreate: Iterable<BranchOffice>): List<Long> {
        return transaction(dataSource) {
            branchOfficeDao.createBranchOffices(toCreate)
        }
    }

    fun findBranchOffice(id: Long): BranchOffice? {
        return transaction(dataSource) {
            branchOfficeDao.findBranchOffice(id)
        }
    }

    fun updateBranchOffice(branchOffice: BranchOffice) {
        return transaction(dataSource) {
            branchOfficeDao.updateBranchOffice(branchOffice)
        }
    }

    fun deleteBranchOffice(id: Long) {
        return transaction(dataSource) {
            branchOfficeDao.deleteBranchOffice(id)
        }
    }

    fun countBranchOffices(): Int? {
        return transaction(dataSource) {
            branchOfficeDao.countBranchOffices()
        }
    }

    fun getAllBranchOffices(): List<BranchOffice> {
        return transaction(dataSource) {
            branchOfficeDao.gelAll()
        }
    }

    fun getBranchOfficesAndKiosks(): List<Pair<BranchOffice, Kiosk>> {
        return transaction(dataSource) {
            branchOfficeDao.selectBranchOfficesAndKiosks()
        }
    }

    fun filterBranchOffices(
            id: Long,
            address: String,
            amount: Int
    ): List<FilteredBranchOffice> {

            return transaction(dataSource) {
                branchOfficeDao.filterOffices(id, address, amount)
            }

    }
}