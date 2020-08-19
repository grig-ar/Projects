package photocentre.controllers

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Customer
import photocentre.executors.CustomerExecutor

class CustomerController(private val executor: CustomerExecutor) {

    fun createCustomer(customer: Customer): Customer {

        val id = executor.createCustomer(customer)
        return Customer(
                id = id,
                name = customer.name,
                discount = customer.discount,
                experience = customer.experience,
                branchOffice = customer.branchOffice
        )
    }

    fun createCustomers(customers: List<Customer>): List<Customer> {
        val ids = executor.createCustomers(customers)
        val newCustomers = ArrayList<Customer>()

        for (i in 1..ids.size) {
            newCustomers += Customer(
                    id = ids[i],
                    name = customers[i].name,
                    discount = customers[i].discount,
                    experience = customers[i].experience,
                    branchOffice = customers[i].branchOffice
            )
        }
        return newCustomers
    }

    fun getCustomer(id: Long): Customer? {
        return executor.findCustomer(id)
    }

    fun updateCustomer(customer: Customer): String {
        return executor.updateCustomer(customer).toString()
    }

    fun deleteCustomer(id: Long): String {
        return executor.deleteCustomer(id).toString()
    }

    fun getCustomersByOffice(branchOffice: BranchOffice): List<Customer> {
        return executor.getCustomersByOffice(branchOffice)
    }

    fun getIfDiscount(): List<Customer> {
        return executor.getIfDiscount()
    }

    fun getByAmount(photoAmount: Int): List<Pair<Customer, Int>> {
        return executor.getByAmount(photoAmount)
    }

    fun getAllCustomers(): List<Customer> {
        return executor.getAllCustomers()
    }
}