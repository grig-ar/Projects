using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommandTypes;
using System.IO;
using System.Reflection;
using System.Windows.Forms;
using System.Threading;
using NoteTypes;
using NoteBookDLL;
using AddStudentModule;

namespace ContainerApp
{
    class App
    {


        //static void Main(string[] args)
        //{
        //    AppDomain domain = AppDomain.CurrentDomain;
        //    Console.WriteLine("Name: {0}", domain.FriendlyName);
        //    Console.WriteLine("Base Directory: {0}", domain.BaseDirectory);
        //    Console.WriteLine();
        //    domain.Load("HelpCommand");
        //    domain.Load("AddStudentCommand");
        //    Assembly[] assemblies = domain.GetAssemblies();
        //    //var attr = assemblies.getC
        //    //domain.get
        //    Assembly componentAsm = null;
        //    bool classFound = false;
        //    dynamic test = null;
        //    int index = 0;
        //    do
        //    {
        //        int i = 0;
        //        var theClassTypes = from t in assemblies[i].GetTypes()
        //                            where t.IsClass && (t.GetInterface("ICommand") != null)
        //                            select t;
        //        if (theClassTypes != null)
        //        {
        //            classFound = true;
        //            test = theClassTypes;
        //            index = i;
        //        }
        //    } while (!classFound);

        //    //Console.WriteLine(assemblies[4].GetTypes());
        //    foreach (Type t in test)
        //    {
        //        //foundComponent = true;
        //        // Use late binding to create the type.
        //        ICommand itfApp = (ICommand)assemblies[index].CreateInstance(t.FullName, true);
        //        Console.WriteLine(itfApp?.GetName()); 
        //        //lstLoadedSnapIns.Items.Add(t.FullName);
        //        // Show company info.
        //        //DisplayCompanyData(t);
        //    }
        //    //Console.WriteLine(theClassTypes);
        //    //try
        //    //{

        //    //} catch (Exception ex)
        //    //{
        //    //    Console.WriteLine($"An error occurred loading the component: {ex.Message}");
        //    //}
        //    //for (int i = 4; i < 5; i++)
        //    //{
        //    //    var classTypes = assemblies[i].GetTypes();
        //    //}

        //    //foreach (var asm in assemblies)
        //    //{
        //    //    var theClassTypes = from t in asm.GetTypes()
        //    //                        where t.IsClass && (t.GetInterface("ICommand") != null)
        //    //                        select t;
        //    //}
        //    //var theClassTypes = from t in componentAsm.GetTypes()
        //    //                    where t.IsClass && (t.GetInterface("ICommand") != null)
        //    //                    select t;



        //    //Assembly[] assemblies = domain.GetAssemblies();
        //    //foreach (Assembly asm in assemblies)
        //    //    Console.WriteLine(asm.GetName());

        //    Console.ReadKey();
        //    //List<Note> phoneNotes = new List<Note>() { new PhoneNote("abc", "123") };
        //    //Saver writer = new Saver();
        //    //writer.Save(phoneNotes);
        //    //Loader loader = new Loader();
        //    //List<Note> notes;
        //    //notes = loader.Load();
        //    //Console.ReadKey();
        //}

        [STAThread]
        static void Main(string[] args)
        {


            Container container = new Container();
            container.Build();
            var commandProcessor = container.CreateElement<CommandProcessor>();
            commandProcessor.Run();



            //var commandProcessor = new CommandProcessor(container.Components, container.Application);

            //Dictionary<string, Type> commansds = new Dictionary<string, Type>();
            //foreach (var type in container.Types)
            //{
            //    commansds.Add(type.Name.ToLower(), type);
            //}
            //CommandProcessor commandProcessor = new CommandProcessor(commansds);
            //commandProcessor.Run();



            //container.Resolve<CommandProcessor>();
            //INoteBook noteBook = new NoteBook(new List<Note>());
            //noteBook.PrintHelp();
            //AddStudent addCommand = new AddStudent(ref noteBook);
            //addCommand.Execute();
            //noteBook.Find();
            //HelpModule.Help help = new HelpModule.Help();
            //help.Execute();
        }

        //static void Main(string[] args)
        //{
        //    Container container = new Container();
        //    container.Build();
        //    AppDomain domain = AppDomain.CurrentDomain;
        //    Assembly[] assemblies = domain.GetAssemblies();
        //    //foreach (var asm in assemblies)
        //    //{
        //    //    Console.WriteLine(asm.FullName);
        //    //}

        //    //foreach (var asm in assemblies)
        //    //{
        //    //    if (asm.FullName.Equals("TestModule, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null", StringComparison.OrdinalIgnoreCase))
        //    //    {
        //    //        var types = asm.GetTypes();
        //    //        foreach (var type in types)
        //    //        {
        //    //            var attrs = Attribute.GetCustomAttributes(type);
        //    //            var interfaces = type.GetInterfaces();
        //    //            var ctors = type.GetConstructors();
        //    //            foreach (var ctor in ctors)
        //    //            {
        //    //                var parameters = ctor.GetParameters();
        //    //            }

        //    //        }
        //    //    }
        //    //}
        //    foreach (var asm in assemblies)
        //    {
        //        var theClassTypes = from t in asm.GetTypes()
        //                            where t.IsClass && (t.GetCustomAttribute<ComponentAttribute>() != null)
        //                            select t;
        //        foreach (var classType in theClassTypes)
        //        {

        //            var ctors = classType.GetConstructors();
        //            if (classType.Name.Equals("Y"))
        //            {
        //                //var parameters = ctors[0].GetParameters();
        //                //dynamic obj = Activator.CreateInstance(classType, new object[] { 10 });
        //                //obj.PrintNumber();
        //                dynamic test = Resolve(classType);
        //                test.PrintNumber();
        //                //var method = classType.GetMethod("PrintNumber");
        //                //method.Invoke(obj, null);
        //            }
        //            //foreach (var ctor in ctors)
        //            //{
        //            //    Console.Write(classType.Name + " (");
        //            //    // получаем параметры конструктора
        //            //    ParameterInfo[] parameters = ctor.GetParameters();
        //            //    for (int i = 0; i < parameters.Length; i++)
        //            //    {
        //            //        Console.Write(parameters[i].ParameterType.Name + " " + parameters[i].Name);
        //            //        if (i + 1 < parameters.Length) Console.Write(", ");
        //            //    }
        //            //    Console.WriteLine(")");
        //            //}
        //        }
        //    }
            
                                
                                

        //        //var theClassTypes = from t in asm.GetTypes()
        //        //                    where t.IsClass && (t.GetInterface("ICommand") != null)
        //        //                    select t;
            

        //    //var cp = container.Resolve();
        //    //cp.Run();
        //}

        public static object Resolve(Type contract)
        {
            //Type Implementation = typeSettings[contract];

            var constructor = contract.GetConstructors();

            if (constructor.Length == 0)
            {
                return Activator.CreateInstance(contract);
            }

            var constructorParam = constructor[0].GetParameters();

            if (constructorParam.Length == 0)
                Activator.CreateInstance(contract);

            var paramList = new List<object>(constructorParam.Length);

            foreach (var param in constructorParam)
                paramList.Add(Resolve(param.ParameterType));

            return constructor[0].Invoke(paramList.ToArray());
        }

        public static T Resolve<T>()
        {
            return (T)Resolve(typeof(T));
        }


        private static bool LoadExternalModule(string path)
        {
            bool foundComponent = false;
            Assembly componentAsm = null;
            List<Assembly> assemblies = new List<Assembly>();

            try
            {
                componentAsm = Assembly.LoadFrom(path);
            }

            catch (Exception ex)
            {
                Console.WriteLine($"An error occurred loading the component: {ex.Message}");
                return foundComponent;
            }

            // Get all IAppFunctionality compatible classes in assembly.
            var theClassTypes = from t in componentAsm.GetTypes()
                                where t.IsClass && (t.GetInterface("ICommand") != null)
                                select t;
            // Now, create the object and call DoIt() method.
            foreach (Type t in theClassTypes)
            {
                foundComponent = true;
                // Use late binding to create the type.
                ICommand itfApp = (ICommand)componentAsm.CreateInstance(t.FullName, true);
                itfApp?.Execute();
                //lstLoadedSnapIns.Items.Add(t.FullName);
                // Show company info.
                //DisplayCompanyData(t);
            }
            return foundComponent;
        }

        //private static void DisplayCompanyData(Type t)
        //{
        //    // Get [CompanyInfo] data.
        //    var compInfo = from ci in t.GetCustomAttributes(false)
        //                   where (ci is CompanyInfoAttribute)
        //                   select ci;
        //    // Show data.
        //    foreach (CompanyInfoAttribute c in compInfo)
        //    {
        //        Console.WriteLine($"More info about {c.CompanyName} can be found at {c.CompanyUrl}");
        //    }
        //}

        void Resolve()
        {

        }

        //void Run()
        //{
        //    Console.WriteLine("***** Welcome to MyNoteBook *****");
        //    PrintHelp();
        //    bool running = false;
        //    var action = "";
        //    const string IsReady = "> ";
        //    do
        //    {
        //        Console.WriteLine(1);
        //    } while (running);
        //}

        //static void PrintHelp()
        //{
        //    Console.WriteLine("Author: Grigorovich Artyom; NSU, FIT\nGroup: 16203\nTask: Container notebook\nVersion: 0.0.1\n");
        //    Console.WriteLine("Available commands:");
        //    Console.WriteLine("help -- this message");
        //    Console.WriteLine("exit -- closes notebook");
        //    Console.WriteLine("add student -- adds new student note to notebook;");
        //    Console.WriteLine("add phone  -- adds new phone note to notebook;");
        //    Console.WriteLine("print -- shows all notes;");
        //    Console.WriteLine("search -- searches by name, group or phone");
        //    Console.WriteLine("save -- saves notes to file");
        //    Console.WriteLine("load filename -- tries to load notes from filename");
        //}

    }

    //public static class DynamicObjectFactory
    //{
    //    private static readonly object _lock = new object();

    //    public static object GetInstance(string assemblyName, string className)
    //    {
    //        Monitor.Enter(_lock);
    //        try
    //        {
    //            Assembly asm = Assembly.Load(assemblyName);
    //            return asm.CreateInstance(className, false, BindingFlags.CreateInstance, null, null, null, null);
    //        }
    //        finally
    //        {
    //            Monitor.Exit(_lock);
    //        }
    //    }

    //    public static object GetInstance(string assemblyName, string className, object[] constructorParameters)
    //    {
    //        Monitor.Enter(_lock);
    //        try
    //        {
    //            Assembly asm = Assembly.Load(assemblyName);
    //            return asm.CreateInstance(className, false, BindingFlags.CreateInstance, null, constructorParameters, null, null);
    //        }
    //        finally
    //        {
    //            Monitor.Exit(_lock);
    //        }
    //    }
    //}

}
