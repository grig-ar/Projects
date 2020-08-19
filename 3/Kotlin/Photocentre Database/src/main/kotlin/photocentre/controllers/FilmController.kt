package photocentre.controllers

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Film
import photocentre.dataClasses.Kiosk
import photocentre.executors.FilmExecutor
import java.sql.Date

class FilmController(private val executor: FilmExecutor) {

    fun createFilm(film: Film): Film {
        val id = executor.createFilm(film)
        return Film(
                id = id,
                name = film.name,
                soldItem = film.soldItem,
                order = film.order
        )
    }

    fun createFilms(films: List<Film>): List<Film> {
        val ids = executor.createFilms(films)
        val newFilms = ArrayList<Film>()

        for (i in 1..ids.size) {
            newFilms += Film(
                    id = ids[i],
                    name = films[i].name,
                    soldItem = films[i].soldItem,
                    order = films[i].order
            )
        }
        return newFilms
    }

    fun getFilm(id: Long): String {
       return executor.findFilm(id).toString()
    }

    fun updateFilm(film: Film): String {
        return executor.updateFilm(film).toString()
    }

    fun deleteFilm(id: Long): String {
        return executor.deleteFilm(id).toString()
    }

    fun getAmountByOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        return executor.getAmountByOffice(branchOffice, dateBegin, dateEnd)
    }

    fun getAmountByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        return executor.getAmountByKiosk(kiosk, dateBegin, dateEnd)
    }

    fun getAmountByDate(dateBegin: Date, dateEnd: Date): Int? {
        return executor.getAmountByDate(dateBegin, dateEnd)
    }

    fun getAllFilms(): List<Film> {
        return executor.getAllFilms()
    }

}