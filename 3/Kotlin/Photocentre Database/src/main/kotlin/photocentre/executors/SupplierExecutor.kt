package photocentre.executors

import photocentre.dao.SupplierDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Supplier
import photocentre.enums.ItemType
import photocentre.main.PhotocentreDataSource
import java.sql.Date

class SupplierExecutor(
        private val dataSource: PhotocentreDataSource,
        private val supplierDao: SupplierDao
) {
    fun createSupplier(toCreate: Supplier): Long {
        return transaction(dataSource) {
            supplierDao.createSupplier(toCreate)
        }
    }

    fun createSuppliers(toCreate: Iterable<Supplier>): List<Long> {
        return transaction(dataSource) {
            supplierDao.createSuppliers(toCreate)
        }
    }

    fun findSupplier(id: Long): Supplier? {
        return transaction(dataSource) {
            supplierDao.findSupplier(id)
        }
    }

    fun updateSupplier(supplier: Supplier) {
        return transaction(dataSource) {
            supplierDao.updateSupplier(supplier)
        }
    }

    fun deleteSupplier(id: Long) {
        return transaction(dataSource) {
            supplierDao.deleteSupplier(id)
        }
    }

    fun getAllSuppliers(): List<Supplier> {
        return transaction(dataSource) {
            supplierDao.getAll()
        }
    }

    fun getBySpecialization(specialization: ItemType): List<Supplier> {
        return transaction(dataSource) {
            supplierDao.getBySpecialization(specialization)
        }
    }

    fun getByDate(dateBegin: Date, dateEnd: Date): List<Supplier> {
        return transaction(dataSource) {
            supplierDao.getByDate(dateBegin, dateEnd)
        }
    }

    fun getByAmount(supplyItemAmount: Int, dateBegin: Date, dateEnd: Date): List<Pair<Supplier, Int>> {
        return transaction(dataSource) {
            supplierDao.getByAmount(supplyItemAmount, dateBegin, dateEnd)
        }
    }

    fun getTopSuppliersByBranchOffice(branchOffice: BranchOffice): List<Pair<Supplier, Int>> {
        return transaction(dataSource) {
            supplierDao.getTopSuppliersByBranchOffice(branchOffice)
        }
    }

    fun getTopSuppliers(): List<Pair<Supplier, Int>> {
        return transaction(dataSource) {
            supplierDao.getTopSuppliers()
        }
    }
}