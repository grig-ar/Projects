package photocentre.main

import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource

class Db {
    //todo пароль менять не забывай
    val dataSource = PGSimpleDataSource().apply {
        serverName = "localhost"
        portNumber = 5432
        databaseName = "postgres"
        user = "postgres"
        password = "111"
    }

    init {

        val flyway = Flyway
            .configure()
            .dataSource(dataSource)
            .load()

        // Start the migration

        flyway.migrate()
    }
}