using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace Lab4.Models
{
    public class WorkContext : DbContext
    {
        public WorkContext (DbContextOptions<WorkContext> options)
            : base(options)
        {
        }

        public DbSet<Worker> Worker { get; set; }
        public DbSet<Project> Project { get; set; }
    }
}
