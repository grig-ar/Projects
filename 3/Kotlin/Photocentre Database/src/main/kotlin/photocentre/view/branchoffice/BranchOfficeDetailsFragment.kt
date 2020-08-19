package photocentre.view.branchoffice

import photocentre.controllers.BranchOfficeController
import photocentre.controllers.KioskController
import photocentre.executors.BranchOfficeExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.dao.BranchOfficeDao
import photocentre.dao.KioskDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.BranchOfficeModel
import photocentre.dataClasses.KioskModel
import photocentre.executors.KioskExecutor
import tornadofx.*

class BranchOfficeDetailsFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val branchOfficeModel: BranchOfficeModel by inject()
    val branchOfficeDao = BranchOfficeDao(photocentreDataSource)
    val branchOfficeController = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, branchOfficeDao))
    val kioskDao = KioskDao(photocentreDataSource)
    val kioskController = KioskController(KioskExecutor(photocentreDataSource, kioskDao))
    val kioskModel: KioskModel by inject()
    var currentID: Long = 0
    var currentAddress: String = ""
    var currentAmount: Int = 0
    override val savable = branchOfficeModel.dirty
    override val root = vbox {
        label("Address")
        textfield(branchOfficeModel.address)
        label("Amount of workers")
        textfield(branchOfficeModel.amountOfWorkers)

        buttonbar {
            button("Save") {
                enableWhen(branchOfficeModel.dirty)
                action {
                    currentID = branchOfficeModel.id.value
                    currentAddress = branchOfficeModel.address.value
                    currentAmount = branchOfficeModel.amountOfWorkers.value
                    branchOfficeController.updateBranchOffice(BranchOffice(currentID, currentAddress, currentAmount))
                    branchOfficeModel.branchOffices.set(branchOfficeController.getBranchOffices().asObservable())
                    kioskModel.kiosks.set(kioskController.getAllKiosks().asObservable())
                }
            }
            button("Undo") {
                enableWhen(branchOfficeModel.dirty)
                action {
                    branchOfficeModel.rollback()
                }
            }
        }

        button("Back") {
            action {
                val branchOfficeCRUDFragment = BranchOfficeCRUDFragment(photocentreDataSource)
                replaceWith(branchOfficeCRUDFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
            }
        }
    }
}