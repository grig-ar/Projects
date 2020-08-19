package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.*

class Kiosk(
        id: Long = -1,
        address: String = "",
        amountOfWorkers: Int = 0,
        branchOffice: BranchOffice? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Kiosk::id)

    var address: String by property(address)
    fun addressProperty() = getProperty(Kiosk::address)

    var amountOfWorkers: Int by property(amountOfWorkers)
    fun amountOfWorkersProperty() = getProperty(Kiosk::amountOfWorkers)

    var branchOffice: BranchOffice by property(branchOffice)
    fun branchOfficeProperty() = getProperty(Kiosk::branchOffice)

    var branchOfficeAddress: String by property(branchOffice?.address)
    fun branchOfficeAddressProperty() = getProperty(Kiosk::branchOfficeAddress)

}

class KioskModel : ItemViewModel<Kiosk>() {
    val kiosks = SimpleObjectProperty<ObservableList<Kiosk>>()
    val id = bind(Kiosk::idProperty)
    val address = bind(Kiosk::addressProperty)
    val amountOfWorkers = bind(Kiosk::amountOfWorkersProperty)
    val branchOffice = bind(Kiosk::branchOfficeProperty)
    val branchOfficeAddress = bind(Kiosk::branchOfficeAddressProperty)
}