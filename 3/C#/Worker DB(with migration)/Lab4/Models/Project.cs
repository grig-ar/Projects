using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace Lab4.Models
{
    public class Project
    {
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int ProjectID { get; set; }
        public int WorkerID { get; set; }
        public string Name { get; set; }
        public int Award { get; set; }
        public Worker Worker { get; set; }

    }
}
