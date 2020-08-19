package photocentre.view.branchoffice

import photocentre.controllers.BranchOfficeController
import photocentre.executors.BranchOfficeExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.dao.BranchOfficeDao
import photocentre.dataClasses.BranchOfficeModel
import photocentre.dataClasses.FilteredBranchOffice
import photocentre.dataClasses.FilteredBranchOfficeModel
import tornadofx.*

class BranchOfficeCRUDFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val fragment = this
    val branchOfficeModel: BranchOfficeModel by inject()
    val filteredBranchOfficeModel: FilteredBranchOfficeModel by inject()
    val branchOfficeDao = BranchOfficeDao(photocentreDataSource)
    val branchOfficeController = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, branchOfficeDao))

    init {
        //filteredBranchOfficeModel.filteredBranchOffices.set((branchOfficeController.getBranchOffices() )
    }

    override val root = borderpane {
        center = vbox(10.0) {
            paddingAll = 10
            button("Create") {
                action {
                    val branchOfficeCreateFragment = BranchOfficeCreateFragment(photocentreDataSource, fragment)
                    replaceWith(branchOfficeCreateFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
            }
            button("Update") {
                action {
                    val branchOfficeDetailsFragment = BranchOfficeDetailsFragment(photocentreDataSource)
                    replaceWith(branchOfficeDetailsFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
            }
//            button("selectBranchOfficesAndKiosks") {
//                action {
//                    val selectBranchOfficeAndKiosksFragment = BranchOfficeSelectBranchOfficeAndKiosksFragment(photocentreDataSource)
//                    replaceWith(selectBranchOfficeAndKiosksFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
//                }
//            }
            button("countBranchOffices") {
                action {
                    val countOfficeFragment = BranchOfficeSelectCountOfficeFragment(photocentreDataSource)
                    replaceWith(countOfficeFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
            }
            label("")
            label("Filter_ID")
            val idText = textfield()
            label("Filter_Address")
            val addressText = textfield()
            label("Filter_Amount")
            val amountText = textfield()
            label("")
            val myTable = tableview(filteredBranchOfficeModel.filteredBranchOffices) {
                column("ID", FilteredBranchOffice::filteredIdProperty)
                column("Address", FilteredBranchOffice::filteredAddressProperty)
                column("Amount of workers", FilteredBranchOffice::filteredAmountOfWorkersProperty)
                bindSelected(filteredBranchOfficeModel)
                columnResizePolicy = SmartResize.POLICY
            }
            button("Filter") {
                action {
                    val idFilter = idText.text
                    val addressFilter = addressText.text
                    val amountFilter = amountText.text
                    filteredBranchOfficeModel.filteredBranchOffices.set(
                            branchOfficeController.filterBranchOffice(idFilter, addressFilter, amountFilter).asObservable()
                    )
                    //val idFilter = filteredBranchOfficeModel.filteredId.value

                }
            }

        }
    }
}
