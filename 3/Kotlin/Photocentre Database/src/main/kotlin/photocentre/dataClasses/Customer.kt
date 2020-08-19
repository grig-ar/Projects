package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.*

class Customer
(
        id: Long = -1,
        name: String = "",
        discount: Int = 0,
        experience: Int = 0,
        branchOffice: BranchOffice? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Customer::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(Customer::name)

    var discount: Int by property(discount)
    fun discountProperty() = getProperty(Customer::discount)

    var experience: Int by property(experience)
    fun experienceProperty() = getProperty(Customer::experience)

    var branchOffice: BranchOffice by property(branchOffice)
    fun branchOfficeProperty() = getProperty(Customer::branchOffice)

    var branchOfficeAddress: String by property(branchOffice?.address)
    fun branchOfficeAddressProperty() = getProperty(Customer::branchOfficeAddress)
}

class CustomerModel : ItemViewModel<Customer>() {
    val customers = SimpleObjectProperty<ObservableList<Customer>>()
    val id = bind(Customer::idProperty)
    val name = bind(Customer::nameProperty)
    val discount = bind(Customer::discountProperty)
    val experience = bind(Customer::experienceProperty)
    val branchOffice = bind(Customer::branchOfficeProperty)
    val branchOfficeAddress = bind(Customer::branchOfficeAddressProperty)
}