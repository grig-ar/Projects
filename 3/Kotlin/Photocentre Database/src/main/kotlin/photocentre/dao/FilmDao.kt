package photocentre.dao

import photocentre.dataClasses.*
import java.sql.Date
import java.sql.Statement
import java.sql.Types
import javax.sql.DataSource

class FilmDao(private val dataSource: DataSource) {

    fun createFilm(film: Film): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into films (film_name, sold_item_id, order_id) values(?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )
        statement.setString(1, film.name)
        if (film.soldItem != null) {
            statement.setLong(2, film.soldItem.id!!)
        } else {
            statement.setNull(2, Types.BIGINT)
        }
        if (film.order != null) {
            statement.setLong(3, film.order.id!!)
        } else {
            statement.setNull(3, Types.BIGINT)
        }
        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createFilms(toCreate: Iterable<Film>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into films (film_name, sold_item_id, order_id) values(?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (film in toCreate) {
            statement.setString(1, film.name)
            if (film.soldItem != null) {
                statement.setLong(2, film.soldItem.id!!)
            } else {
                statement.setNull(2, Types.BIGINT)
            }
            if (film.order != null) {
                statement.setLong(3, film.order.id!!)
            } else {
                statement.setNull(3, Types.BIGINT)
            }
            statement.addBatch()
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        val res = ArrayList<Long>()
        while (generated.next()) {
            res += generated.getLong(1)
        }
        return res
    }

    fun findFilm(id: Long): Film? {
        val statement = dataSource.connection.prepareStatement(
                "select film_id, film_name, sold_item_id, order_id from films where film_id = ?"
        )
        statement.setLong(1, id)
        val resultSet = statement.executeQuery()
        val soldItemDao = SoldItemDao(dataSource)
        val orderDao = OrderDao(dataSource)

        return if (resultSet.next()) {
            Film(
                    id = resultSet.getLong("film_id"),
                    name = resultSet.getString("film_name"),
                    soldItem = soldItemDao.findSoldItem(resultSet.getLong("sold_item_id")),
                    order = orderDao.findOrder(resultSet.getLong("order_id"))
            )
        } else {
            null
        }
    }

    fun updateFilm(film: Film) {
        val statement = dataSource.connection.prepareStatement(
                "update films set film_name = ?, sold_item_id = ?, order_id = ? where film_id = ?"
        )
        statement.setString(1, film.name)
        if (film.soldItem != null) {
            statement.setLong(2, film.soldItem.id!!)
        } else {
            statement.setNull(2, Types.BIGINT)
        }
        if (film.order != null) {
            statement.setLong(3, film.order.id!!)
        } else {
            statement.setNull(3, Types.BIGINT)
        }
        statement.setLong(4, film.id!!)

        statement.executeUpdate()
    }

    fun deleteFilm(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from films where film_id = ?"
        )
        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun getAmountByOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(film_id) as film_amount " +
                        "from orders " +
                        "left join films " +
                        "on films.order_id = orders.order_id " +
                        "where branch_office_id = ? " +
                        "and orders.order_date between ? and ? " +
                        "and orders.order_type in ('Film processing', 'Both')"
        )
        statement.setLong(1, branchOffice.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if(resultSet.next()) {
            resultSet.getInt("film_amount")
        } else {
            null
        }
    }

    fun getAmountByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(film_id) as film_amount " +
                        "from orders " +
                        "left join films " +
                        "on films.order_id = orders.order_id " +
                        "where kiosk_id = ? " +
                        "and orders.order_date between ? and ? " +
                        "and orders.order_type in ('Film processing', 'Both')"
        )
        statement.setLong(1, kiosk.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if(resultSet.next()) {
            resultSet.getInt("film_amount")
        } else {
            null
        }
    }

    fun getAmountByDate(dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(film_id) as film_amount " +
                        "from orders " +
                        "left join films " +
                        "on films.order_id = orders.order_id " +
                        "and orders.order_date between ? and ? " +
                        "and orders.order_type in ('Film processing', 'Both')"
        )
        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)

        val resultSet = statement.executeQuery()

        return if(resultSet.next()) {
            resultSet.getInt("film_amount")
        } else {
            null
        }
    }

    fun gelAll(): List<Film> {
        val statement = dataSource.connection.prepareStatement(
                "select * from films"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Film>()

        val soldItemDao = SoldItemDao(dataSource)
        val orderDao = OrderDao(dataSource)

        while (resultSet.next()) {
            res += Film(
                    id = resultSet.getLong("film_id"),
                    name = resultSet.getString("film_name"),
                    soldItem = soldItemDao.findSoldItem(resultSet.getLong("sold_item_id")),
                    order = orderDao.findOrder(resultSet.getLong("order_id"))
            )
        }
        return res
    }
}