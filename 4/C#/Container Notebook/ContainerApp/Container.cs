using CommandTypes;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using NoteBookDLL;

namespace ContainerApp
{
    internal class Container
    {
        private List<Assembly> _assemblies = new List<Assembly>();
        private List<Type> _types = new List<Type>();
        private Dictionary<string, object> _components = new Dictionary<string, object>();
        private Tuple<Type, object> _application = null;

        public List<Assembly> Assemblies { get => _assemblies; set => _assemblies = value; }
        public List<Type> Types { get => _types; set => _types = value; }
        public Dictionary<string, object> Components { get => _components; }

        public Tuple<Type, object> Application { get => _application; set => _application = value; }

        //public IA Resolve<IA>()
        //{
        //    return default(IA);
        //}

        public void Build()
        {
            string binPath = AppDomain.CurrentDomain.BaseDirectory;

            foreach (string dll in Directory.GetFiles(binPath, "*.dll", SearchOption.AllDirectories))
            {
                Assembly loadedAssembly = Assembly.LoadFile(dll);
                var appTypes = from t in loadedAssembly.GetTypes()
                               where t.IsClass && (t.GetCustomAttribute<ApplicationAttribute>() != null)
                               select t;
                foreach (var app in appTypes)
                {
                    Application = Tuple.Create(app, Resolve(app));
                }
            }

            foreach (string dll in Directory.GetFiles(binPath, "*.dll", SearchOption.AllDirectories))
            {

                try
                {
                    Assembly loadedAssembly = Assembly.LoadFile(dll);
                    var theClassTypes = from t in loadedAssembly.GetTypes()
                                        where t.IsClass && (t.GetCustomAttribute<ComponentAttribute>() != null)
                                        select t;

                    foreach (var classType in theClassTypes)
                    {
                        _types.Add(classType);
                        _components.Add(classType.Name.ToLower(), Resolve(classType, Application.Item2));
                    }

                    _assemblies.Add(loadedAssembly);

                    //var appTypes = from t in loadedAssembly.GetTypes()
                    //               where t.IsClass && (t.GetCustomAttribute<ApplicationAttribute>() != null)
                    //               select t;
                    //foreach (var app in appTypes)
                    //{
                    //    Application = Tuple.Create(app, Resolve(app));
                    //}
                    
                }
                catch (FileLoadException loadEx)
                {
                    Console.WriteLine($"{dll} has already been loaded: {loadEx.Message}");
                } // The Assembly has already been loaded.
                catch (BadImageFormatException imgEx)
                {
                    Console.WriteLine($"{dll} is not an assembly: {imgEx.Message}");
                } // If a BadImageFormatException exception is thrown, the file is not an assembly.

            } // foreach dll
        }

        public static object Resolve(Type type, params object[] values)
        {

            var ctors = type.GetConstructors();

            if (ctors.Length == 0)
            {
                return Activator.CreateInstance(type);
            }

            var parameters = ctors[0].GetParameters();

            if (parameters.Length == 0)
            {
                return Activator.CreateInstance(type);
            }

            var paramList = new List<object>(parameters.Length);

            if (values.Length != 0)
            {
                return ctors[0].Invoke(values);
            }

            foreach (var param in parameters)
                paramList.Add(Resolve(param.ParameterType));

            return ctors[0].Invoke(paramList.ToArray());
        }

        public static T Resolve<T>()
        {
            return (T)Resolve(typeof(T));
            //Type type = typeof(T);
            //var ctors = type.GetConstructors();
            //if (ctors.Length != 1)
            //    throw new Exception("Illegal constructors amount");

            //var parameters = ctors[0].GetParameters();

            //if (parameters.Length == 0)
            //{
            //    return Activator.CreateInstance(type);
            //}

            //var paramList = new List<object>(parameters.Length);

            //foreach (var param in parameters)
            //    paramList.Add(Resolve(param.ParameterType));

            //return ctors[0].Invoke(paramList.ToArray());

            //foreach (var classType in _types)
            //{

            //    var ctors = classType.GetConstructors();

            //    //var parameters = ctors[0].GetParameters();
            //    //dynamic obj = Activator.CreateInstance(classType, new object[] { 10 });
            //    //obj.PrintNumber();
            //    dynamic test = Resolve(classType);
            //    test.PrintNumber();
            //    //var method = classType.GetMethod("PrintNumber");
            //    //method.Invoke(obj, null);

            //    //foreach (var ctor in ctors)
            //    //{
            //    //    Console.Write(classType.Name + " (");
            //    //    // получаем параметры конструктора
            //    //    ParameterInfo[] parameters = ctor.GetParameters();
            //    //    for (int i = 0; i < parameters.Length; i++)
            //    //    {
            //    //        Console.Write(parameters[i].ParameterType.Name + " " + parameters[i].Name);
            //    //        if (i + 1 < parameters.Length) Console.Write(", ");
            //    //    }
            //    //    Console.WriteLine(")");
            //    //}
            //}


        }

        //public static object CreateElement(Type contract)
        //{
        //    var constructor = contract.GetConstructors();
        //    if (constructor.Length == 0)
        //    {
        //        return Activator.CreateInstance(contract);
        //    }
        //    var constructorParam = constructor[0].GetParameters();
        //    if (constructorParam.Length == 0)
        //    {
        //        Activator.CreateInstance(contract);
        //    }
        //    var paramList = new List<object>(constructorParam.Length);
        //    foreach (var param in constructorParam)
        //    {
        //        paramList.Add(CreateElement(param.ParameterType));
        //    }
        //    return constructor[0].Invoke(paramList.ToArray());
        //}

        public T CreateElement<T>()
        {
            Type type = typeof(T);
            var ctors = type.GetConstructors();
            return (T)ctors[0].Invoke(new object[] { Components, Application });
        }

    }
}
