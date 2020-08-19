package photocentre.view.kiosk

import javafx.collections.FXCollections
import photocentre.controllers.KioskController
import photocentre.dao.KioskDao
import photocentre.executors.KioskExecutor
import photocentre.main.PhotocentreDataSource
import tornadofx.*

class KioskSelectCountFragment(photocentreDataSource: PhotocentreDataSource) : Fragment() {
    val kioskDao = KioskDao(photocentreDataSource)
    val kioskController = KioskController(KioskExecutor(photocentreDataSource, kioskDao))
    var amount: Int = 0

    init {
        amount = kioskController.countKiosks()
    }

    private var count = FXCollections.observableArrayList<TotalKiosks>(
            TotalKiosks(amount)
    )
    override val root = vbox {
        tableview(count) {
            column("Total kiosks", TotalKiosks::amountProperty)
        }
        button("Back") {
            action {
                val kioskCRUDFragment = KioskCRUDFragment(photocentreDataSource)
                replaceWith(kioskCRUDFragment, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
            }
        }
    }
}

class TotalKiosks(amount: Int) {
    var amount: Int by property(amount)
    fun amountProperty() = getProperty(TotalKiosks::amount)
}
