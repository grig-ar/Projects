package photocentre.view.branchoffice

import photocentre.controllers.BranchOfficeController
import photocentre.dao.BranchOfficeDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.BranchOfficeAndKioskModel
import photocentre.dataClasses.BranchOfficeModel
import photocentre.dataClasses.Kiosk
import photocentre.executors.BranchOfficeExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class BranchOfficeSelectBranchOfficeAndKiosksFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val branchOfficeDao = BranchOfficeDao(photocentreDataSource)
    private val branchOfficeController = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, branchOfficeDao))
    private val branchOfficeAndKioskModel: BranchOfficeAndKioskModel by inject()

    init {
        branchOfficeAndKioskModel.pair.set(branchOfficeController.getBranchOfficesAndKiosks().asObservable())
    }

    override val root = borderpane {
        center = tableview(branchOfficeAndKioskModel.pair) {
            column("ID Office", BranchOffice::idProperty)
            column("Address Office", BranchOffice::addressProperty)
            column("ID Kiosk", Kiosk::idProperty)
            column("Address Kiosk", Kiosk::addressProperty)
            bindSelected(branchOfficeAndKioskModel)
            columnResizePolicy = SmartResize.POLICY
        }
    }
}