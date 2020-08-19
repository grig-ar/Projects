package photocentre.executors

import photocentre.dao.CustomerDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Customer
import photocentre.main.PhotocentreDataSource

class CustomerExecutor(
        private val dataSource: PhotocentreDataSource,
        private val customerDao: CustomerDao
) {
    fun createCustomer(customer: Customer): Long {
        return transaction(dataSource) {
            customerDao.createCustomer(customer)
        }
    }

    fun createCustomers(customers: Iterable<Customer>): List<Long> {
        return transaction(dataSource) {
            customerDao.createCustomers(customers)
        }
    }

    fun findCustomer(id: Long): Customer? {
        return transaction(dataSource) {
            customerDao.findCustomer(id)
        }
    }

    fun updateCustomer(customer: Customer) {
        return transaction(dataSource) {
            customerDao.updateCustomer(customer)
        }
    }

    fun deleteCustomer(id: Long) {
        return transaction(dataSource) {
            customerDao.deleteCustomer(id)
        }
    }

    fun getCustomersByOffice(branchOffice: BranchOffice): List<Customer> {
        return transaction(dataSource) {
            customerDao.getByOffice(branchOffice)
        }
    }

    fun getIfDiscount(): List<Customer> {
        return transaction(dataSource) {
            customerDao.getIfDiscount()
        }
    }

    fun getByAmount(photoAmount: Int): List<Pair<Customer, Int>> {
        return transaction(dataSource) {
            customerDao.getByAmount(photoAmount)
        }
    }

    fun getAllCustomers(): List<Customer> {
        return transaction(dataSource) {
            customerDao.gelAll()
        }
    }
}