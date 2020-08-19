using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommandTypes;
using NoteBookDLL;
using PhoneNoteDLL;
using NoteTypes;

namespace AddPhoneModule
{
    [Component(CommandInfo = "add phone note to notebook")]
    public class AddPhone : ICommand
    {
        private INoteBook _noteBook;

        public AddPhone(NoteBook noteBook)
        {
            _noteBook = noteBook;
        }

        public void Execute()
        {
            var name = "";
            var phone = "";
            Console.WriteLine("Student name:");
            name = Console.ReadLine();
            Console.WriteLine("Student phone:");
            phone = Console.ReadLine();
            _noteBook.Add(new PhoneNote(name, phone));
        }

        public string GetName()
        {
            return "AddPhone";
        }
    }
}
