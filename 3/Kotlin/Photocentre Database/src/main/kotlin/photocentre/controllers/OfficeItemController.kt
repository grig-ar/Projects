package photocentre.controllers

import photocentre.dataClasses.OfficeItem
import photocentre.executors.OfficeItemExecutor

class OfficeItemController(private val executor: OfficeItemExecutor) {

    fun createItem(item: OfficeItem): OfficeItem {
        val id = executor.createOfficeItem(item)
        return OfficeItem(
                id = id,
                forSale = item.forSale,
                amount = item.amount,
                recommendedAmount = item.recommendedAmount,
                criticalAmount = item.criticalAmount,
                name = item.name,
                cost = item.cost,
                type = item.type,
                branchOffice = item.branchOffice
        )
    }

    fun createItems(items: List<OfficeItem>): List<OfficeItem> {
        val ids = executor.createOfficeItems(items)
        val newOfficeItems = ArrayList<OfficeItem>()

        for (i in 1..ids.size) {
            newOfficeItems += OfficeItem(
                    id = ids[i],
                    forSale = items[i].forSale,
                    amount = items[i].amount,
                    recommendedAmount = items[i].recommendedAmount,
                    criticalAmount = items[i].criticalAmount,
                    name = items[i].name,
                    cost = items[i].cost,
                    type = items[i].type,
                    branchOffice = items[i].branchOffice
            )
        }
        return newOfficeItems
    }

    fun getItem(id: Long): String {
        return executor.findOfficeItem(id).toString()
    }
    
    fun updateItem(item: OfficeItem): String {
        return executor.updateOfficeItem(item).toString()
    }
 
    fun deleteItem(id: Long): String {
        return executor.deleteOfficeItem(id).toString()
    }

    fun getAllOfficeItems(): List<OfficeItem> {
        return executor.getAllOfficeItems()
    }
}