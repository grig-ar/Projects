package photocentre.view.kiosk

import photocentre.controllers.KioskController
import photocentre.dao.KioskDao
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.KioskModel
import photocentre.executors.KioskExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class KioskDetailsFragment (photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val kioskModel: KioskModel by inject()
    val kioskDao = KioskDao(photocentreDataSource)
    val kioskController = KioskController(KioskExecutor(photocentreDataSource, kioskDao))
    var currentID: Long = 0
    var currentAddress: String = ""
    var currentAmount: Int = 0
    var currentBranchOfficeAddress: String = ""
    override val savable = kioskModel.dirty
    override val root = vbox {
        label("Address")
        textfield(kioskModel.address)
        label("Amount of workers")
        textfield(kioskModel.amountOfWorkers)
        label("Branch office address")

        combobox(kioskModel.branchOfficeAddress, kioskController.getAllBranchOfficeAddresses())

        buttonbar {
            button("Save") {
                enableWhen(kioskModel.dirty)
                action {
                    currentID = kioskModel.id.value
                    currentAddress = kioskModel.address.value
                    currentAmount = kioskModel.amountOfWorkers.value
                    currentBranchOfficeAddress = kioskModel.branchOfficeAddress.value
                    kioskController.updateKiosk(Kiosk(currentID, currentAddress, currentAmount, kioskModel.branchOffice.value))
                    kioskModel.kiosks.set(kioskController.getAllKiosks().asObservable())
                }
            }
            button("Undo") {
                enableWhen(kioskModel.dirty)
                action {
                    kioskModel.rollback()
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
