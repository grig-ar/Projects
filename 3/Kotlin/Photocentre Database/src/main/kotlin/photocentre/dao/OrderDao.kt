package photocentre.dao

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Customer
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.Order
import photocentre.enums.OrderType
import java.sql.Date
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class OrderDao(private val dataSource: DataSource) {

    fun createOrder(order: Order): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into orders (order_urgent, order_cost, order_date, order_completion_date, order_type, branch_office_id, kiosk_id, customer_id) values (?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setBoolean(1, order.urgent)
        statement.setFloat(2, order.cost)
        statement.setDate(3, order.date)
        statement.setDate(4, order.completionDate)
        statement.setString(5, order.type.toString())
        if (order.branchOffice != null) {
            statement.setLong(6, order.branchOffice.id!!)
        } else {
            statement.setNull(6, BIGINT)
        }
        if (order.kiosk != null) {
            statement.setLong(7, order.kiosk.id!!)
        } else {
            statement.setNull(7, BIGINT)
        }
        if (order.customer != null) {
            statement.setLong(8, order.customer.id!!)
        } else {
            statement.setNull(8, BIGINT)
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createOrders(toCreate: Iterable<Order>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into orders (order_urgent, order_cost, order_date, order_completion_date, order_type, branch_office_id, kiosk_id, customer_id) values (?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (order in toCreate) {
            statement.setBoolean(1, order.urgent)
            statement.setFloat(2, order.cost)
            statement.setDate(3, order.date)
            statement.setDate(4, order.completionDate)
            statement.setString(5, order.type.toString())
            if (order.branchOffice != null) {
                statement.setLong(6, order.branchOffice.id!!)
            } else {
                statement.setNull(6, BIGINT)
            }
            if (order.kiosk != null) {
                statement.setLong(7, order.kiosk.id!!)
            } else {
                statement.setNull(7, BIGINT)
            }
            if (order.customer != null) {
                statement.setLong(8, order.customer.id!!)
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

    fun findOrder(id: Long): Order? {
        val statement = dataSource.connection.prepareStatement(
                "select order_id, order_urgent, order_cost, order_date, order_completion_date, order_type, branch_office_id, kiosk_id, customer_id from orders where order_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()

        val branchOfficeDao = BranchOfficeDao(dataSource)
        val kioskDao = KioskDao(dataSource)
        val customerDao = CustomerDao(dataSource)

        if (resultSet.next()) {

            val type = when (resultSet.getString("order_type")) {
                "PRINT" -> OrderType.PRINT
                "PROCESSING" -> OrderType.PROCESSING
                "BOTH" -> OrderType.BOTH
                else -> null
            }

            return Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("order_urgent"),
                    cost = resultSet.getFloat("order_cost"),
                    date = resultSet.getDate("order_date"),
                    completionDate = resultSet.getDate("order_completion_date"),
                    type = type,
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id")),
                    kiosk = kioskDao.findKiosk(resultSet.getLong("kiosk_id")),
                    customer = customerDao.findCustomer(resultSet.getLong("customer_id"))
            )
        } else {
            return null
        }
    }

    fun updateOrder(order: Order) {
        val statement = dataSource.connection.prepareStatement(
                "update orders set order_urgent = ?, order_cost = ?, order_date = ?, order_completion_date = ?, order_type = ?, branch_office_id = ?, kiosk_id = ?, customer_id = ? where order_id = ?"
        )

        statement.setBoolean(1, order.urgent)
        statement.setFloat(2, order.cost)
        statement.setDate(3, order.date)
        statement.setDate(4, order.completionDate)
        statement.setString(5, order.type.toString())
        if (order.branchOffice != null) {
            statement.setLong(6, order.branchOffice.id!!)
        } else {
            statement.setNull(6, BIGINT)
        }
        if (order.kiosk != null) {
            statement.setLong(7, order.kiosk.id!!)
        } else {
            statement.setNull(7, BIGINT)
        }
        if (order.customer != null) {
            statement.setLong(8, order.customer.id!!)
        } else {
            statement.setNull(8, BIGINT)
        }
        statement.setLong(9, order.id!!)

        statement.executeUpdate()
    }

    fun deleteOrder(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from orders where order_id = ?"
        )
        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun getByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): List<Order> {

        val statement = dataSource.connection.prepareStatement(
                "select orders.branch_office_id as office_id, " +
                        "orders.order_id as order_id, " +
                        "branch_offices.branch_office_address as address, " +
                        "orders.order_cost as cost, " +
                        "orders.order_date as order_date, " +
                        "customers.customer_name as name, " +
                        "orders.order_urgent as urgent, " +
                        "orders.order_type as type " +
                        "from orders " +
                        "left join branch_offices " +
                        "on orders.branch_office_id = branch_offices.branch_office_id " +
                        "join customers " +
                        "on orders.customer_id = customers.customer_id " +
                        "where orders.branch_office_id = ? and " +
                        "order_date between ? and ? " +
                        "order by orders.branch_office_id, orders.order_id, orders.order_cost, orders.order_urgent"
        )

        statement.setLong(1, branchOffice.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Order>()

        while (resultSet.next()) {

            val type = when (resultSet.getString("type")) {
                "PRINT" -> OrderType.PRINT
                "PROCESSING" -> OrderType.PROCESSING
                "BOTH" -> OrderType.BOTH
                else -> null
            }

            res += Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("urgent"),
                    cost = resultSet.getFloat("cost"),
                    date = resultSet.getDate("order_date"),
                    type = type,
                    branchOffice = BranchOffice(
                            id = resultSet.getLong("office_id"),
                            address = resultSet.getString("address")
                    ),
                    customer = Customer(
                            name = resultSet.getString("name")
                    )
            )
        }
        return res
    }

    fun getByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): List<Order> {

        val statement = dataSource.connection.prepareStatement(
                "select orders.kiosk_id as kiosk_id, " +
                        "orders.order_id as order_id, " +
                        "kiosk.kiosk_address as address, " +
                        "orders.order_cost as cost, " +
                        "orders.order_date as order_date, " +
                        "customers.customer_name as name, " +
                        "orders.order_urgent as urgent, " +
                        "orders.order_type as type " +
                        "from orders " +
                        "left join kiosks " +
                        "on orders.kiosk_id = kiosks.kiosk_id " +
                        "join customers " +
                        "on orders.customer_id = customers.customer_id " +
                        "where orders.kiosk_id = ? and " +
                        "order_date between ? and ? " +
                        "order by orders.kiosk_id, orders.order_id, orders.order_cost, orders.order_urgent"
        )

        statement.setLong(1, kiosk.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Order>()

        while (resultSet.next()) {

            val type = when (resultSet.getString("type")) {
                "PRINT" -> OrderType.PRINT
                "PROCESSING" -> OrderType.PROCESSING
                "BOTH" -> OrderType.BOTH
                else -> null
            }

            res += Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("urgent"),
                    cost = resultSet.getFloat("cost"),
                    date = resultSet.getDate("order_date"),
                    type = type,
                    kiosk = Kiosk(
                            id = resultSet.getLong("kiosk_id"),
                            address = resultSet.getString("address")
                    ),
                    customer = Customer(
                            name = resultSet.getString("name")
                    )
            )
        }
        return res
    }

    fun countbyBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(branch_office_id) as total_amount " +
                        "from orders " +
                        "where orders.branch_office_id = ? and " +
                        "order_date between ? and ? " +
                        "group by branch_office_id"
        )

        statement.setLong(1, branchOffice.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getInt("total_amount")
        } else {
            null
        }
    }

    fun countbyKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(kiosk_id) as total_amount " +
                        "from orders " +
                        "where orders.kiosk_id = ? and " +
                        "order_date between ? and ? " +
                        "group by kiosk_id"
        )

        statement.setLong(1, kiosk.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getInt("total_amount")
        } else {
            null
        }
    }

    fun getByDate(dateBegin: Date, dateEnd: Date): List<Order> {

        val statement = dataSource.connection.prepareStatement(
                "select order_id, branch_offices.branch_office_address as address, order_cost as cost, order_date, order_type, customers.customer_name as name, order_urgent as urgent " +
                        "from orders " +
                        "left join branch_offices " +
                        "on orders.branch_office_id = branch_offices.branch_office_id " +
                        "join customers " +
                        "on orders.customer_id = customers.customer_id " +
                        "where order_date between ? and ? " +
                        "order by order_id"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Order>()

        while (resultSet.next()) {

            val type = when (resultSet.getString("type")) {
                "PRINT" -> OrderType.PRINT
                "PROCESSING" -> OrderType.PROCESSING
                "BOTH" -> OrderType.BOTH
                else -> null
            }

            res += Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("urgent"),
                    cost = resultSet.getFloat("cost"),
                    date = resultSet.getDate("order_date"),
                    type = type,
                    branchOffice = BranchOffice(
                            address = resultSet.getString("address")
                    ),
                    customer = Customer(
                            name = resultSet.getString("name")
                    )
            )
        }
        return res
    }

    fun getCommonByBranchOfficeAndType(branchOffice: BranchOffice, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        val statement = dataSource.connection.prepareStatement(
                "select order_id, branch_offices.branch_office_address as address, order_cost as cost, order_date, customers.customer_name as name, order_urgent as urgent " +
                        "from orders " +
                        "left join branch_offices " +
                        "on orders.branch_office_id = branch_offices.branch_office_id " +
                        "join customers " +
                        "on orders.customer_id = customers.customer_id " +
                        "where order_type in (?)" +
                        "and order_date between ? and ? " +
                        "and orders.branch_office_id = ? " +
                        "and orders.urgent = false " +
                        "order by order_id"
        )

        statement.setString(1, type.toString())
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)
        statement.setLong(4, branchOffice.id!!)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Order>()

        while (resultSet.next()) {
            res += Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("urgent"),
                    cost = resultSet.getFloat("cost"),
                    date = resultSet.getDate("order_date"),
                    type = type,
                    branchOffice = BranchOffice(
                            id = branchOffice.id,
                            address = resultSet.getString("address")
                    ),
                    customer = Customer(
                            name = resultSet.getString("name")
                    )
            )
        }
        return res
    }

    fun getUrgentByBranchOfficeAndType(branchOffice: BranchOffice, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        val statement = dataSource.connection.prepareStatement(
                "select order_id, branch_offices.branch_office_address as address, order_cost as cost, order_date, customers.customer_name as name, order_urgent as urgent " +
                        "from orders " +
                        "left join branch_offices " +
                        "on orders.branch_office_id = branch_offices.branch_office_id " +
                        "join customers " +
                        "on orders.customer_id = customers.customer_id " +
                        "where order_type in (?)" +
                        "and order_date between ? and ? " +
                        "and orders.branch_office_id = ? " +
                        "and orders.urgent = true " +
                        "order by order_id"
        )

        statement.setString(1, type.toString())
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)
        statement.setLong(4, branchOffice.id!!)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Order>()

        while (resultSet.next()) {
            res += Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("urgent"),
                    cost = resultSet.getFloat("cost"),
                    date = resultSet.getDate("order_date"),
                    type = type,
                    branchOffice = BranchOffice(
                            id = branchOffice.id,
                            address = resultSet.getString("address")
                    ),
                    customer = Customer(
                            name = resultSet.getString("name")
                    )
            )
        }
        return res
    }

    fun getCommonByKioskAndType(kiosk: Kiosk, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        val statement = dataSource.connection.prepareStatement(
                "select order_id, kiosks.kiosk_address as address, order_cost as cost, order_date, customers.customer_name as name, order_urgent as urgent " +
                        "from orders " +
                        "left join kiosks " +
                        "on orders.kiosk_id = kiosks.kiosk_id " +
                        "join customers " +
                        "on orders.customer_id = customers.customer_id " +
                        "where order_type in (?)" +
                        "and order_date between ? and ? " +
                        "and orders.kiosk_id = ? " +
                        "and orders.urgent = false " +
                        "order by order_id"
        )

        statement.setString(1, type.toString())
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)
        statement.setLong(4, kiosk.id!!)

        val resultSet = statement.executeQuery()
        val res = ArrayList<Order>()

        while (resultSet.next()) {
            res += Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("urgent"),
                    cost = resultSet.getFloat("cost"),
                    date = resultSet.getDate("order_date"),
                    type = type,
                    kiosk = Kiosk(
                            id = kiosk.id,
                            address = resultSet.getString("address")
                    ),
                    customer = Customer(
                            name = resultSet.getString("name")
                    )
            )
        }
        return res
    }

    fun getRevenueByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Pair<Float?, Float?>? {
        val statement = dataSource.connection.prepareStatement(
                "select sum(case when order_urgent then 0 else order_cost end) as common_sum, " +
                        "sum(case when order_urgent then 0 else order_cost end) as urgent_sum " +
                        "from orders " +
                        "where branch_office_id = ? " +
                        "and order_date between ? and ?"
        )

        statement.setLong(1, branchOffice.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
            Pair<Float?, Float?>(resultSet.getFloat("common_sum"), resultSet.getFloat("urgent_sum"))
        } else {
            null
        }
    }

    fun getRevenueByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Float? {
        val statement = dataSource.connection.prepareStatement(
                "select sum(order_cost) as common_sum " +
                        "from orders " +
                        "where kiosk_id = ? " +
                        "and order_date between ? and ?"
        )

        statement.setLong(1, kiosk.id!!)
        statement.setDate(2, dateBegin)
        statement.setDate(3, dateEnd)

        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
            resultSet.getFloat("common_sum")
        } else {
            null
        }
    }

    fun getRevenueByDate(dateBegin: Date, dateEnd: Date): Pair<Float?, Float?>? {
        val statement = dataSource.connection.prepareStatement(
                "select sum(case when order_urgency = 'Common' then order_cost else 0 end) as common_sum, " +
                        "sum(case when order_urgency = 'Urgent' then order_cost else 0 end) as urgent_sum " +
                        "from orders " +
                        "where order_date between ? and ?"
        )

        statement.setDate(1, dateBegin)
        statement.setDate(2, dateEnd)

        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
            Pair<Float?, Float?>(resultSet.getFloat("common_sum"), resultSet.getFloat("urgent_sum"))
        } else {
            null
        }
    }

    fun gelAll(): List<Order> {
        val statement = dataSource.connection.prepareStatement(
                "select * from orders"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Order>()

        val branchOfficeDao = BranchOfficeDao(dataSource)
        val kioskDao = KioskDao(dataSource)
        val customerDao = CustomerDao(dataSource)

        while (resultSet.next()) {
            val type = when (resultSet.getString("order_type")) {
                "PRINT" -> OrderType.PRINT
                "PROCESSING" -> OrderType.PROCESSING
                "BOTH" -> OrderType.BOTH
                else -> null
            }

            res += Order(
                    id = resultSet.getLong("order_id"),
                    urgent = resultSet.getBoolean("order_urgent"),
                    cost = resultSet.getFloat("order_cost"),
                    date = resultSet.getDate("order_date"),
                    completionDate = resultSet.getDate("order_completion_date"),
                    type = type,
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id")),
                    kiosk = kioskDao.findKiosk(resultSet.getLong("kiosk_id")),
                    customer = customerDao.findCustomer(resultSet.getLong("customer_id"))
            )
        }
        return res
    }

}