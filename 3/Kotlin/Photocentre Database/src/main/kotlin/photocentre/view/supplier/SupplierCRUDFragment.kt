package photocentre.view.supplier

import photocentre.controllers.SupplierController
import photocentre.dao.SupplierDao
import photocentre.dataClasses.SupplierModel
import photocentre.executors.SupplierExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class SupplierCRUDFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val supplierModel: SupplierModel by inject()
    val supplierDao = SupplierDao(photocentreDataSource)
    val supplierController = SupplierController(SupplierExecutor(photocentreDataSource, supplierDao))
    override val root = borderpane {
        center = vbox(10.0) {
            paddingAll = 10
            button("Create") {
                action {
                    val supplierCreateFragment = SupplierCreateFragment(photocentreDataSource)
                    replaceWith(supplierCreateFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
            }
            button("Update") {
                action {
                    val supplierDetailsFragment = SupplierDetailsFragment(photocentreDataSource)
                    replaceWith(supplierDetailsFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
            }
//            button("countKiosks") {
//                action {
//                    val countKiosksFragment = KioskSelectCountFragment(photocentreDataSource)
//                    replaceWith(countKiosksFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
//                }
//            }
        }
    }
}
