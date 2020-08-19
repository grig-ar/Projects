package photocentre.controllers

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.Photo
import photocentre.executors.PhotoExecutor
import java.sql.Date

class PhotoController(private val executor: PhotoExecutor) {

    fun createPhoto(photo: Photo): Photo {
        val id = executor.createPhoto(photo)
        return Photo(
                id = id,
                paperType = photo.paperType,
                format = photo.format,
                film = photo.film
        )
    }

    fun createPhotos(photos: List<Photo>): List<Photo> {

        val ids = executor.createPhotos(photos)
        val newPhotos = ArrayList<Photo>()

        for (i in 1..ids.size) {
            newPhotos += Photo(
                    id = ids[i],
                    paperType = photos[i].paperType,
                    format = photos[i].format,
                    film = photos[i].film
            )
        }
        return newPhotos

    }

    fun getPhoto(id: Long): String {
        return executor.findPhoto(id).toString()
    }

    fun updatePhoto(photo: Photo): String {
        return executor.updatePhoto(photo).toString()
    }

    fun deletePhoto(id: Long): String {
        return executor.deletePhoto(id).toString()
    }

    fun countByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        return executor.countByKiosk(kiosk, dateBegin, dateEnd)
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        return executor.countByBranchOffice(branchOffice, dateBegin, dateEnd)
    }

    fun countByDate(dateBegin: Date, dateEnd: Date): Int? {
        return executor.countByDate(dateBegin, dateEnd)
    }

    fun getAllPhotos(): List<Photo> {
        return executor.getAllPhotos()
    }
}