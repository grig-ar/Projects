package photocentre.dataClasses

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.ItemViewModel
import tornadofx.*

class Film(
        id: Long = -1,
        name: String = "",
        soldItem: SoldItem? = null,
        order: Order? = null
) {
    var id: Long by property(id)
    fun idProperty() = getProperty(Film::id)

    var name: String by property(name)
    fun nameProperty() = getProperty(Film::name)

    var soldItem: SoldItem by property(soldItem)
    fun soldItemProperty() = getProperty(Film::soldItem)

    var order: Order by property(order)
    fun orderProperty() = getProperty(Film::order)
}

class FilmModel : ItemViewModel<Film>() {
    val films = SimpleObjectProperty<ObservableList<Film>>()
    val id = bind(Film::idProperty)
    val name = bind(Film::nameProperty)
    val soldItem = bind(Film::soldItemProperty)
    val order = bind(Film::orderProperty)
}