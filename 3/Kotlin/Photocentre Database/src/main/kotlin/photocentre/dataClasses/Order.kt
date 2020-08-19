package photocentre.dataClasses

import com.sun.org.apache.xpath.internal.operations.Bool
import com.sun.org.apache.xpath.internal.operations.Or
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import photocentre.enums.Urgency
import photocentre.enums.OrderType
import photocentre.executors.OrderExecutor
import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.property
import java.sql.Date
import javax.lang.model.type.MirroredTypeException

class Order
(
        id: Long = -1,
        urgent: Boolean = false,
        cost: Float = 0f,
        date: Date = Date(java.util.Date().time),
        completionDate: Date? = null,
        type: OrderType? = null,
        branchOffice: BranchOffice? = null,
        kiosk: Kiosk? = null,
        customer: Customer? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Order::id)

    var urgent: Boolean by property(urgent)
    fun urgentProperty() = getProperty(Order::urgent)

    var cost: Float by property(cost)
    fun costProperty() = getProperty(Order::cost)

    var date: Date by property(date)
    fun dateProperty() = getProperty(Order::date)

    var completionDate: Date by property(completionDate)
    fun completionDateProperty() = getProperty(Order::completionDate)

    var type: OrderType by property(type)
    fun typeProperty() = getProperty(Order::type)

    var branchOffice: BranchOffice by property(branchOffice)
    fun branchOfficeProperty() = getProperty(Order::branchOffice)

    var kiosk: Kiosk by property(kiosk)
    fun kioskProperty() = getProperty(Order::kiosk)

    var customer: Customer by property(customer)
    fun customerProperty() = getProperty(Order::customer)
}

class OrderModel : ItemViewModel<Order>() {
    val orders = SimpleObjectProperty<ObservableList<Order>>()
    val id = bind(Order::idProperty)
    val urgent = bind(Order::urgentProperty)
    val cost = bind(Order::costProperty)
    val date = bind(Order::dateProperty)
    val completionDate = bind(Order::completionDateProperty)
    val type = bind(Order::typeProperty)
    val branchOffice = bind(Order::branchOfficeProperty)
    val kiosk = bind(Order::kioskProperty)
    val customer = bind(Order::customerProperty)
}