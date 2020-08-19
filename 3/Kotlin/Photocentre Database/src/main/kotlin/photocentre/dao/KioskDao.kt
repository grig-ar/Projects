package photocentre.dao

import photocentre.dataClasses.BranchOffice
import photocentre.dataClasses.Film
import photocentre.dataClasses.Kiosk
import java.sql.Statement
import java.sql.Types.BIGINT
import javax.sql.DataSource

class KioskDao(private val dataSource: DataSource) {

    fun createKiosk(kiosk: Kiosk): Long {
        val statement = dataSource.connection.prepareStatement(
                "insert into kiosks (kiosk_address, kiosk_amount_of_workers, branch_office_id) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        statement.setString(1, kiosk.address)
        statement.setInt(2, kiosk.amountOfWorkers)
        statement.setLong(3, kiosk.branchOffice.id)

        statement.executeUpdate()
        val generated = statement.generatedKeys
        generated.next()
        return generated.getLong(1)
    }

    fun createKiosks(toCreate: Iterable<Kiosk>): List<Long> {
        val statement = dataSource.connection.prepareStatement(
                "INSERT INTO kiosks (kiosk_address, kiosk_amount_of_workers, branch_office_id) VALUES(?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        for (kiosk in toCreate) {
            statement.setString(1, kiosk.address)
            statement.setInt(2, kiosk.amountOfWorkers)
            statement.setLong(3, kiosk.branchOffice.id)
            statement.addBatch()
        }

        statement.executeUpdate()
        val generated = statement.generatedKeys
        val res = ArrayList<Long>()
        while (generated.next()) {
            res += generated.getLong(1)
        }

        return res
    }

    fun findKiosk(id: Long): Kiosk? {
        val statement = dataSource.connection.prepareStatement(
                "SELECT kiosk_id, kiosk_address, kiosk_amount_of_workers, branch_office_id FROM kiosks WHERE kiosk_id = ?"
        )

        statement.setLong(1, id)
        val resultSet = statement.executeQuery()
        val branchOfficeDao = BranchOfficeDao(dataSource)

        if (resultSet.next()) {
            return Kiosk(
                    id = resultSet.getLong("kiosk_id"),
                    address = resultSet.getString("kiosk_address"),
                    amountOfWorkers = resultSet.getInt("kiosk_amount_of_workers"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        }
        return null
    }

    fun updateKiosk(kiosk: Kiosk) {
        val statement = dataSource.connection.prepareStatement(
                "UPDATE kiosks SET kiosk_address = ?, kiosk_amount_of_workers = ?, branch_office_id = ? WHERE kiosk_id = ?"
        )

        statement.setString(1, kiosk.address)
        statement.setInt(2, kiosk.amountOfWorkers)
        statement.setLong(3, kiosk.branchOffice.id)
        statement.setLong(4, kiosk.id)
        statement.executeUpdate()
    }

    fun deleteKiosk(id: Long) {
        val statement = dataSource.connection.prepareStatement(
                "DELETE FROM kiosks WHERE kiosk_id = ?"
        )
        statement.setLong(1, id)
        statement.executeUpdate()
    }

    fun countKiosks(): Int? {
        val statement = dataSource.connection.prepareStatement(
                "select count(*) as Total_Kiosks from kiosks"
        )
        val resultSet = statement.executeQuery()
        return if (resultSet.next()) {
            resultSet.getInt("Total_kiosks")
        } else {
            null
        }
    }

    fun gelAll(): List<Kiosk> {
        val statement = dataSource.connection.prepareStatement(
                "select * from kiosks"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<Kiosk>()
        val branchOfficeDao = BranchOfficeDao(dataSource)

        while (resultSet.next()) {
            res += Kiosk(
                    id = resultSet.getLong("kiosk_id"),
                    address = resultSet.getString("kiosk_address"),
                    amountOfWorkers = resultSet.getInt("kiosk_amount_of_workers"),
                    branchOffice = branchOfficeDao.findBranchOffice(resultSet.getLong("branch_office_id"))
            )
        }
        return res
    }

    fun getAllAddresses(): List<String> {
        val statement = dataSource.connection.prepareStatement( "select distinct branch_office_address from branch_offices"
                //"select distinct branch_office_address from kiosks join branch_offices on kiosks.branch_office_id = branch_offices.branch_office_id"
        )
        val resultSet = statement.executeQuery()
        val res = ArrayList<String>()
        while(resultSet.next()) {
            res += resultSet.getString("branch_office_address")
        }
        return res
    }

}