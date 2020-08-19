//package photocentre.executors
//
//import photocentre.dao.SupplyItemDao
//import photocentre.dataClasses.SupplyItem
//import photocentre.main.PhotocentreDataSource
//
//class SupplyItemExecutor(
//        private val dataSource: PhotocentreDataSource,
//        private val supplyItemDao: SupplyItemDao
//) {
//    fun createSupplyItem(toCreate: SupplyItem): Long {
//        return transaction(dataSource) {
//            supplyItemDao.createSupplyItem(toCreate)
//        }
//    }
//
//    fun createSupplyItems(toCreate: Iterable<SupplyItem>): List<Long> {
//        return transaction(dataSource) {
//            supplyItemDao.createSupplyItems(toCreate)
//        }
//    }
//
//    fun findSupplyItem(id: Long): SupplyItem? {
//        return transaction(dataSource) {
//            supplyItemDao.findSupplyItem(id)
//        }
//    }
//
//    fun updateSupplyItem(supplyItem: SupplyItem) {
//        return transaction(dataSource) {
//            supplyItemDao.updateSupplyItem(supplyItem)
//        }
//    }
//
//    fun deleteSupplyItem(id: Long) {
//        return transaction(dataSource) {
//            supplyItemDao.deleteSupplyItem(id)
//        }
//    }
//
//    fun getAllSupplyItems(): List<SupplyItem> {
//        return transaction(dataSource) {
//            supplyItemDao.gelAll()
//        }
//    }
//}