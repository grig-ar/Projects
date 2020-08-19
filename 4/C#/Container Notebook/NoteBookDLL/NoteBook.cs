using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using NoteTypes;
using CommandTypes;
using StudentNoteDLL;
using PhoneNoteDLL;

namespace NoteBookDLL
{
    [Application]
    public class NoteBook : INoteBook
    {
        //public NoteBook()
        //{
        //    _notes = new List<Note>();
        //}

        public NoteBook(List<Note> notes)
        {
            Notes = notes;
        }

        public List<Note> Notes { get; set; }

        public void Add(Note note)
        {
            Notes.Add(note);
        }

        public void Find()
        {
            Console.WriteLine("Select category:(name, phone, group)");
            var category = Console.ReadLine().ToLower();
            bool result = false;
            switch (category)
            {
                case "name":
                    Console.WriteLine("Input name: ");
                    result = SearchByName(Console.ReadLine());
                    break;
                case "phone":
                    Console.WriteLine("Input phone: ");
                    result = SearchByPhone(Console.ReadLine());
                    break;
                case "group":
                    Console.WriteLine("Input group: ");
                    string group;
                    int groupNumber;
                    group = Console.ReadLine();
                    var success = int.TryParse(group, out groupNumber);
                    if (success)
                    {
                        result = SearchByGroup(groupNumber);
                    }
                    else
                    {
                        Console.WriteLine("{0} is not a number", group);
                    }
                    break;
                default:
                    Console.WriteLine("Incorrect category!");
                    return;
            }
            if (!result)
            {
                Console.WriteLine("No records found.");
            }
        }

        private bool SearchByName(string name)
        {
            var results = from res in Notes
                          where res.Name.Equals(name)
                          select res;

            if(results != null && results.Any())
            {
                foreach (var item in results)
                {
                    Console.WriteLine(item.ToString());
                }
                return true;
            }
            return false;
        }

        private bool SearchByPhone(string phone)
        {
            //bool result = false;
            //Node currentNode = list.Head;
            //while (currentNode.Current != null)
            //{

            //    if (currentNode.Current is PhoneNote temp)
            //    {
            //        if (temp.PhoneNumber.Equals(phone))
            //            Console.WriteLine(currentNode.Current.ToString());
            //        result = true;
            //    }
            //    currentNode = currentNode.Next;
            //}
            //return result;
            var results = from res in Notes
                          where (res is PhoneNote) && ((res as PhoneNote).PhoneNumber.Equals(phone))
                          select res;

            if (results != null && results.Any())
            {
                foreach (var item in results)
                {
                    Console.WriteLine(item.ToString());
                }
                return true;
            }
            return false;
        }

        private bool SearchByGroup(int groupNumber)
        {
            //bool result = false;
            //Node currentNode = list.Head;
            //while (currentNode.Current != null)
            //{

            //    if (currentNode.Current is StudentNote temp)
            //    {
            //        if (temp.GroupNumber.Equals(group))
            //            Console.WriteLine(currentNode.Current.ToString());
            //        result = true;
            //    }
            //    currentNode = currentNode.Next;
            //}
            //return result;
            var results = from res in Notes
                          where res is StudentNote && ((StudentNote) res).GroupNumber == groupNumber
                          select res;

            if (results != null && results.Any())
            {
                foreach (var item in results)
                {
                    Console.WriteLine(item.ToString());
                }
                return true;
            }
            return false;
        }

        public void Help()
        {
            Console.WriteLine("Author: Grigorovich Artyom; NSU, FIT\nGroup: 16203\nTask: Container notebook\nVersion: 0.0.1\n");
            Console.WriteLine("Notebook commands: ");
            Console.WriteLine("Print -- shows all notes;");
            Console.WriteLine("Find -- searches by name, group or phone");
            Console.WriteLine("Help -- this message");
            Console.WriteLine("Exit -- closes notebook");
            Console.WriteLine("DLL commands: ");
            var domain = AppDomain.CurrentDomain;
            var assemblies = domain.GetAssemblies();

            foreach (var asm in assemblies)
            {
                var types = asm.GetTypes();
                foreach (var type in types)
                {
                    var compInfo = from ci in type.GetCustomAttributes(false)
                                   where (ci is ComponentAttribute)
                                   select ci;

                    foreach (ComponentAttribute c in compInfo)
                    {
                        Console.WriteLine($"{type.Name} -- {c.CommandInfo}");
                    }
                }
            }
        }

        public void Print()
        {
            foreach (var note in Notes)
            {
                Console.WriteLine(note.ToString());
            }
        }

    }
}
