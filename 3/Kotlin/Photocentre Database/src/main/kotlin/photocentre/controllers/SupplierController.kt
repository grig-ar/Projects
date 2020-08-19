package photocentre.controllers

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Supplier
import photocentre.enums.ItemType
import photocentre.executors.SupplierExecutor
import java.sql.Date

class SupplierController(private val executor: SupplierExecutor) {

    fun createSupplier(supplier: Supplier): Supplier {
        val id = executor.createSupplier(supplier)
        return Supplier(
                id = id,
                name = supplier.name,
                specialization = supplier.specialization
        )
    }

    fun createSuppliers(suppliers: List<Supplier>): List<Supplier> {
        val ids = executor.createSuppliers(suppliers)
        val newSuppliers = ArrayList<Supplier>()

        for (i in 1..ids.size) {
            newSuppliers += Supplier(
                    id = ids[i],
                    name = suppliers[i].name,
                    specialization = suppliers[i].specialization
            )
        }
        return newSuppliers
    }

    fun getSupplier(id: Long): Supplier? {
        return executor.findSupplier(id)
    }
    
    fun updateSupplier(supplier: Supplier): String {
        return executor.updateSupplier(supplier).toString()
    }

    fun deleteSupplier(id: Long): String {
        return executor.deleteSupplier(id).toString()
    }

    fun getAllSuppliers(): List<Supplier> {
        return executor.getAllSuppliers()
    }

    fun getBySpecialization(specialization: ItemType): List<Supplier> {
        return executor.getBySpecialization(specialization)
    }

    fun getByDate(dateBegin: Date, dateEnd: Date): List<Supplier> {
        return executor.getByDate(dateBegin, dateEnd)
    }

    fun getByAmount(supplyItemAmount: Int, dateBegin: Date, dateEnd: Date): List<Pair<Supplier, Int>> {
        return executor.getByAmount(supplyItemAmount, dateBegin, dateEnd)
    }

    fun getTopSuppliersByBranchOffice(branchOffice: BranchOffice): List<Pair<Supplier, Int>> {
        return executor.getTopSuppliersByBranchOffice(branchOffice)
    }

    fun getTopSuppliers(): List<Pair<Supplier, Int>> {
        return executor.getTopSuppliers()
    }
}