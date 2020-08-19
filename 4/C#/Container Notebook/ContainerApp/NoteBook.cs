//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Reflection;
//using System.Text;
//using System.Threading.Tasks;
//using CommandTypes;
//using NoteTypes;

//namespace ContainerApp
//{
//    class NoteBook : INoteBook
//    {
//        private readonly List<Note> _notes;

//        public NoteBook(List<Note> notes)
//        {
//            _notes = notes;
//        }

//        public List<Note> Notes
//        {
//            get
//            {
//                return _notes;
//            }
//        }

//        public void Add(Note note)
//        {
//            throw new NotImplementedException();
//        }

//        public void Find(String element)
//        {
//            throw new NotImplementedException();
//        }

//        public void PrintHelp()
//        {
//            Console.WriteLine("Author: Grigorovich Artyom; NSU, FIT\nGroup: 16203\nTask: Container notebook\nVersion: 0.0.1\n");
//            Console.WriteLine("Notebook commands: ");
//            Console.WriteLine("Print -- shows all notes;");
//            Console.WriteLine("Search -- searches by name, group or phone");
//            Console.WriteLine("Help -- this message");
//            Console.WriteLine("Exit -- closes notebook");
//            Console.WriteLine("DLL commands: ");
//            AppDomain domain = AppDomain.CurrentDomain;
//            Assembly[] assemblies = domain.GetAssemblies();

//            foreach (var asm in assemblies)
//            {
//                var types = asm.GetTypes();
//                foreach (var type in types)
//                {
//                    var compInfo = from ci in type.GetCustomAttributes(false)
//                                   where (ci is ComponentAttribute)
//                                   select ci;

//                    foreach (ComponentAttribute c in compInfo)
//                    {
//                        Console.WriteLine($"{type.Name} -- {c.CommandInfo}");
//                    }
//                }
//            }
//        }

//    }
//}
