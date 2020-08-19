using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using Lab4.Models;

namespace Lab4.Pages.Projects
{
    public class CreateModel : PageModel
    {
        private readonly Lab4.Models.WorkContext _context;

        public CreateModel(Lab4.Models.WorkContext context)
        {
            _context = context;
        }

        public IActionResult OnGet()
        {
        ViewData["WorkerFullName"] = new SelectList(_context.Worker, "ID", "FullName");
            return Page();
        }

        [BindProperty]
        public Project Project { get; set; }

        //[BindProperty]
        //public Worker Worker { get; set; }

        public async Task<IActionResult> OnPostAsync()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }

            _context.Project.Add(Project);
            await _context.SaveChangesAsync();

            return RedirectToPage("./Index");
        }
    }
}