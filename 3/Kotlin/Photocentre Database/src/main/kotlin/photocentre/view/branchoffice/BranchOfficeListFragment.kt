package photocentre.view.branchoffice

import javafx.scene.control.TableView
import tornadofx.*
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

class BranchOfficeListFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val branchOfficeDao = BranchOfficeDao(photocentreDataSource)
    val branchOfficeCtrl = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, branchOfficeDao))
    val branchOfficeModel: BranchOfficeModel by inject()
    val kioskDao = KioskDao(photocentreDataSource)
    val kioskController = KioskController(KioskExecutor(photocentreDataSource, kioskDao))
    val kioskModel: KioskModel by inject()
    //val myTable = TableView<BranchOffice>()

    init {
        branchOfficeModel.branchOffices.set(branchOfficeCtrl.getBranchOffices().asObservable())
    }

    override val root = borderpane {
        //val data = SortedFilteredList(branchOfficeModel.branchOffices.get()).bindTo(myTable)
        center = tableview(branchOfficeModel.branchOffices) {
            column("ID", BranchOffice::idProperty)
            column("Address", BranchOffice::addressProperty)
            column("Amount of workers", BranchOffice::amountOfWorkersProperty)
            bindSelected(branchOfficeModel)
            contextmenu {
                item("Delete").action {
                    //branchOfficeModel.branchOffices
                    branchOfficeCtrl.deleteBranchOffice(selectedItem?.id!!)
                    branchOfficeModel.branchOffices.set(branchOfficeCtrl.getBranchOffices().asObservable())
                    kioskModel.kiosks.set(kioskController.getAllKiosks().asObservable())
                }
            }
            columnResizePolicy = SmartResize.POLICY
        }
        //center = myTable
//        center = tableview(branchOfficeModel.branchOffices) {
//            column("ID", BranchOffice::idProperty)
//            column("Address", BranchOffice::addressProperty)
//            column("Amount of workers", BranchOffice::amountOfWorkersProperty)
//            bindSelected(branchOfficeModel)
//            contextmenu {
//                item("Delete").action {
//                    //branchOfficeModel.branchOffices
//                    branchOfficeCtrl.deleteBranchOffice(selectedItem?.id!!)
//                    branchOfficeModel.branchOffices.set(branchOfficeCtrl.getBranchOffices().asObservable())
//                    kioskModel.kiosks.set(kioskController.getAllKiosks().asObservable())
//                }
//            }
//            columnResizePolicy = SmartResize.POLICY
//        }
    }
}