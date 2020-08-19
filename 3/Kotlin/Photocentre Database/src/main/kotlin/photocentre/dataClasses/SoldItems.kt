package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.*
import java.sql.Date

class SoldItem
(
        id: Long = -1,
        name: String = "",
        cost: Float = 0f,
        date: Date = Date(java.util.Date().time),
        branchOffice: BranchOffice? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(SoldItem::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(SoldItem::name)

    var cost: Float by property(cost)
    fun costProperty() = getProperty(SoldItem::cost)

    var date: Date by property(date)
    fun dateProperty() = getProperty(SoldItem::date)

    var branchOffice: BranchOffice by property(branchOffice)
    fun branchOfficeProperty() = getProperty(SoldItem::branchOffice)
}

class SoldItemModel : ItemViewModel<SoldItem>() {
    val soldItems = SimpleObjectProperty<ObservableList<SoldItem>>()
    val id = bind(SoldItem::idProperty)
    val name = bind(SoldItem::nameProperty)
    val date = bind(SoldItem::dateProperty)
    val branchOffice = bind(SoldItem::branchOfficeProperty)
}