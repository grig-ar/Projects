package photocentre.dao

import photocentre.dataClasses.Worker
import photocentre.enums.AreaOfWork
import photocentre.enums.ItemType
import photocentre.enums.Position
import java.sql.Statement
import javax.sql.DataSource

class WorkerDao(private val dataSource: DataSource) {

    fun createWorker(worker: Worker): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into workers (worker_name, worker_area_of_work, worker_position) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, worker.name)
        statement.setString(2, worker.areaOfWork.toString())
        statement.setString(3, worker.position.toString())

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createWorkers(toCreate: Iterable<Worker>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into workers (worker_name, worker_area_of_work, worker_position) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )
        for (worker in toCreate) {
            statement.setString(1, worker.name)
            statement.setString(2, worker.areaOfWork.toString())
            statement.setString(3, worker.position.toString())
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

    fun findWorker(id: Long): Worker? {
        val statement = dataSource.connection.prepareStatement(
                "select worker_id, worker_name, worker_area_of_work, worker_position from workers where worker_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val area = when (resultSet.getString("worker_area_of_work")) {
                "MANAGEMENT" -> AreaOfWork.MANAGEMENT
                "PROCESSING" -> AreaOfWork.PROCESSING
                "PHOTOGRAPHY" -> AreaOfWork.PHOTOGRAPHY
                else -> null
            }
            val position = when (resultSet.getString("worker_position")) {
                "LOW" -> Position.LOW
                "MIDDLE" -> Position.MIDDLE
                "TOP" -> Position.TOP
                else -> null
            }


            return Worker(
                    id = resultSet.getLong("worker_id"),
                    name = resultSet.getString("worker_name"),
                    areaOfWork = area,
                    position = position
            )
        } else {
            return null
        }
    }

    fun updateWorker(worker: Worker) {
        val statement = dataSource.connection.prepareStatement(
                "update workers set worker_name = ?, worker_area_of_work = ?, worker_position = ? where worker_id = ?"
        )

        statement.setString(1, worker.name)
        statement.setString(2, worker.areaOfWork.toString())
        statement.setString(3, worker.position.toString())
        statement.setLong(4, worker.id!!)

        statement.executeUpdate()
    }

    fun deleteWorker(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from workers where worker_id = ?"
        )

        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun getBySpeciazization(areaOfWork: AreaOfWork): List<Worker> {
        val statement = dataSource.connection.prepareStatement(
                "select worker_name, worker_area_of_work " +
                        "from workers " +
                        "where worker_area_of_work in (?) " +
                        "order by worker_area_of_work"
        )

        statement.setString(1, areaOfWork.toString())
        val resultSet = statement.executeQuery()
        val res = ArrayList<Worker>()

        while (resultSet.next()) {
            res += Worker(
                    name = resultSet.getString("worker_name"),
                    areaOfWork = areaOfWork
            )
        }
        return res
    }

    fun getAll(): List<Worker> {
        val statement = dataSource.connection.prepareStatement(
                "select * from workers"
        )

        val resultSet = statement.executeQuery()
        val res = ArrayList<Worker>()

        while (resultSet.next()) {
            val area = when (resultSet.getString("worker_area_of_work")) {
                "MANAGEMENT" -> AreaOfWork.MANAGEMENT
                "PROCESSING" -> AreaOfWork.PROCESSING
                "PHOTOGRAPHY" -> AreaOfWork.PHOTOGRAPHY
                else -> null
            }
            val position = when (resultSet.getString("worker_position")) {
                "LOW" -> Position.LOW
                "MIDDLE" -> Position.MIDDLE
                "TOP" -> Position.TOP
                else -> null
            }

            res += Worker(
                    id = resultSet.getLong("worker_id"),
                    name = resultSet.getString("worker_name"),
                    areaOfWork = area,
                    position = position
            )
        }
        return res
    }
}