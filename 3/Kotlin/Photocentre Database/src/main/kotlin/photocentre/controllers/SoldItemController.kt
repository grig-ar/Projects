package photocentre.controllers

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.SoldItem
import photocentre.executors.SoldItemExecutor
import java.sql.Date

class SoldItemController(private val executor: SoldItemExecutor) {

    fun createItem(soldItem: SoldItem): SoldItem {
        val id = executor.createSoldItem(soldItem)
        return SoldItem(
                id = id,
                name = soldItem.name,
                cost = soldItem.cost,
                date = soldItem.date,
                branchOffice = soldItem.branchOffice
        )
    }

    fun createItems(soldItems: List<SoldItem>): List<SoldItem> {
        val ids = executor.createSoldItems(soldItems)
        val newSoldItems = ArrayList<SoldItem>()

        for (i in 1..ids.size) {
            newSoldItems += SoldItem(
                    id = ids[i],
                    name = soldItems[i].name,
                    cost = soldItems[i].cost,
                    date = soldItems[i].date,
                    branchOffice = soldItems[i].branchOffice
            )
        }
        return newSoldItems
    }

    fun getItem(id: Long): SoldItem? {
        return executor.findSoldItem(id)
    }

    fun updateItem(soldItem: SoldItem): String {
        return executor.updateSoldItem(soldItem).toString()
    }

    fun deleteItem(id: Long): String {
        return executor.deleteSoldItem(id).toString()
    }

    fun getRevenueByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Float? {
        return executor.getRevenueByBranchOffice(branchOffice, dateBegin, dateEnd)
    }

    fun getRevenueByDate(dateBegin: Date, dateEnd: Date): Float? {
        return executor.getRevenueByDate(dateBegin, dateEnd)
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): List<Pair<SoldItem, Int>> {
        return executor.countByBranchOffice(branchOffice, dateBegin, dateEnd)
    }

    fun countByDate(dateBegin: Date, dateEnd: Date): List<Pair<SoldItem, Int>> {
        return executor.countByDate(dateBegin, dateEnd)
    }

    fun getAllSoldItems(): List<SoldItem> {
        return executor.getAllSoldItems()
    }

}