package photocentre.view.supplier

import photocentre.controllers.SupplierController
import photocentre.dao.SupplierDao
import photocentre.dataClasses.Supplier
import photocentre.dataClasses.SupplierModel
import photocentre.executors.SupplierExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class SupplierListFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val supplierDao = SupplierDao(photocentreDataSource)
    val supplierController = SupplierController(SupplierExecutor(photocentreDataSource, supplierDao))
    val supplierModel: SupplierModel by inject()

    init {
        supplierModel.suppliers.set(supplierController.getAllSuppliers().asObservable())
    }

    override val root = borderpane {
        center = tableview(supplierModel.suppliers) {
            column("ID", Supplier::idProperty)
            column("Name", Supplier::nameProperty)
            column("Specialization", Supplier::specializationProperty)
            bindSelected(supplierModel)
            contextmenu {
                item("Delete").action {
                    supplierController.deleteSupplier(selectedItem?.id!!)
                    supplierModel.suppliers.set(supplierController.getAllSuppliers().asObservable())
                }
            }
            columnResizePolicy = SmartResize.POLICY
        }
    }
}
