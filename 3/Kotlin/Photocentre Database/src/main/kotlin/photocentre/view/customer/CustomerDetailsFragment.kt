package photocentre.view.customer

import photocentre.controllers.CustomerController
import photocentre.executors.CustomerExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.dao.CustomerDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Customer
import photocentre.dataClasses.CustomerModel
import tornadofx.*

class CustomerDetailsFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val customerModel: CustomerModel by inject()
    val customerDao = CustomerDao(photocentreDataSource)
    val customerController = CustomerController(CustomerExecutor(photocentreDataSource, customerDao))
    var currentID: Long = 0
    var currentName: String = ""
    var currentDiscount: Int = 0
    var currentExperience: Int = 0
    var currentBranchOffice: BranchOffice? = null
    var currentAddress: String = ""

    override val savable = customerModel.dirty

    override val root = vbox {
        label("Name")
        textfield(customerModel.name)
        label("Discount")
        textfield(customerModel.discount)
        label("Experience")
        textfield(customerModel.experience)
        label("Branch Office")
        textfield(customerModel.branchOfficeAddress)

        buttonbar {
            button("Save") {
                enableWhen(customerModel.dirty)
                action {
                    currentID = customerModel.id.value
                    currentName = customerModel.name.value
                    currentDiscount = customerModel.discount.value
                    currentAddress = customerModel.branchOfficeAddress.value
                    customerController.updateCustomer(Customer(
                            id = currentID,
                            name = currentName,
                            discount = currentDiscount,
                            branchOffice = customerModel.branchOffice.value
                    ))
                    customerModel.customers.set(customerController.getAllCustomers().asObservable())
                }
            }
            button("Undo") {
                enableWhen(customerModel.dirty)
                action {
                    customerModel.rollback()
                }
            }
        }

        button("Back") {
            action {
                val customerCRUDFragment = CustomerCRUDFragment(photocentreDataSource)
                replaceWith(customerCRUDFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
            }
        }
    }
}