package photocentre.dao

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Customer
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class CustomerDao(private val dataSource: DataSource) {

    fun createCustomer(customer: Customer): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into customers (customer_name, customer_discount, customer_experience, branch_office_id) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, customer.name)
        statement.setInt(2, customer.discount)
        statement.setInt(3, customer.experience)
        if (customer.branchOffice != null) {
            statement.setLong(4, customer.branchOffice!!.id)
        } else {
            statement.setNull(4, BIGINT)
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createCustomers(toCreate: Iterable<Customer>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "insert into customers (customer_name, customer_discount, customer_experience, branch_office_id) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (customer in toCreate) {
            statement.setString(1, customer.name)
            statement.setInt(2, customer.discount)
            statement.setInt(3, customer.experience)
            if (customer.branchOffice != null) {
                statement.setLong(4, customer.branchOffice!!.id)
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

    fun findCustomer(id: Long?): Customer? {
        val statement = dataSource.connection.prepareStatement(
                "select customer_id, customer_name, customer_discount, customer_experience, branch_office_id from customers where customer_id = ?"
        )
        if (id != null) {
            statement.setLong(1, id)
        } else {
            return null
        }
        val resultSet = statement.executeQuery()
        val branchOfficeDao = BranchOfficeDao(dataSource)

        return if (resultSet.next()) {
            Customer(
                    id = resultSet.getLong("customer_id"),
                    name = resultSet.getString("customer_name"),
                    discount = resultSet.getInt("customer_discount"),
                    experience = resultSet.getInt("customer_experience"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        } else {
            null
        }
    }

    fun updateCustomer(customer: Customer) {
        val statement = dataSource.connection.prepareStatement(
                "update customers set customer_name = ?, customer_discount = ?, customer_experience = ?, branch_office_id = ? where customer_id = ?"
        )
        statement.setString(1, customer.name)
        statement.setInt(2, customer.discount)
        statement.setInt(3, customer.experience)
        if (customer.branchOffice != null) {
            statement.setLong(4, customer.branchOffice!!.id)
        } else {
            statement.setNull(4, BIGINT)
        }
        statement.setLong(5, customer.id)

        statement.executeUpdate()
    }

    fun deleteCustomer(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "delete from customers where customer_id = ?"
        )
        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun getByOffice(branchOffice: BranchOffice): List<Customer> {
        val statement = dataSource.connection.prepareStatement(
                "select customer_id, customer_name, customer_discount, " +
                        "branch_offices.branch_office_address as address " +
                        "from customers " +
                        "join branch_offices " +
                        "on branch_office.branch_office_id = customers.branch_office_id " +
                        "where customers.branch_office_id = ?"
        )
        statement.setLong(1, branchOffice.id)
        val resultSet = statement.executeQuery()
        val res = ArrayList<Customer>()

        while (resultSet.next()) {
            res += Customer(
                    id = resultSet.getLong("customer_id"),
                    name = resultSet.getString("customer_name"),
                    discount = resultSet.getInt("customer_discount"),
                    branchOffice = BranchOffice(
                            id = resultSet.getLong("office_id"),
                            address = resultSet.getString("address")
                    )
            )
        }
        return res
    }

    fun getIfDiscount(): List<Customer> {
        val statement = dataSource.connection.prepareStatement(
                "select customer_id, customer_name, customer_discount from customers where customer_discount > 0 order by customer_discount desc"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Customer>()

        while (resultSet.next()) {
            res += Customer(
                    id = resultSet.getLong("customer_id"),
                    name = resultSet.getString("customer_name"),
                    discount = resultSet.getInt("customer_discount")
            )
        }
        return res
    }

    fun getByAmount(amount: Int): List<Pair<Customer, Int>> {
        val statement = dataSource.connection.prepareStatement(
                "select customers.customer_name, count(photo_id) as photo_amount " +
                        "from orders " +
                        "left join customers " +
                        "on orders.customer_id = customers.customer_id " +
                        "join films " +
                        "on films.order_id = orders.order_id " +
                        "left join photos " +
                        "on photos.film_id = films.film_id " +
                        "group by customers.customer_name " +
                        "having count(photo_id) >= ? " +
                        "order by photo_amount desc"
        )
        statement.setInt(1, amount)
        val resultSet = statement.executeQuery()
        val res = ArrayList<Pair<Customer, Int>>()

        while (resultSet.next()) {
            res += Pair(
                    Customer(
                            name = resultSet.getString("customer_name")
                    ),
                    resultSet.getInt("photo_amount")
            )
        }
        return res
    }

    fun gelAll(): List<Customer> {
        val statement = dataSource.connection.prepareStatement(
                "select * from customers"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Customer>()

        val branchOfficeDao = BranchOfficeDao(dataSource)

        while (resultSet.next()) {
            res += Customer(
                    id = resultSet.getLong("customer_id"),
                    name = resultSet.getString("customer_name"),
                    discount = resultSet.getInt("customer_discount"),
                    experience = resultSet.getInt("customer_experience"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        }
        return res
    }
}