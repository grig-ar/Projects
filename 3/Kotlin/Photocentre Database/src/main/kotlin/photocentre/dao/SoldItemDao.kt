package photocentre.dao

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.SoldItem
import java.sql.Date
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class SoldItemDao(private val dataSource: DataSource) {

    fun createSoldItem(item: SoldItem): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into sold_items (sold_item_name, sold_item_cost, sold_item_date, branch_office_id) values(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, item.name)
        statement.setFloat(2, item.cost)
        statement.setDate(3, item.date)
        if (item.branchOffice != null) {
            statement.setLong(4, item.branchOffice.id!!)
        } else {
            statement.setNull(4, BIGINT)
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createSoldItems(toCreate: Iterable<SoldItem>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into sold_items (sold_item_name, sold_item_cost, sold_item_date, branch_office_id) values(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (item in toCreate) {
            statement.setString(1, item.name)
            statement.setFloat(2, item.cost)
            statement.setDate(3, item.date)
            if (item.branchOffice != null) {
                statement.setLong(4, item.branchOffice.id!!)
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

    fun findSoldItem(id: Long): SoldItem? {
        val statement = dataSource.connection.prepareStatement(
                "select sold_item_id, sold_item_name, sold_item_cost, sold_item_date, branch_office_id from sold_items where sold_item_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        val branchOfficeDao = BranchOfficeDao(dataSource)

        return if (resultSet.next()) {
            SoldItem(
                    id = resultSet.getLong("sold_item_id"),
                    name = resultSet.getString("sold_item_name"),
                    cost = resultSet.getFloat("sold_item_cost"),
                    date = resultSet.getDate("sold_item_date"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        } else {
            null
        }
    }

    fun updateSoldItem(item: SoldItem) {
        val statement = dataSource.connection.prepareStatement(
                "update sold_items set sold_item_name = ?, sold_item_cost = ?, sold_item_date = ?, branch_office_id = ? where sold_item_id = ?"
        )

        statement.setString(1, item.name)
        statement.setFloat(2, item.cost)
        statement.setDate(3, item.date)
        if (item.branchOffice != null) {
            statement.setLong(4, item.branchOffice.id!!)
        } else {
            statement.setNull(4, BIGINT)
        }
        statement.setLong(5, item.id!!)

        statement.executeUpdate()
    }

    fun deleteSoldItem(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from sold_items where sold_item_id = ?"
        )

        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun getRevenueByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Float? {
        val statement = dataSource.connection.prepareStatement(
                "select sum(sold_item_cost) as total_amount" +
                        "from sold_items " +
                        "where branch_office_id = ? " +
                        "and sold_item_date between ? and ?"
        )

        statement.setLong(1, branchOffice.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getFloat("total_amount")
        } else {
            null
        }
    }

    fun getRevenueByDate(dateBegin: Date, dateEnd: Date): Float? {
        val statement = dataSource.connection.prepareStatement(
                "select sum(sold_item_cost) as total_amount" +
                        "from sold_items " +
                        "where sold_item_date between ? and ?"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)

        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getFloat("total_amount")
        } else {
            null
        }
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): List<Pair<SoldItem, Int>> {
        val statement = dataSource.connection.prepareStatement(
                "select sold_item_name, count(*) as total_amount" +
                        "from sold_items " +
                        "where sold_item_date between ? and ? " +
                        "and branch_office_id = ? " +
                        "group by sold_item_name"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)
        statement.setLong(3, branchOffice.id!!)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Pair<SoldItem, Int>>()

        while (resultSet.next()) {
            res += Pair(
                    SoldItem(
                            name = resultSet.getString("sold_item_name")
                    ),
                    resultSet.getInt("total_amount")
            )
        }
        return res
    }

    fun countByDate(dateBegin: Date, dateEnd: Date): List<Pair<SoldItem, Int>> {
        val statement = dataSource.connection.prepareStatement(
                "select sold_item_name, count(*) as total_amount" +
                        "from sold_items " +
                        "where sold_item_date between ? and ? " +
                        "group by sold_item_name"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Pair<SoldItem, Int>>()

        while (resultSet.next()) {
            res += Pair(
                    SoldItem(
                            name = resultSet.getString("sold_item_name")
                    ),
                    resultSet.getInt("total_amount")
            )
        }
        return res
    }

    fun gelAll(): List<SoldItem> {
        val statement = dataSource.connection.prepareStatement(
                "select * from sold_items"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<SoldItem>()

        val branchOfficeDao = BranchOfficeDao(dataSource)

        while (resultSet.next()) {

            res += SoldItem(
                    id = resultSet.getLong("sold_item_id"),
                    name = resultSet.getString("sold_item_name"),
                    cost = resultSet.getFloat("sold_item_cost"),
                    date = resultSet.getDate("sold_item_date"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        }
        return res
    }
    
}