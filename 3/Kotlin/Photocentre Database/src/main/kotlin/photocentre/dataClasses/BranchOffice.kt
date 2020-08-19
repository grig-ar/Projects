package photocentre.dataClasses

import javafx.beans.property.*
import javafx.collections.ObservableList
import tornadofx.*

class BranchOffice(id: Long = -1, address: String = "", amountOfWorkers: Int = 0) {

    var id: Long by property(id)
    fun idProperty() = getProperty(BranchOffice::id)

    var address: String by property(address)
    fun addressProperty() = getProperty(BranchOffice::address)

    var amountOfWorkers: Int by property(amountOfWorkers)
    fun amountOfWorkersProperty() = getProperty(BranchOffice::amountOfWorkers)
}

class BranchOfficeModel : ItemViewModel<BranchOffice>() {
    val branchOffices = SimpleObjectProperty<ObservableList<BranchOffice>>()
    val id = bind(BranchOffice::idProperty)
    val address = bind(BranchOffice::addressProperty)
    val amountOfWorkers = bind(BranchOffice::amountOfWorkersProperty)
}

class FilteredBranchOffice(id: Long = -1, address: String = "", amountOfWorkers: Int = 0) {

    var filteredId: Long by property(id)
    fun filteredIdProperty() = getProperty(FilteredBranchOffice::filteredId)

    var filteredAddress: String by property(address)
    fun filteredAddressProperty() = getProperty(FilteredBranchOffice::filteredAddress)

    var filteredAmountOfWorkers: Int by property(amountOfWorkers)
    fun filteredAmountOfWorkersProperty() = getProperty(FilteredBranchOffice::filteredAmountOfWorkers)
}

class FilteredBranchOfficeModel : ItemViewModel<FilteredBranchOffice>() {
    val filteredBranchOffices = SimpleObjectProperty<ObservableList<FilteredBranchOffice>>()
    val filteredId = bind(FilteredBranchOffice::filteredIdProperty)
    val filteredAddress = bind(FilteredBranchOffice::filteredAddressProperty)
    val filteredAmountOfWorkers = bind(FilteredBranchOffice::filteredAmountOfWorkersProperty)
}

class BranchOfficeAndKioskModel : ItemViewModel<Pair<BranchOffice, Kiosk>>() {
    val pair = SimpleObjectProperty<ObservableList<Pair<BranchOffice, Kiosk>>>()
    val idOffice = bind(BranchOffice::idProperty)
    val idKiosk = bind(Kiosk::idProperty)
    val addressOffice = bind(BranchOffice::addressProperty)
    val addressKiosk = bind(Kiosk::addressProperty)
}