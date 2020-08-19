package photocentre.dao

import photocentre.dataClasses.OfficeItem
import photocentre.enums.ItemType
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class OfficeItemDao(private val dataSource: DataSource) {

    fun createOfficeItem(officeItem: OfficeItem): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into office_items (" +
                        "office_item_for_sale, " +
                        "office_item_amount, " +
                        "office_item_recommended, " +
                        "office_item_critical, " +
                        "office_item_name, office_item_cost, " +
                        "office_item_type, " +
                        "branch_office_id" +
                        ") " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setBoolean(1, officeItem.forSale)
        statement.setInt(2, officeItem.amount)
        statement.setInt(3, officeItem.recommendedAmount)
        statement.setInt(4, officeItem.criticalAmount)
        statement.setString(5, officeItem.name)
        statement.setFloat(6, officeItem.cost)
        statement.setString(7, officeItem.type.toString())
        if (officeItem.branchOffice != null) {
            statement.setLong(8, officeItem.branchOffice.id!!)
        } else {
            statement.setNull(8, BIGINT)
        }
        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createOfficeItems(toCreate: Iterable<OfficeItem>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into office_items (" +
                        "office_item_for_sale, " +
                        "office_item_amount, " +
                        "office_item_recommended, " +
                        "office_item_critical, " +
                        "office_item_name, " +
                        "office_item_cost, " +
                        "office_item_type, " +
                        "branch_office_id" +
                        ") " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)

        for (officeItem in toCreate) {
            statement.setBoolean(1, officeItem.forSale)
            statement.setInt(2, officeItem.amount)
            statement.setInt(3, officeItem.recommendedAmount)
            statement.setInt(4, officeItem.criticalAmount)
            statement.setString(5, officeItem.name)
            statement.setFloat(6, officeItem.cost)
            statement.setString(7, officeItem.type.toString())
            if (officeItem.branchOffice != null) {
                statement.setLong(8, officeItem.branchOffice.id!!)
            } else {
                statement.setNull(8, BIGINT)
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

    fun findOfficeItem(id: Long): OfficeItem? {
        val statement = dataSource.connection.prepareStatement(
                "select office_item_id, " +
                        "office_item_for_sale, " +
                        "office_item_amount, " +
                        "office_item_recommended, " +
                        "office_item_critical, " +
                        "office_item_name, " +
                        "office_item_cost, " +
                        "office_item_type, " +
                        "branch_office_id" +
                        "FROM office_items " +
                        "WHERE office_item_id = ?"
        )

        statement.setLong(1, id)

        val resultSet = statement.executeQuery()
        val branchOfficeDao = BranchOfficeDao(dataSource)

        if (resultSet.next()) {
            val type = when (resultSet.getString("office_item_type")) {
                "PAPER" -> ItemType.PAPER
                "INK" -> ItemType.INK
                "FILM" -> ItemType.FILM
                else -> null
            }

            return OfficeItem(
                    id = resultSet.getLong("office_item_id"),
                    forSale = resultSet.getBoolean("office_item_for_sale"),
                    amount = resultSet.getInt("office_item_amount"),
                    recommendedAmount = resultSet.getInt("office_item_recommended"),
                    criticalAmount = resultSet.getInt("office_item_critical"),
                    name = resultSet.getString("office_item_name"),
                    cost = resultSet.getFloat("office_item_cost"),
                    type = type,
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        } else {
            return null
        }
    }

    fun updateOfficeItem(officeItem: OfficeItem) {
        val statement = dataSource.connection.prepareStatement(
                "update office_items " +
                        "set office_item_for_sale = ?, " +
                        "office_item_amount = ?, " +
                        "office_item_recommended = ?, " +
                        "office_item_critical = ?, " +
                        "office_item_name = ?, " +
                        "office_item_cost = ?, " +
                        "office_item_type = ?, " +
                        "branch_office_id = ? " +
                        "where office_item_id = ?"
        )

        statement.setBoolean(1, officeItem.forSale)
        statement.setInt(2, officeItem.amount)
        statement.setInt(3, officeItem.recommendedAmount)
        statement.setInt(4, officeItem.criticalAmount)
        statement.setString(5, officeItem.name)
        statement.setFloat(6, officeItem.cost)
        statement.setString(7, officeItem.type.toString())
        if (officeItem.branchOffice != null) {
            statement.setLong(8, officeItem.branchOffice.id!!)
        } else {
            statement.setNull(8, BIGINT)
        }
        statement.setLong(9, officeItem.id!!)

        statement.executeUpdate()
    }

    fun deleteOfficeItem(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from office_items where office_item_id = ?"
        )
        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun gelAll(): List<OfficeItem> {
        val statement = dataSource.connection.prepareStatement(
                "select * from office_items"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<OfficeItem>()
        val branchOfficeDao = BranchOfficeDao(dataSource)

        while (resultSet.next()) {
            val type = when (resultSet.getString("office_item_type")) {
                "PAPER" -> ItemType.PAPER
                "INK" -> ItemType.INK
                "FILM" -> ItemType.FILM
                else -> null
            }

            res += OfficeItem(
                    id = resultSet.getLong("office_item_id"),
                    forSale = resultSet.getBoolean("office_item_for_sale"),
                    amount = resultSet.getInt("office_item_amount"),
                    recommendedAmount = resultSet.getInt("office_item_recommended"),
                    criticalAmount = resultSet.getInt("office_item_critical"),
                    name = resultSet.getString("office_item_name"),
                    cost = resultSet.getFloat("office_item_cost"),
                    type = type,
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        }
        return res
    }
}