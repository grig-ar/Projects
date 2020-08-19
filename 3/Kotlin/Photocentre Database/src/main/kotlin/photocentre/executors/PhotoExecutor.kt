package photocentre.executors

import photocentre.dao.PhotoDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.Photo
import photocentre.main.PhotocentreDataSource
import java.sql.Date

class PhotoExecutor(
        private val dataSource: PhotocentreDataSource,
        private val photoDao: PhotoDao
) {
    fun createPhoto(toCreate: Photo): Long {
        return transaction(dataSource) {
            photoDao.createPhoto(toCreate)
        }
    }

    fun createPhotos(toCreate: Iterable<Photo>): List<Long> {
        return transaction(dataSource) {
            photoDao.createPhotos(toCreate)
        }
    }

    fun findPhoto(id: Long): Photo? {
        return transaction(dataSource) {
            photoDao.findPhoto(id)
        }
    }

    fun updatePhoto(photo: Photo) {
        return transaction(dataSource) {
            photoDao.updatePhoto(photo)
        }
    }

    fun deletePhoto(id: Long) {
        return transaction(dataSource) {
            photoDao.deletePhoto(id)
        }
    }

    fun countByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            photoDao.countByKiosk(kiosk, dateBegin, dateEnd)
        }
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            photoDao.countByBranchOffice(branchOffice, dateBegin, dateEnd)
        }
    }

    fun countByDate(dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            photoDao.countByDate(dateBegin, dateEnd)
        }
    }

    fun getAllPhotos(): List<Photo> {
        return transaction(dataSource) {
            photoDao.gelAll()
        }
    }

}