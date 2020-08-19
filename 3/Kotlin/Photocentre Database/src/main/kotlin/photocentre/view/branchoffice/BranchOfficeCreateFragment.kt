package photocentre.view.branchoffice

import javafx.scene.control.TextField
import photocentre.controllers.BranchOfficeController
import photocentre.executors.BranchOfficeExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.dao.BranchOfficeDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.BranchOfficeModel
import photocentre.dataClasses.Customer
import photocentre.dataClasses.CustomerModel
import tornadofx.*
import java.lang.NumberFormatException
import kotlin.math.max

class BranchOfficeCreateFragment(photocentreDataSource: PhotocentreDataSource, from: Fragment) : Fragment() {
    val branchOfficeModel: BranchOfficeModel by inject()
    val customerModel: CustomerModel by inject()
    val branchOfficeDao = BranchOfficeDao(photocentreDataSource)
    val branchOfficeController = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, branchOfficeDao))
    var currentAddressText: TextField by singleAssign()
    var currentAmountText: TextField by singleAssign()
    val amountContext = ValidationContext()


    override val root = form {
        fieldset("Create Branch Office") {

            field("Address") {
                currentAddressText = textfield()
            }
            field("Amount of workers") {
                currentAmountText = textfield()
            }
            currentAmountText.text = "0"
//            val amountValidator = amountContext.addValidator(currentAmountText, currentAmountText.textProperty()) {
//                when {
//                    it.isNullOrBlank() -> error("Required")
//                    it.matches(Regex("^\\d+\$")) -> null
//                    else -> error("Only numbers")
//                }
//            }
//            amountValidator.validate()
//            val amountResult = amountValidator.result
        }
        buttonbar {
            button("Save") {
                action {
                    branchOfficeModel.branchOffices.get().add(branchOfficeController.createBranchOffice(BranchOffice(
                            address = currentAddressText.text,
                            amountOfWorkers = currentAmountText.text.toInt()
                    )))
                    from.onRefresh()
                    replaceWith(from)
                }
            }
        }
        button("Back") {
            action {
                replaceWith(from)
            }
        }
    }
}