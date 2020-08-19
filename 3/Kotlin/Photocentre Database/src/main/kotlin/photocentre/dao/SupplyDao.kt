package photocentre.dao

import photocentre.dataClasses.Supply
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class SupplyDao(private val dataSource: DataSource) {

    fun createSupply(supply: Supply): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into supplies (supply_cost, supply_date, supply_completion_date, supplier_id) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setFloat(1, supply.cost)
        statement.setDate(2, supply.date)
        statement.setDate(3, supply.completionDate)
        if (supply.supplier != null) {
            statement.setLong(4, supply.supplier!!.id!!)
        } else {
            statement.setNull(4, BIGINT)
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createSupplies(toCreate: Iterable<Supply>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into supplies (supply_cost, supply_date, supply_completion_date, supplier_id) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (supply in toCreate) {
            statement.setFloat(1, supply.cost)
            statement.setDate(2, supply.date)
            statement.setDate(3, supply.completionDate)
            if (supply.supplier != null) {
                statement.setLong(4, supply.supplier!!.id!!)
            } else {
                statement.setNull(4, BIGINT)
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

    fun findSupply(id: Long): Supply? {
        val statement = dataSource.connection.prepareStatement(
                "select supply_id, supply_cost, supply_date, supply_completion_date, supplier_id from supplies where supply_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        val supplierDao = SupplierDao(dataSource)

        return if (resultSet.next()) {
            Supply(
                    id = resultSet.getLong("supply_id"),
                    cost = resultSet.getFloat("supply_cost"),
                    date = resultSet.getDate("supply_date"),
                    completionDate = resultSet.getDate("supply_completion_date"),
                    supplier = supplierDao.findSupplier(resultSet.getLong("supplier_id"))
            )
        } else {
            null
        }
    }

    fun updateSupply(supply: Supply) {
        val statement = dataSource.connection.prepareStatement(
                "update supplies set supply_cost = ?, supply_date = ?, supply_completion_date = ?, supplier_id = ? where supply_id = ?"
        )

        statement.setFloat(1, supply.cost)
        statement.setDate(2, supply.date)
        statement.setDate(3, supply.completionDate)
        if (supply.supplier != null) {
            statement.setLong(4, supply.supplier!!.id!!)
        } else {
            statement.setNull(4, BIGINT)
        }
        statement.setLong(5, supply.id!!)

        statement.executeUpdate()
    }

    fun deleteSupply(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from supplies where supply_id = ?"
        )

        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun gelAll(): List<Supply> {
        val statement = dataSource.connection.prepareStatement(
                "select * from supplies"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Supply>()

        val supplierDao = SupplierDao(dataSource)

        while (resultSet.next()) {

            res += Supply(
                    id = resultSet.getLong("supply_id"),
                    cost = resultSet.getFloat("supply_cost"),
                    date = resultSet.getDate("supply_date"),
                    completionDate = resultSet.getDate("supply_completion_date"),
                    supplier = supplierDao.findSupplier(resultSet.getLong("supplier_id"))
            )
        }
        return res
    }
}