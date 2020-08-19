using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Lab4.Models
{
    public class Worker
    {
        public int ID { get; set; }
        public string FullName { get; set; }
        public string Hobby { get; set; }

        public ICollection<Project> Projects { get; set; }
    }
}
