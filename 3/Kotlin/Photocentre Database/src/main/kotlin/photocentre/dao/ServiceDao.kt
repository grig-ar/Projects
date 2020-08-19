package photocentre.dao

import photocentre.dataClasses.BranchOffice
//import photocentre.dataClasses.Professional
import photocentre.dataClasses.Service
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class ServiceDao(private val dataSource: DataSource) {

    fun createService(service: Service): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into services (service_name, service_cost, branch_office_id) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, service.name)
        statement.setFloat(2, service.cost)
        if (service.branchOffice != null) {
            statement.setLong(3, service.branchOffice.id!!)
        } else {
            statement.setNull(3, BIGINT)
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createServices(toCreate: Iterable<Service>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into services (service_name, service_cost, branch_office_id) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (service in toCreate) {
            statement.setString(1, service.name)
            statement.setFloat(2, service.cost)
            if (service.branchOffice != null) {
                statement.setLong(3, service.branchOffice.id!!)
            } else {
                statement.setNull(3, BIGINT)
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

    fun findService(id: Long): Service? {
        val statement = dataSource.connection.prepareStatement(
                "select service_id, service_name, service_cost, branch_office_id from services where service_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        val branchOfficeDao = BranchOfficeDao(dataSource)

        return if (resultSet.next()) {
            Service(
                    id = resultSet.getLong("service_id"),
                    name = resultSet.getString("service_name"),
                    cost = resultSet.getFloat("service_cost"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        } else {
            null
        }
    }

    fun updateService(service: Service) {
        val statement = dataSource.connection.prepareStatement(
                "update services set service_name = ?, service_cost = ?, branch_office_id = ? where service_id = ?"
        )
        statement.setString(1, service.name)
        statement.setFloat(2, service.cost)
        if (service.branchOffice != null) {
            statement.setLong(3, service.branchOffice.id!!)
        } else {
            statement.setNull(3, BIGINT)
        }
        statement.setLong(4, service.id!!)
        statement.executeUpdate()
    }

    fun deleteService(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from services where service_id = ?"
        )

        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun gelAll(): List<Service> {
        val statement = dataSource.connection.prepareStatement(
                "select * from services"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Service>()

        val branchOfficeDao = BranchOfficeDao(dataSource)

        while (resultSet.next()) {

            res += Service(
                    id = resultSet.getLong("service_id"),
                    name = resultSet.getString("service_name"),
                    cost = resultSet.getFloat("service_cost"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        }
        return res
    }
}