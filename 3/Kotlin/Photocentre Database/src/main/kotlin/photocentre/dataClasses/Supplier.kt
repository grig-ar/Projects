package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import photocentre.enums.ItemType
import tornadofx.ItemViewModel
import tornadofx.*

class Supplier
(
        id: Long = -1,
        name: String = "",
        specialization: ItemType? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Supplier::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(Supplier::name)

    var specialization: ItemType by property(specialization)
    fun specializationProperty() = getProperty(Supplier::specialization)
}

class SupplierModel : ItemViewModel<Supplier>() {
    val suppliers = SimpleObjectProperty<ObservableList<Supplier>>()
    val id = bind(Supplier::idProperty)
    val name = bind(Supplier::nameProperty)
    val specialization = bind(Supplier::specializationProperty)
}