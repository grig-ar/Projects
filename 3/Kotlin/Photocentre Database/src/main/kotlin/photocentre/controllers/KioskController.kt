package photocentre.controllers

import photocentre.dataClasses.Kiosk
import photocentre.executors.KioskExecutor

class KioskController(private val executor: KioskExecutor) {

    fun createKiosk(kiosk: Kiosk): Kiosk {
        val id = executor.createKiosk(kiosk)
        return Kiosk(
                id = id,
                address = kiosk.address,
                branchOffice = kiosk.branchOffice
        )
    }

    fun createKiosks(kiosks: List<Kiosk>): List<Kiosk> {
        val ids = executor.createKiosks(kiosks)
        val newKiosks = ArrayList<Kiosk>()

        for (i in 1..ids.size) {
            newKiosks += Kiosk(
                    id = ids[i],
                    address = kiosks[i].address,
                    branchOffice = kiosks[i].branchOffice
            )
        }
        return newKiosks
    }

    fun getKiosk(id: Long): String {
        return executor.findKiosk(id).toString()
    }

    fun updateKiosk(kiosk: Kiosk): String {
        return executor.updateKiosk(kiosk).toString()
    }

    fun deleteKiosk(id: Long): String {
        return executor.deleteKiosk(id).toString()
    }

    fun countKiosks(): Int {
        return executor.countKiosks() ?: 0
    }

    fun getAllKiosks(): List<Kiosk> {
        return executor.getAllKiosks()
    }

    fun getAllBranchOfficeAddresses(): List<String> {
        return executor.getAllBranchOfficeAddresses()
    }

}