//package photocentre.controllers
//
//import photocentre.dataClasses.SupplyItem
//import photocentre.executors.SupplyItemExecutor
//
//class SupplyItemController(private val executor: SupplyItemExecutor) {
//
//    fun createSupplyItem(supplyItem: SupplyItem): SupplyItem {
//
//        val id = executor.createSupplyItem(supplyItem)
//        return SupplyItem(
//                id = id,
//                name = supplyItem.name,
//                amount = supplyItem.amount,
//                type = supplyItem.type,
//                supply = supplyItem.supply
//        )
//    }
//
//    fun createSupplyItems(supplyItems: List<SupplyItem>): List<SupplyItem> {
//
//        val ids = executor.createSupplyItems(supplyItems)
//        val newSupplyItems = ArrayList<SupplyItem>()
//
//        for (i in 1..ids.size) {
//            newSupplyItems += SupplyItem(
//                    id = ids[i],
//                    name = supplyItems[i].name,
//                    amount = supplyItems[i].amount,
//                    type = supplyItems[i].type,
//                    supply = supplyItems[i].supply
//            )
//        }
//        return newSupplyItems
//    }
//
//    fun getSupplyItem(id: Long): SupplyItem? {
//        return executor.findSupplyItem(id)
//    }
//
//    fun updateSupplyItem(supplyItem: SupplyItem): String {
//        return executor.updateSupplyItem(supplyItem).toString()
//    }
//
//    fun deleteSupplyItem(id: Long): String {
//        return executor.deleteSupplyItem(id).toString()
//    }
//
//    fun getAllSupplyItems():List<SupplyItem> {
//        return executor.getAllSupplyItems()
//    }
//
//}