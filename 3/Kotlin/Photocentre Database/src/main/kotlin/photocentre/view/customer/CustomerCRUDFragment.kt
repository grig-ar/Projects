package photocentre.view.customer

import photocentre.controllers.CustomerController
import photocentre.executors.CustomerExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.dao.CustomerDao
import photocentre.dataClasses.CustomerModel
import tornadofx.*

class CustomerCRUDFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val customerModel: CustomerModel by inject()
    val customerDao = CustomerDao(photocentreDataSource)
    val customerController = CustomerController(CustomerExecutor(photocentreDataSource, customerDao))
    override val root = borderpane {
        center = vbox(10.0) {
            paddingAll = 10
            button("Create") {
                action {
                    val customerCreateFragment = CustomerCreateFragment(photocentreDataSource)
                    replaceWith(customerCreateFragment)
                }
            }
            button("Update") {
                action {
                    val customerDetailsFragment = CustomerDetailsFragment(photocentreDataSource)
                    replaceWith(customerDetailsFragment)
                }
            }
            /*button("Get customers by office") {
                action {
                    //val selectCustomerByOffice = CustomerSelectByOfficeFragment(photocentreDataSource)
                    //replaceWith(selectCustomerByOffice)
                }
            }
            button("Get customers who have discount") {
                action {
                    //val getDiscountCustomersFragment = CustomerSelectDiscountFragment(photocentreDataSource)
                    //replaceWith(getDiscountCustomersFragment)
                }
            }
            button("Get customers by amount of photos") {
                action {
                    //val getCustomersByAmountFragment = CustomerSelectByAmountFragment(photocentreDataSource)
                    //replaceWith(getCustomersByAmountFragment)
                }
            }*/
        }
    }
}
