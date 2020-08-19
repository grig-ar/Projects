package photocentre.dao

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.Photo
import photocentre.enums.PaperType
import photocentre.enums.PhotoFormat
import java.sql.Date
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class PhotoDao(private val dataSource: DataSource) {

    fun createPhoto(photo: Photo): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into photos (photo_paper_type, photo_format, film_id) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, photo.paperType.toString())
        statement.setString(2, photo.format.toString())
        if (photo.film != null) {
            statement.setLong(3, photo.film.id!!)
        } else {
            statement.setNull(3, BIGINT)
        }
        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createPhotos(toCreate: Iterable<Photo>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into photos (photo_paper_type, photo_format, film_id) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (photo in toCreate) {
            statement.setString(1, photo.paperType.toString())
            statement.setString(2, photo.format.toString())
            if (photo.film != null) {
                statement.setLong(3, photo.film.id!!)
            } else {
                statement.setNull(3, BIGINT)
            }
            statement.addBatch()
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        val res = ArrayList<Long>()
        while(generated.next()) {
            res += generated.getLong(1)
        }

        return res
    }

    fun findPhoto(id: Long): Photo? {
        val statement = dataSource.connection.prepareStatement(
                "select photo_id, photo_paper_type, photo_format, film_id from photos where photo_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        val filmDao = FilmDao(dataSource)

        if (resultSet.next()) {
            val paperType = when (resultSet.getString("photo_paper_type")) {
                "PHOTO" -> PaperType.PHOTO
                "CHEAP" -> PaperType.CHEAP
                "EXPENSIVE" -> PaperType.EXPENSIVE
                else -> null
            }

            val photoFormat = when (resultSet.getString("photo_format")) {
                "FORMAT_10_15" -> PhotoFormat.FORMAT_10_15
                "A5" -> PhotoFormat.A5
                "A4" -> PhotoFormat.A4
                "A3" -> PhotoFormat.A3
                else -> null
            }

            return Photo(
                    id = resultSet.getLong("photo_id"),
                    paperType = paperType,
                    format = photoFormat,
                    film = filmDao.findFilm(resultSet.getLong("film_id"))
            )
        } else {
            return null
        }
    }

    fun updatePhoto(photo: Photo) {
        val statement = dataSource.connection.prepareStatement(
                "update photos set photo_paper_type = ?, photo_format = ?, film_id = ? where photo_id = ?"
        )
        statement.setString(1, photo.paperType.toString())
        statement.setString(2, photo.format.toString())
        if (photo.film != null) {
            statement.setLong(3, photo.film.id!!)
        } else {
            statement.setNull(3, BIGINT)
        }
        statement.setLong(4, photo.id!!)
        statement.executeUpdate()
    }

    fun deletePhoto(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from photos where photo_id = ?"
        )
        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun countByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(photo_id) as photo_amount" +
                        "from Photos " +
                        "join Films " +
                        "on Photos.film_id = Films.film_id " +
                        "join Orders " +
                        "on Films.order_id = Orders.order_id " +
                        "where Orders.kiosk_id = ? " +
                        "and Orders.order_date between ? and ?"
        )

        statement.setLong(1, kiosk.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if(resultSet.next()) {
            resultSet.getInt("photo_amount")
        } else {
            null
        }
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(photo_id) as photo_amount" +
                        "from Photos " +
                        "join Films " +
                        "on Photos.film_id = Films.film_id " +
                        "join Orders " +
                        "on Films.order_id = Orders.order_id " +
                        "where Orders.branch_office_id = ? " +
                        "and Orders.order_date between ? and ?"
        )

        statement.setLong(1, branchOffice.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if(resultSet.next()) {
            resultSet.getInt("photo_amount")
        } else {
            null
        }
    }

    fun countByDate(dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(photo_id) as photo_amount" +
                        "from Photos " +
                        "join Films " +
                        "on Photos.film_id = Films.film_id " +
                        "join Orders " +
                        "on Films.order_id = Orders.order_id " +
                        "where Orders.order_date between ? and ?"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)

        val resultSet = statement.executeQuery()

        return if(resultSet.next()) {
            resultSet.getInt("photo_amount")
        } else {
            null
        }
    }

    fun gelAll(): List<Photo> {
        val statement = dataSource.connection.prepareStatement(
                "select * from photos"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Photo>()

        val filmDao = FilmDao(dataSource)

        while (resultSet.next()) {
            val paperType = when (resultSet.getString("photo_paper_type")) {
                "PHOTO" -> PaperType.PHOTO
                "CHEAP" -> PaperType.CHEAP
                "EXPENSIVE" -> PaperType.EXPENSIVE
                else -> null
            }

            val photoFormat = when (resultSet.getString("photo_format")) {
                "FORMAT_10_15" -> PhotoFormat.FORMAT_10_15
                "A5" -> PhotoFormat.A5
                "A4" -> PhotoFormat.A4
                "A3" -> PhotoFormat.A3
                else -> null
            }

            res += Photo(
                    id = resultSet.getLong("photo_id"),
                    paperType = paperType,
                    format = photoFormat,
                    film = filmDao.findFilm(resultSet.getLong("film_id"))
            )
        }
        return res
    }
}