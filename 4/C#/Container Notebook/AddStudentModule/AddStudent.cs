using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NoteBookDLL;
using CommandTypes;
using StudentNoteDLL;
using NoteTypes;

namespace AddStudentModule
{
    [Component(CommandInfo = "add student note to notebook")]
    public class AddStudent : ICommand
    {
        private INoteBook _noteBook;

        public AddStudent(NoteBook noteBook)
        {
            _noteBook = noteBook;
        }

        public void Execute()
        {
            var name = "";
            var group = "";
            int groupNumber = -1;
            Console.WriteLine("Student name:");
            name = Console.ReadLine();
            Console.WriteLine("Student group:");
            group = Console.ReadLine();
            var success = Int32.TryParse(group, out groupNumber);
            if (success)
            {
                _noteBook.Add(new StudentNote(name, groupNumber));
            }
            else
            {
                Console.WriteLine("{0} is not a number", group);
            }
        }

        public string GetName()
        {
            return "AddStudent";
        }
    }
}
