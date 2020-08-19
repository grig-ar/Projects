package photocentre.executors

import photocentre.dao.KioskDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Kiosk
import photocentre.main.PhotocentreDataSource

class KioskExecutor(
        private val dataSource: PhotocentreDataSource,
        private val kioskDao: KioskDao
) {
    fun createKiosk(toCreate: Kiosk): Long {
        return transaction(dataSource) {
            kioskDao.createKiosk(toCreate)
        }
    }

    fun createKiosks(toCreate: Iterable<Kiosk>): List<Long> {
        return transaction(dataSource) {
            kioskDao.createKiosks(toCreate)
        }
    }

    fun findKiosk(id: Long): Kiosk? {
        return transaction(dataSource) {
            kioskDao.findKiosk(id)
        }
    }

    fun updateKiosk(kiosk: Kiosk) {
        return transaction(dataSource) {
            kioskDao.updateKiosk(kiosk)
        }
    }

    fun deleteKiosk(id: Long) {
        return transaction(dataSource) {
            kioskDao.deleteKiosk(id)
        }
    }

    fun countKiosks(): Int? {
        return transaction(dataSource) {
            kioskDao.countKiosks()
        }
    }

    fun getAllKiosks(): List<Kiosk> {
        return transaction(dataSource) {
            kioskDao.gelAll()
        }
    }

    fun getAllBranchOfficeAddresses() : List<String> {
        return transaction(dataSource) {
            kioskDao.getAllAddresses()
        }
    }

}