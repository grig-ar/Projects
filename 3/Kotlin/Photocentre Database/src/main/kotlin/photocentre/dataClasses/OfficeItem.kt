package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import photocentre.enums.ItemType
import tornadofx.*

class OfficeItem
(
        id: Long = -1,
        forSale: Boolean = false,
        amount: Int = 0,
        recommendedAmount: Int = 0,
        criticalAmount: Int = 0,
        name: String = "",
        cost: Float = 0f,
        type: ItemType? = null,
        branchOffice: BranchOffice? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(OfficeItem::id)

    var forSale: Boolean by property(forSale)
    fun forSaleProperty() = getProperty(OfficeItem::forSale)

    var amount: Int by property(amount)
    fun amountProperty() = getProperty(OfficeItem::amount)

    var recommendedAmount: Int by property(recommendedAmount)
    fun recommendedAmountProperty() = getProperty(OfficeItem::recommendedAmount)

    var criticalAmount: Int by property(criticalAmount)
    fun criticalAmountProperty() = getProperty(OfficeItem::criticalAmount)

    var name: String by property(name)
    fun nameProperty() = getProperty(OfficeItem::name)

    var cost: Float by property(cost)
    fun costProperty() = getProperty(OfficeItem::cost)

    var type: ItemType by property(type)
    fun typeProperty() = getProperty(OfficeItem::type)

    var branchOffice: BranchOffice by property(branchOffice)
    fun branchOfficeProperty() = getProperty(OfficeItem::branchOffice)
}

class OfficeItemModel : ItemViewModel<OfficeItem>() {
    val officeItems = SimpleObjectProperty<ObservableList<OfficeItem>>()
    val id = bind(OfficeItem::idProperty)
    val forSale = bind(OfficeItem::forSaleProperty)
    val amount = bind(OfficeItem::amountProperty)
    val recommendedAmount = bind(OfficeItem::recommendedAmountProperty)
    val criticalAmount = bind(OfficeItem::criticalAmountProperty)
    val name = bind(OfficeItem::nameProperty)
    val cost = bind(OfficeItem::costProperty)
    val type = bind(OfficeItem::typeProperty)
    val branchOffice = bind(OfficeItem::branchOfficeProperty)
}