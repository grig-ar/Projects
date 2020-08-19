package photocentre.view.kiosk

import photocentre.controllers.KioskController
import photocentre.dao.KioskDao
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.KioskModel
import photocentre.executors.KioskExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class KioskListFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val kioskDao = KioskDao(photocentreDataSource)
    val kioskController = KioskController(KioskExecutor(photocentreDataSource, kioskDao))
    val kioskModel: KioskModel by inject()

    init {
        kioskModel.kiosks.set(kioskController.getAllKiosks().asObservable())
    }

    override val root = borderpane {
        center = tableview(kioskModel.kiosks) {
            column("ID", Kiosk::idProperty)
            column("Address", Kiosk::addressProperty)
            column("Amount of workers", Kiosk::amountOfWorkersProperty)
            column("Branch office Address", Kiosk::branchOfficeAddressProperty)
            bindSelected(kioskModel)
            contextmenu {
                item("Delete").action {
                    kioskController.deleteKiosk(selectedItem?.id!!)
                    kioskModel.kiosks.set(kioskController.getAllKiosks().asObservable())
                }
            }
            columnResizePolicy = SmartResize.POLICY
        }
    }
}
