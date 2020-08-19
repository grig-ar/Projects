package photocentre.executors

import photocentre.dao.FilmDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Film
import photocentre.dataClasses.Kiosk
import photocentre.main.PhotocentreDataSource
import java.sql.Date

class FilmExecutor(
        private val dataSource: PhotocentreDataSource,
        private val filmDao: FilmDao
) {
    fun createFilm(toCreate: Film): Long {
        return transaction(dataSource) {
            filmDao.createFilm(toCreate)
        }
    }

    fun createFilms(toCreate: Iterable<Film>): List<Long> {
        return transaction(dataSource) {
            filmDao.createFilms(toCreate)
        }
    }

    fun findFilm(id: Long): Film? {
        return transaction(dataSource) {
            filmDao.findFilm(id)
        }
    }

    fun updateFilm(film: Film) {
        return transaction(dataSource) {
            filmDao.updateFilm(film)
        }
    }

    fun deleteFilm(id: Long) {
        return transaction(dataSource) {
            filmDao.deleteFilm(id)
        }
    }

    fun getAmountByOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            filmDao.getAmountByOffice(branchOffice, dateBegin, dateEnd)
        }
    }

    fun getAmountByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            filmDao.getAmountByKiosk(kiosk, dateBegin, dateEnd)
        }
    }

    fun getAmountByDate(dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            filmDao.getAmountByDate(dateBegin, dateEnd)
        }
    }

    fun getAllFilms(): List<Film> {
        return transaction(dataSource) {
            filmDao.gelAll()
        }
    }
}