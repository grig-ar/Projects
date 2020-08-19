//package photocentre.controllers
//
//import photocentre.dataClasses.Supply
//import photocentre.executors.SupplyExecutor
//
//class SupplyController(private val executor: SupplyExecutor) {
//
//    fun createSupply(supply: Supply): Supply {
//        val id = executor.createSupply(supply)
//        return Supply(
//                id = id,
//                cost = supply.cost,
//                date = supply.date,
//                completionDate = supply.completionDate,
//                supplier = supply.supplier
//        )
//    }
//
//    fun createSupplies(supplies: List<Supply>): List<Supply> {
//        val ids = executor.createSupplies(supplies)
//        val newSupplies = ArrayList<Supply>()
//
//        for (i in 1..ids.size) {
//            newSupplies += Supply(
//                    id = ids[i],
//                    cost = supplies[i].cost,
//                    date = supplies[i].date,
//                    completionDate = supplies[i].completionDate,
//                    supplier = supplies[i].supplier
//            )
//        }
//        return newSupplies
//    }
//
//    fun getSupply(id: Long): Supply? {
//        return executor.findSupply(id)
//    }
//
//    fun updateSupply(supply: Supply): String {
//        return executor.updateSupply(supply).toString()
//    }
//
//    fun deleteSupply(id: Long): String {
//        return executor.deleteSupply(id).toString()
//    }
//
//    fun getAllSupplies(): List<Supply> {
//        return executor.getAllSupplies()
//    }
//}