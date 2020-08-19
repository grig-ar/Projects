using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using Lab4.Models;

namespace Lab4.Pages.Workers
{
    public class IndexModel : PageModel
    {
        private readonly Lab4.Models.WorkContext _context;

        public IndexModel(Lab4.Models.WorkContext context)
        {
            _context = context;
        }

        public IList<Worker> Worker { get;set; }

        public async Task OnGetAsync()
        {
            Worker = await _context.Worker.ToListAsync();
        }
    }
}
