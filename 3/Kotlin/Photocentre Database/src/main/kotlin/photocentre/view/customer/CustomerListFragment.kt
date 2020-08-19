package photocentre.view.customer

import tornadofx.*
import photocentre.controllers.CustomerController
import photocentre.executors.CustomerExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.dao.CustomerDao
import photocentre.dataClasses.Customer
import photocentre.dataClasses.CustomerModel

class CustomerListFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val customerDao = CustomerDao(photocentreDataSource)
    val customerCtrl = CustomerController(CustomerExecutor(photocentreDataSource, customerDao))
    val customerModel: CustomerModel by inject()

    init {
        customerModel.customers.set(customerCtrl.getAllCustomers().asObservable())
    }

    override val root = borderpane {
        center = tableview(customerModel.customers) {
            column("ID", Customer::idProperty)
            column("Name", Customer::nameProperty)
            column("Discount", Customer::discountProperty)
            column("Experience", Customer::experienceProperty)
            column("Branch Office", Customer::branchOfficeAddressProperty)
            bindSelected(customerModel)
            contextmenu {
                item("Delete").action {
                    customerCtrl.deleteCustomer(selectedItem?.id!!)
                    customerModel.customers.set(customerCtrl.getAllCustomers().asObservable())
                }
            }
            columnResizePolicy = SmartResize.POLICY
        }
    }
}