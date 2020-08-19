package photocentre.view

import javafx.scene.control.TabPane
import photocentre.main.Db
import photocentre.main.PhotocentreDataSource
import photocentre.view.branchoffice.BranchOfficeCRUDFragment
import photocentre.view.branchoffice.BranchOfficeListFragment
import photocentre.view.customer.CustomerCRUDFragment
import photocentre.view.customer.CustomerCreateFragment
import photocentre.view.customer.CustomerListFragment
import photocentre.view.kiosk.KioskCRUDFragment
import photocentre.view.kiosk.KioskListFragment
import photocentre.view.supplier.SupplierCRUDFragment
import photocentre.view.supplier.SupplierListFragment
import photocentre.view.worker.WorkerCRUDFragment
import photocentre.view.worker.WorkerListFragment
import tornadofx.*

class MainView : View("Photocentre Database") {
    val db = Db()
    val photocentreDataSource = PhotocentreDataSource(db.dataSource)

    override val root = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        tab("Offices") {
            splitpane {
                add(BranchOfficeListFragment(photocentreDataSource))
                add(BranchOfficeCRUDFragment(photocentreDataSource))
            }
        }
        tab("Kiosks") {
            splitpane {
                add(KioskListFragment(photocentreDataSource))
                add(KioskCRUDFragment(photocentreDataSource))
            }
        }

        tab("Workers") {
                splitpane {
                    add(WorkerListFragment(photocentreDataSource))
                    add(WorkerCRUDFragment(photocentreDataSource))
                }
        }

        tab("Customers") {
            splitpane {
                add(CustomerListFragment(photocentreDataSource))
                add(CustomerCRUDFragment(photocentreDataSource))
            }
        }

        tab("Suppliers") {
            splitpane() {
                add(SupplierListFragment(photocentreDataSource))
                add(SupplierCRUDFragment(photocentreDataSource))
            }
        }

        //todo табы для таблиц

//        tab("Workers") {
//            vbox {
//
//            }
//        }
//        tab("Amateurs") {
//            vbox {
//
//            }
//        }
//        tab("Professionals") {
//            vbox {
//
//            }
//        }
//        tab("Orders") {
//            vbox {
//
//            }
//        }
//        tab("Photos") {
//            vbox {
//
//            }
//        }
//        tab("Films") {
//            vbox {
//
//            }
//        }
//        tab("Services") {
//            vbox {
//
//            }
//        }
//        tab("Office Items") {
//            vbox {
//
//            }
//        }
//        tab("Sold Items") {
//            vbox {
//
//            }
//        }
//        tab("Supply Items") {
//            vbox {
//
//            }
//        }
//        tab("Supplies") {
//            vbox {
//
//            }
//        }

    }

}
