package photocentre.executors

import photocentre.dao.OrderDao
import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.Order
import photocentre.enums.OrderType
import photocentre.main.PhotocentreDataSource
import java.sql.Date

class OrderExecutor(
        private val dataSource: PhotocentreDataSource,
        private val orderDao: OrderDao
) {
    fun createOrder(toCreate: Order): Long {
        return transaction(dataSource) {
            orderDao.createOrder(toCreate)
        }
    }

    fun createOrders(toCreate: Iterable<Order>): List<Long> {
        return transaction(dataSource) {
            orderDao.createOrders(toCreate)
        }
    }

    fun findOrder(id: Long): Order? {
        return transaction(dataSource) {
            orderDao.findOrder(id)
        }
    }

    fun updateOrder(order: Order) {
        return transaction(dataSource) {
            orderDao.updateOrder(order)
        }
    }

    fun deleteOrder(id: Long) {
        return transaction(dataSource) {
            orderDao.deleteOrder(id)
        }
    }

    fun getByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): List<Order> {
        return transaction(dataSource) {
            orderDao.getByBranchOffice(branchOffice, dateBegin, dateEnd)
        }
    }

    fun getByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): List<Order> {
        return transaction(dataSource) {
            orderDao.getByKiosk(kiosk, dateBegin, dateEnd)
        }
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            orderDao.countbyBranchOffice(branchOffice, dateBegin, dateEnd)
        }
    }

    fun countByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        return transaction(dataSource) {
            orderDao.countbyKiosk(kiosk, dateBegin, dateEnd)
        }
    }

    fun getByDate(dateBegin: Date, dateEnd: Date): List<Order> {
        return transaction(dataSource) {
            orderDao.getByDate(dateBegin, dateEnd)
        }
    }

    fun getCommonByBranchOfficeAndType(branchOffice: BranchOffice, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        return transaction(dataSource) {
            orderDao.getCommonByBranchOfficeAndType(branchOffice, type, dateBegin, dateEnd)
        }
    }

    fun getUrgentByBranchOfficeAndType(branchOffice: BranchOffice, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        return transaction(dataSource) {
            orderDao.getUrgentByBranchOfficeAndType(branchOffice, type, dateBegin, dateEnd)
        }
    }

    fun getCommonByKioskAndType(kiosk: Kiosk, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        return transaction(dataSource) {
            orderDao.getCommonByKioskAndType(kiosk, type, dateBegin, dateEnd)
        }
    }

    fun getRevenueByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Pair<Float?, Float?>? {
        return transaction(dataSource) {
            orderDao.getRevenueByBranchOffice(branchOffice, dateBegin, dateEnd)
        }
    }

    fun getRevenueByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Float? {
        return transaction(dataSource) {
            orderDao.getRevenueByKiosk(kiosk, dateBegin, dateEnd)
        }
    }

    fun getRevenueByDate(dateBegin: Date, dateEnd: Date): Pair<Float?, Float?>? {
        return transaction(dataSource) {
            orderDao.getRevenueByDate(dateBegin, dateEnd)
        }
    }

    fun getAllOrders(): List<Order> {
        return transaction(dataSource) {
            orderDao.gelAll()
        }
    }
}