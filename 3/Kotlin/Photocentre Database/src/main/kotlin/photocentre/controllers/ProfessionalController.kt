//package photocentre.controllers
//
//import photocentre.dataClasses.Professional
//
//class ProfessionalController(private val executor: ProfessionalExecutor) {
//
//    fun createProfessional(professional: Professional): Professional {
//        val id = executor.createProfessional(professional)
//        return Professional(
//                id = id,
//                discount = professional.discount,
//                branchOffice = professional.branchOffice
//        )
//    }
//
//    fun createProfessionals(professionals: List<Professional>): List<Professional> {
//        val ids = executor.createProfessionals(professionals)
//        val newProfessionals = ArrayList<Professional>()
//
//        for (i in 1..ids.size) {
//            newProfessionals += Professional(
//                    id = ids[i],
//                    discount = professionals[i].discount,
//                    branchOffice = professionals[i].branchOffice
//            )
//        }
//        return newProfessionals
//    }
//
//    fun getProfessional(id: Long): Professional? {
//        return executor.findProfessional(id)
//    }
//
//    fun updateProfessional(professional: Professional): String {
//        return executor.updateProfessional(professional).toString()
//    }
//
//    fun deleteProfessional(id: Long): String {
//        return executor.deleteProfessional(id).toString()
//    }
//
//    fun getAllProfessionals(): List<Professional> {
//        return executor.getAllProfessionals()
//    }
//
//}