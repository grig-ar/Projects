package photocentre.view.kiosk

import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import photocentre.controllers.BranchOfficeController
import photocentre.controllers.KioskController
import photocentre.dao.BranchOfficeDao
import photocentre.dao.KioskDao
import photocentre.dataClasses.BranchOfficeModel
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.KioskModel
import photocentre.executors.BranchOfficeExecutor
import photocentre.executors.KioskExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class KioskCreateFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val kioskModel: KioskModel by inject()
    val branchOfficeModel: BranchOfficeModel by inject()
    val kioskDao = KioskDao(photocentreDataSource)
    val branchOfficeDao = BranchOfficeDao(photocentreDataSource)
    val kioskController = KioskController(KioskExecutor(photocentreDataSource, kioskDao))
    val branchOfficeController = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, branchOfficeDao))
    var currentAddressText: TextField by singleAssign()
    var currentAmountText: TextField by singleAssign()
    var currentOfficeAddress: ComboBox<String> by singleAssign()
    val addressContext = ValidationContext()
    val amountContext = ValidationContext()
    override val root = form {
        fieldset("Create Branch Kiosk") {

            field("Address") {
                currentAddressText = textfield()
            }
            val addressValidator = addressContext.addValidator(currentAddressText, currentAddressText.textProperty()) {
                if (it.isNullOrBlank()) error("Required") else null
            }

            addressValidator.validate()

            //val addressResult = addressValidator.result
            field("Amount of workers") {
                currentAmountText = textfield()
            }
            currentAmountText.text = "0"
            val amountValidator = amountContext.addValidator(currentAmountText, currentAmountText.textProperty()) {
                if (it.isNullOrBlank()) error("Required") else
                if (it!!.matches(Regex("^\\d+\$"))) null else error("Only numbers")
            }
            amountValidator.validate()
            //val amountResult = addressValidator.result
//            currentAmountText.validator {
//                if (it!!.length < 5) error("Too short") else null
//            }
            field("Branch office address") {
                currentOfficeAddress = combobox {
                    items = kioskController.getAllBranchOfficeAddresses().asObservable()
                }
            }
        }
        buttonbar {
            button("Save") {
                enableWhen(addressContext.valid and amountContext.valid)
                action {
                    var index = 0
                    for (i in 1..branchOfficeModel.branchOffices.get().size) {
                        if (branchOfficeModel.branchOffices.get()[i].address == currentOfficeAddress.value)
                            index = i
                    }
                    val kiosk: Kiosk = try {
                        Kiosk(
                                address = currentAddressText.text,
                                amountOfWorkers = currentAmountText.text.toInt(),
                                branchOffice = branchOfficeModel.branchOffices.get()[index]
                        )
                    } catch (e: NumberFormatException) {
                        Kiosk(
                                address = currentAddressText.text,
                                branchOffice = branchOfficeModel.branchOffices.get()[index]
                        )
                        //alert(Alert.AlertType.WARNING, "Error", "Incorrect input")
                    }

                    kioskModel.kiosks.get().add(kioskController.createKiosk(kiosk))
                    //branchOfficeModel.branchOffices.set(branchOfficeController.getBranchOffices().asObservable())
                    currentAddressText.text = ""
                    currentAmountText.text = ""
                }
            }
        }
        button("Back") {
            action {
                val kioskCRUDFragment = KioskCRUDFragment(photocentreDataSource)
                replaceWith(kioskCRUDFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
            }
        }
    }
}
