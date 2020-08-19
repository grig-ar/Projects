package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.*

class Service
(
        id: Long = -1,
        name: String = "",
        cost: Float = 0f,
        branchOffice: BranchOffice? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Service::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(Service::name)

    var cost: Float by property(cost)
    fun costProperty() = getProperty(Service::cost)

    var branchOffice: BranchOffice by property(branchOffice)
    fun branchOfficeProperty() = getProperty(Service::branchOffice)
}

class ServiceModel : ItemViewModel<Service>() {
    val services = SimpleObjectProperty<ObservableList<Service>>()
    val id = bind(Service::idProperty)
    val name = bind(Service::nameProperty)
    val cost = bind(Service::costProperty)
    val branchOffice = bind(Service::branchOfficeProperty)
}