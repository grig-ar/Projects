package photocentre.controllers

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Kiosk
import photocentre.dataClasses.Order
import photocentre.enums.OrderType
import photocentre.executors.OrderExecutor
import java.sql.Date

class OrderController(private val executor: OrderExecutor) {

    fun createOrder(order: Order): Order {
        val id = executor.createOrder(order)
        return Order(
                id = id,
                urgent = order.urgent,
                cost = order.cost,
                date = order.date,
                completionDate = order.completionDate,
                type = order.type,
                branchOffice = order.branchOffice,
                kiosk = order.kiosk,
                customer = order.customer
        )
    }

    fun createOrders(orders: List<Order>): List<Order> {
        val ids = executor.createOrders(orders)
        val newOrders = ArrayList<Order>()

        for (i in 1..ids.size) {
            newOrders += Order(
                    id = ids[i],
                    urgent = orders[i].urgent,
                    cost = orders[i].cost,
                    date = orders[i].date,
                    completionDate = orders[i].completionDate,
                    type = orders[i].type,
                    branchOffice = orders[i].branchOffice,
                    kiosk = orders[i].kiosk,
                    customer = orders[i].customer
            )
        }
        return newOrders
    }

    fun getOrder(id: Long): Order? {
        return executor.findOrder(id)
    }

    fun updateOrder(order: Order): String {
        return executor.updateOrder(order).toString()
    }

    fun deleteOrder(id: Long): String {
        return executor.deleteOrder(id).toString()
    }

    fun getByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): List<Order> {
        return executor.getByBranchOffice(branchOffice, dateBegin, dateEnd)
    }

    fun getByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): List<Order> {
        return executor.getByKiosk(kiosk, dateBegin, dateEnd)
    }

    fun countByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Int? {
        return executor.countByBranchOffice(branchOffice, dateBegin, dateEnd)
    }

    fun countByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Int? {
        return executor.countByKiosk(kiosk, dateBegin, dateEnd)
    }

    fun getByDate(dateBegin: Date, dateEnd: Date): List<Order> {
        return executor.getByDate(dateBegin, dateEnd)
    }

    fun getCommonByBranchOfficeAndType(branchOffice: BranchOffice, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        return executor.getCommonByBranchOfficeAndType(branchOffice, type, dateBegin, dateEnd)
    }

    fun getUrgentByBranchOfficeAndType(branchOffice: BranchOffice, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        return executor.getUrgentByBranchOfficeAndType(branchOffice, type, dateBegin, dateEnd)
    }

    fun getCommonByKioskAndType(kiosk: Kiosk, type: OrderType, dateBegin: Date, dateEnd: Date): List<Order> {
        return executor.getCommonByKioskAndType(kiosk, type, dateBegin, dateEnd)
    }

    fun getRevenueByBranchOffice(branchOffice: BranchOffice, dateBegin: Date, dateEnd: Date): Pair<Float?, Float?>? {
        return executor.getRevenueByBranchOffice(branchOffice, dateBegin, dateEnd)
    }

    fun getRevenueByKiosk(kiosk: Kiosk, dateBegin: Date, dateEnd: Date): Float? {
        return executor.getRevenueByKiosk(kiosk, dateBegin, dateEnd)
    }

    fun getRevenueByDate(dateBegin: Date, dateEnd: Date): Pair<Float?, Float?>? {
        return executor.getRevenueByDate(dateBegin, dateEnd)
    }

    fun getAllOrders(): List<Order> {
        return executor.getAllOrders()
    }

}