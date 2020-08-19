using CommandTypes;
using System;
using System.Linq;
using System.Reflection;

namespace HelpModule
{
    [Component(CommandInfo = "this message")]
    public class Help : ICommand
    {
        public Help()
        {

        }

        public void Execute()
        {

            Console.WriteLine("Commands available via dlls:");
            AppDomain domain = AppDomain.CurrentDomain;
            Assembly[] assemblies = domain.GetAssemblies();

            foreach (var asm in assemblies)
            {
                var types = asm.GetTypes();
                foreach (var type in types)
                {
                    var compInfo = from ci in type.GetCustomAttributes(false)
                                   where (ci is ComponentAttribute)
                                   select ci;
                    // Show data.
                    foreach (ComponentAttribute c in compInfo)
                    {
                        Console.WriteLine($"{type.Name} -- {c.CommandInfo}");
                    }
                }
            }

            //foreach (var asm in assemblies)
            //{
            //    Console.WriteLine(asm.FullName);
            //}
            //Console.WriteLine("help -- this message");
            //Console.WriteLine("exit -- closes notebook");
            //Console.WriteLine("add student -- adds new student note to notebook;");
            //Console.WriteLine("add phone  -- adds new phone note to notebook;");
            //Console.WriteLine("print -- shows all notes;");
            //Console.WriteLine("search -- searches by name, group or phone");
            //Console.WriteLine("save -- saves notes to file");
            //Console.WriteLine("load filename -- tries to load notes from filename");
        }

        public string GetName()
        {
            return "help";
        }
    }
}
