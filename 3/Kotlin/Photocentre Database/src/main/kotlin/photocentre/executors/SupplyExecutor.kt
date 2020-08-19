//package photocentre.executors
//
//import photocentre.dao.SupplyDao
//import photocentre.dataClasses.Supply
//import photocentre.main.PhotocentreDataSource
//
//class SupplyExecutor(
//        private val dataSource: PhotocentreDataSource,
//        private val supplyDao: SupplyDao
//) {
//    fun createSupply(toCreate: Supply): Long {
//        return transaction(dataSource) {
//            supplyDao.createSupply(toCreate)
//        }
//    }
//
//    fun createSupplies(toCreate: Iterable<Supply>): List<Long> {
//        return transaction(dataSource) {
//            supplyDao.createSupplies(toCreate)
//        }
//    }
//
//    fun findSupply(id: Long): Supply? {
//        return transaction(dataSource) {
//            supplyDao.findSupply(id)
//        }
//    }
//
//    fun updateSupply(supply: Supply) {
//        return transaction(dataSource) {
//            supplyDao.updateSupply(supply)
//        }
//    }
//
//    fun deleteSupply(id: Long) {
//        return transaction(dataSource) {
//            supplyDao.deleteSupply(id)
//        }
//    }
//
//    fun getAllSupplies(): List<Supply> {
//        return transaction(dataSource) {
//            supplyDao.gelAll()
//        }
//    }
//}