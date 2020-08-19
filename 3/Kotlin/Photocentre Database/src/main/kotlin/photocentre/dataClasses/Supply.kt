package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.*
import java.sql.Date

class Supply
(
        id: Long = -1,
        cost: Float = 0f,
        date: Date = Date(java.util.Date().time),
        completionDate: Date? = null,
        supplier: Supplier? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Supply::id)

    var cost: Float by property(cost)
    fun costProperty() = getProperty(Supply::cost)

    var date: Date by property(date)
    fun dateProperty() = getProperty(Supply::date)

    var completionDate: Date? by property(completionDate)
    fun completionDateProperty() = getProperty(Supply::completionDate)

    var supplier: Supplier? by property(supplier)
    fun supplierProperty() = getProperty(Supply::supplier)
}

class SupplyModel : ItemViewModel<Supply>() {
    val supplies = SimpleObjectProperty<ObservableList<Supply>>()
    val id = bind(Supply::idProperty)
    val cost = bind(Supply::costProperty)
    val date = bind(Supply::dateProperty)
    val completionDate = bind(Supply::completionDateProperty)
    val supplier = bind(Supply::supplierProperty)
}