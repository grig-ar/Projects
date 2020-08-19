package photocentre.dao

import photocentre.dataClasses.SupplyItem
import photocentre.enums.ItemType
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class SupplyItemDao(private val dataSource: DataSource) {

    fun createSupplyItem(item: SupplyItem): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into supply_items (supply_item_name, supply_item_amount, supply_item_type, supply_id) values(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, item.name)
        statement.setInt(2, item.amount)
        statement.setString(3, item.type.toString())
        if (item.supply != null) {
            statement.setLong(4, item.supply!!.id!!)
        } else {
            statement.setNull(4, BIGINT)
        }
        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createSupplyItems(toCreate: Iterable<SupplyItem>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into supply_items (supply_item_name, supply_item_amount, supply_item_type, supply_id) values(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (item in toCreate) {
            statement.setString(1, item.name)
            statement.setInt(2, item.amount)
            statement.setString(3, item.type.toString())
            if (item.supply != null) {
                statement.setLong(4, item.supply!!.id!!)
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

    fun findSupplyItem(id: Long): SupplyItem? {
        val statement = dataSource.connection.prepareStatement(
                "select supply_item_id, supply_item_name, supply_item_amount, supply_item_type, supply_id from supply_items where supply_item_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        val supplyDao = SupplyDao(dataSource)

        if (resultSet.next()) {
            val itemType = when (resultSet.getString("supply_item_type")) {
                "FILM" -> ItemType.FILM
                "INK" -> ItemType.INK
                "PAPER" -> ItemType.PAPER
                else -> null
            }

            return SupplyItem(
                    id = resultSet.getLong("supply_item_id"),
                    name = resultSet.getString("supply_item_name"),
                    amount = resultSet.getInt("supply_item_amount"),
                    type = itemType,
                    supply = supplyDao.findSupply(resultSet.getLong("supply_id"))
            )
        } else {
            return null
        }
    }

    fun updateSupplyItem(item: SupplyItem) {
        val statement = dataSource.connection.prepareStatement(
                "update supply_items set supply_item_name = ?, supply_item_amount = ?, supply_item_type = ?, supply_id = ? where supply_item_id = ?"
        )

        statement.setString(1, item.name)
        statement.setInt(2, item.amount)
        statement.setString(3, item.type.toString())
        if (item.supply != null) {
            statement.setLong(4, item.supply!!.id!!)
        } else {
            statement.setNull(4, BIGINT)
        }
        statement.setLong(5, item.id!!)

        statement.executeUpdate()
    }

    fun deleteSupplyItem(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from supply_items where supply_item_id = ?"
        )

        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun gelAll(): List<SupplyItem> {
        val statement = dataSource.connection.prepareStatement(
                "select * from supply_items"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<SupplyItem>()

        val supplyDao = SupplyDao(dataSource)

        while (resultSet.next()) {
            val itemType = when (resultSet.getString("supply_item_type")) {
                "FILM" -> ItemType.FILM
                "INK" -> ItemType.INK
                "PAPER" -> ItemType.PAPER
                else -> null
            }

            res += SupplyItem(
                    id = resultSet.getLong("supply_item_id"),
                    name = resultSet.getString("supply_item_name"),
                    amount = resultSet.getInt("supply_item_amount"),
                    type = itemType,
                    supply = supplyDao.findSupply(resultSet.getLong("supply_id"))
            )
        }
        return res
    }
}