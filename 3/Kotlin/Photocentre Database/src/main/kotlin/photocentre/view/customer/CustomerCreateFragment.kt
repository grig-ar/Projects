package photocentre.view.customer

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import photocentre.controllers.BranchOfficeController
import photocentre.controllers.CustomerController
import photocentre.dao.BranchOfficeDao
import photocentre.dao.CustomerDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.BranchOfficeModel
import photocentre.dataClasses.Customer
import photocentre.dataClasses.CustomerModel
import photocentre.executors.BranchOfficeExecutor
import photocentre.executors.CustomerExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.view.branchoffice.BranchOfficeCreateFragment
import tornadofx.*
import kotlin.math.max
import kotlin.math.min

class CustomerCreateFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val fragment = this
    val customerModel: CustomerModel by inject()
    val branchOfficeModel: BranchOfficeModel by inject()
    val customerDao = CustomerDao(photocentreDataSource)
    val customerController = CustomerController(CustomerExecutor(photocentreDataSource, customerDao))
    val branchOfficeController = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, BranchOfficeDao(photocentreDataSource)))
    var currentNameText: TextField by singleAssign()
    var currentDiscount: Int by singleAssign()
    var currentExperience: Int by singleAssign()
    var currentBranchOffice: BranchOffice by singleAssign()
    var currentOfficeAddress: ComboBox<String> by singleAssign()
    var finalList = FXCollections.observableArrayList<String>()

    override fun onRefresh() {
        val officeList = branchOfficeController.getBranchOffices()
        val addressList = ArrayList<String>()

        for (office in officeList) {
            addressList += office.address
        }

        finalList = FXCollections.observableArrayList(addressList)
    }

    override val root = form {
        fieldset("Create Customer") {
            field("Name") {
                currentNameText = textfield()
            }
            field("Discount") {
                val currentDiscountText = textfield()
                val discount = try {
                    currentDiscountText.text.toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                currentDiscount = max(0, min(discount, 100))
            }
            field("Experience") {
                val currentExperienceText = textfield()
                val experience = try {
                    currentExperienceText.text.toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                currentExperience = max(0, min(experience, 100))
            }

            field("Branch Office") {
                val officeList = branchOfficeController.getBranchOffices()
                val addressList = ArrayList<String>()

                for (office in officeList) {
                    addressList += office.address
                }

                finalList = FXCollections.observableArrayList(addressList)

                currentOfficeAddress = combobox {
                    items = finalList.asObservable()
                }
            }


        }
        buttonbar {
            button("Save") {
                action {
                    var index = 0
                    for (i in 1..branchOfficeModel.branchOffices.get().size) {
                        if (branchOfficeModel.branchOffices.get()[i].address == currentOfficeAddress.value)
                            index = i
                    }
                    currentBranchOffice = branchOfficeModel.branchOffices.get()[index]

                    customerModel.customers.get().add(customerController.createCustomer(Customer(
                            name = currentNameText.text,
                            discount = currentDiscount,
                            experience = currentExperience,
                            branchOffice = currentBranchOffice
                    )))
                    currentNameText.text = ""
                    currentDiscount = 0
                    currentExperience = 0
                }
            }
            button("Create New Branch Office...") {
                action {
                    action {
                        val branchOfficeCreateFragment = BranchOfficeCreateFragment(photocentreDataSource, fragment)
                        replaceWith(branchOfficeCreateFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                    }
                }
            }
        }
        button("Back") {
            action {
                val customerCRUDFragment = CustomerCRUDFragment(photocentreDataSource)
                replaceWith(customerCRUDFragment)
            }
        }
    }
}