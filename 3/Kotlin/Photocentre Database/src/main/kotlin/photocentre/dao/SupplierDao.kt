package photocentre.dao

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Supplier
import photocentre.enums.ItemType
import java.sql.Date
import java.sql.Statement
import javax.sql.DataSource

class SupplierDao(private val dataSource: DataSource) {

    fun createSupplier(supplier: Supplier): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into suppliers (supplier_name, supplier_specialization) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, supplier.name)
        statement.setString(2, supplier.specialization.toString())

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createSuppliers(toCreate: Iterable<Supplier>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into suppliers (supplier_name, supplier_specialization) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (supplier in toCreate) {
            statement.setString(1, supplier.name)
            statement.setString(2, supplier.specialization.toString())
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

    fun findSupplier(id: Long): Supplier? {
        val statement = dataSource.connection.prepareStatement(
                "select supplier_id, supplier_name, supplier_specialization from suppliers where supplier_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {

            val specialization = when (resultSet.getString("supplier_specialization")) {
                "FILM" -> ItemType.FILM
                "INK" -> ItemType.INK
                "PAPER" -> ItemType.PAPER
                else -> null
            }

            return Supplier(
                    id = resultSet.getLong("supplier_id"),
                    name = resultSet.getString("supplier_name"),
                    specialization = specialization
            )
        } else {
            return null
        }
    }

    fun updateSupplier(supplier: Supplier) {
        val statement = dataSource.connection.prepareStatement(
                "update suppliers set supplier_name = ?, supplier_specialization = ? where supplier_id = ?"
        )

        statement.setString(1, supplier.name)
        statement.setString(2, supplier.specialization.toString())
        statement.setLong(3, supplier.id!!)

        statement.executeUpdate()
    }

    fun deleteSupplier(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from suppliers where supplier_id = ?"
        )

        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun getAll(): List<Supplier> {
        val statement = dataSource.connection.prepareStatement(
                "select * from suppliers"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Supplier>()

        while (resultSet.next()) {

            val specialization = when (resultSet.getString("supplier_specialization")) {
                "FILM" -> ItemType.FILM
                "INK" -> ItemType.INK
                "PAPER" -> ItemType.PAPER
                else -> null
            }

            res += Supplier(
                    id = resultSet.getLong("supplier_id"),
                    name = resultSet.getString("supplier_name"),
                    specialization = specialization
            )
        }
        return res
    }

    fun getBySpecialization(specialization: ItemType): List<Supplier> {
        val statement = dataSource.connection.prepareStatement(
                "select * from suppliers where supplier_specialization in (?)"
        )

        statement.setString(1, specialization.toString())

        val resultSet = statement.executeQuery()
        val res = ArrayList<Supplier>()

        while (resultSet.next()) {

            res += Supplier(
                    id = resultSet.getLong("supplier_id"),
                    name = resultSet.getString("supplier_name"),
                    specialization = specialization
            )
        }
        return res
    }

    fun getByDate(dateBegin: Date, dateEnd: Date): List<Supplier> {
        val statement = dataSource.connection.prepareStatement(
                "select distinct supplier_name " +
                        "from " +
                        "(" +
                        "select suppliers.supplier_id, " +
                        "supplier_name, " +
                        "supplier_specialization " +
                        "from suppliers " +
                        "left join supplies " +
                        "on supplies.supplier_id = suppliers.supplier_id " +
                        "where supply_date between ? and ? " +
                        "order by supply_date" +
                        ") " +
                        "order by supplier_name"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Supplier>()

        while (resultSet.next()) {

            res += Supplier(
                    name = resultSet.getString("supplier_name")
            )
        }
        return res
    }

    fun getByAmount(supplyItemAmount: Int, dateBegin: Date, dateEnd: Date): List<Pair<Supplier, Int>> {
        val statement = dataSource.connection.prepareStatement(
                "select suppliers.supplier_id as supplier_id, " +
                        "supplier_name, " +
                        "supplier_specialization, " +
                        "supply_item_amount as total_amount " +
                        "from suppliers " +
                        "left join supplies " +
                        "on supplies.supplier_id = suppliers.supplier_id " +
                        "join supply_items " +
                        "on supply_items.supply_id = supplies.supply_id " +
                        "where supply_date between ? and ? " +
                        "and supply_item_amount > ? " +
                        "order by supply_item_amount desc, supply_date"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)
        statement.setInt(3, supplyItemAmount)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Pair<Supplier, Int>>()

        while (resultSet.next()) {

            val specialization = when (resultSet.getString("supplier_specialization")) {
                "FILM" -> ItemType.FILM
                "INK" -> ItemType.INK
                "PAPER" -> ItemType.PAPER
                else -> null
            }

            res += Pair(
                    Supplier(
                            id = resultSet.getLong("supplier_id"),
                            name = resultSet.getString("supplier_name"),
                            specialization = specialization
                    ),
                    resultSet.getInt("total_amount")
            )
        }
        return res
    }

    fun getTopSuppliersByBranchOffice(branchOffice: BranchOffice): List<Pair<Supplier, Int>> {
        val statement = dataSource.connection.prepareStatement(
                "select supplier_name, count(sold_item_name) as sum " +
                        "from sold_items " +
                        "join supply_items " +
                        "on sold_item_name = supply_item_name " +
                        "join supplies " +
                        "on supply_items.supply_id = supplies.supply_id " +
                        "join suppliers " +
                        "on supplies.supplier_id = suppliers.supplier_id " +
                        "where sold_items.branch_office_id = ? " +
                        "group by supplier_name " +
                        "order by sum desc"
        )

        statement.setLong(1, branchOffice.id!!)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Pair<Supplier, Int>>()

        while (resultSet.next()) {

            res += Pair(
                    Supplier(
                            name = resultSet.getString("supplier_name")
                    ),
                    resultSet.getInt("sum")
            )
        }
        return res
    }

    fun getTopSuppliers(): List<Pair<Supplier, Int>> {
        val statement = dataSource.connection.prepareStatement(
                "select supplier_name, count(sold_item_name) as sum " +
                        "from sold_items " +
                        "join supply_items " +
                        "on sold_item_name = supply_item_name " +
                        "join supplies " +
                        "on supply_items.supply_id = supplies.supply_id " +
                        "join suppliers " +
                        "on supplies.supplier_id = suppliers.supplier_id " +
                        "group by supplier_name " +
                        "order by sum desc"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Pair<Supplier, Int>>()

        while (resultSet.next()) {

            res += Pair(
                    Supplier(
                            name = resultSet.getString("supplier_name")
                    ),
                    resultSet.getInt("sum")
            )
        }
        return res
    }
}