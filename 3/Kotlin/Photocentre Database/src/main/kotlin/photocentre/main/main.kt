package photocentre.main

import photocentre.controllers.*
import photocentre.dao.*
import photocentre.executors.*

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            //DB
            val db = Db()
            val photocentreDataSource = PhotocentreDataSource(db.dataSource)
            
            //DAO
        }
    }
}