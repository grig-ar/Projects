using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using Lab4.Models;
using System.Data;


namespace Lab4.Pages.Report
{
    public class IndexModel : PageModel
    {
        private readonly Lab4.Models.WorkContext _context;

        public IndexModel(Lab4.Models.WorkContext context)
        {
            _context = context;
        }

        public Worker Worker { get; set; }
        public IList<Worker> Workers { get; set; }
        public IList<Project> Projects { get; set; }

        public string NameSort { get; set; }
        public string AwardSort { get; set; }
        public string CurrentFilter { get; set; }
        public string CurrentSort { get; set; }

        public async Task OnGetAsync(string sortOrder)
        {
            NameSort = String.IsNullOrEmpty(sortOrder) ? "name_desc" : "";
            AwardSort = sortOrder == "TotalAward" ? "totalaward_desc" : "TotalAward";

            IQueryable<Worker> workerIQ = from s in _context.Worker
                                          select s;

            switch (sortOrder)
            {
                case "name_desc":
                    workerIQ = workerIQ.OrderByDescending(s => s.FullName);
                    break;
                case "TotalAward":
                    workerIQ = workerIQ.OrderBy(o => o.Projects.Sum(s => s.Award));
                    //workerIQ = workerIQ.GroupBy(w => w.FullName).
                    break;
                case "totalaward_desc":
                    workerIQ = workerIQ.OrderByDescending(o => o.Projects.Sum(s => s.Award));
                    break;
                default:
                    workerIQ = workerIQ.OrderBy(s => s.FullName);
                    break;
            }

            //Workers = await _context.Worker.ToListAsync();
            Workers = await workerIQ.ToListAsync();
            Projects = await _context.Project.ToListAsync();
            foreach (var item in Workers)
            {
                foreach (var item2 in Projects)
                {
                    if (item.ID == item2.WorkerID)
                        item.Projects.Add(item2);
                }
            }
            //Worker = await _context.Worker.Include(s => s.Projects);
            //Worker = await _context.Worker
            //  .Include(s => s.Projects);
        }
    }
}