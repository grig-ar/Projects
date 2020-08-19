package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import photocentre.enums.ItemType
import tornadofx.*

class SupplyItem
(
        id: Long? = null,
        name: String = "",
        amount: Int = 0,
        type: ItemType? = null,
        supply: Supply? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(SupplyItem::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(SupplyItem::name)

    var amount: Int by property(amount)
    fun amountProperty() = getProperty(SupplyItem::amount)

    var type: ItemType by property(type)
    fun typeProperty() = getProperty(SupplyItem::type)

    var supply: Supply? by property(supply)
    fun supplyProperty() = getProperty(SupplyItem::supply)
}

class SupplyItemModel : ItemViewModel<Supply>() {
    val supplyItems = SimpleObjectProperty<ObservableList<Supply>>()
    val id = bind(SupplyItem::idProperty)
    val amount = bind(SupplyItem::amountProperty)
    val name = bind(SupplyItem::nameProperty)
    val type = bind(SupplyItem::typeProperty)
    val supply = bind(SupplyItem::supplyProperty)
}