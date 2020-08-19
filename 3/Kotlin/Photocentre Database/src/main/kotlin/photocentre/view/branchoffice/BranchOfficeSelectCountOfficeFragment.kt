package photocentre.view.branchoffice

import tornadofx.*
import javafx.collections.FXCollections
import photocentre.controllers.BranchOfficeController
import photocentre.executors.BranchOfficeExecutor
import photocentre.main.PhotocentreDataSource
import photocentre.dao.BranchOfficeDao

class BranchOfficeSelectCountOfficeFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val branchOfficeDao = BranchOfficeDao(photocentreDataSource)
    val branchOfficeController = BranchOfficeController(BranchOfficeExecutor(photocentreDataSource, branchOfficeDao))
    var amount: Int = 0

    init {
        amount = branchOfficeController.countBranchOffices()
    }

    private var count = FXCollections.observableArrayList<TotalBranchOffice>(
            TotalBranchOffice(amount)
    )
    override val root = vbox {
        tableview(count) {
            column("Total offices", TotalBranchOffice::amountProperty)
        }
        button("Back") {
            action {
                val branchOfficeCRUDFragment = BranchOfficeCRUDFragment(photocentreDataSource)
                replaceWith(branchOfficeCRUDFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
            }
        }
    }
}

class TotalBranchOffice(amount: Int) {
    var amount: Int by property(amount)
    fun amountProperty() = getProperty(TotalBranchOffice::amount)
}
