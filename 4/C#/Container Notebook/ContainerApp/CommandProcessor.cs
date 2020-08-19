using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using CommandTypes;
using NoteBookDLL;

namespace ContainerApp
{
    class CommandProcessor
    {
        //private IDictionary<string, Type> _commands;
        private Dictionary<string, object> _commands;
        private Tuple<Type, object> _application;

        public void Run()
        {
            Console.WriteLine("***** Welcome to MyNoteBook *****");
            Console.WriteLine("Type help for help message");
            
            bool running = true;
            var action = "";
            const string isReady = "> ";
            do
            {
                Console.Write(isReady);
                action = Console.ReadLine().ToLower();
                action = Regex.Replace(action, @"\s+", "");
                if (action.Equals("exit", StringComparison.OrdinalIgnoreCase) ||
                    action.Equals("quit", StringComparison.OrdinalIgnoreCase) ||
                    action.Equals("q", StringComparison.OrdinalIgnoreCase))
                {
                    running = false;
                }
                dynamic commandType = null;
                //foreach (var command in _commands)
                //{
                //    if (command.ToString().Equals(action, StringComparison.OrdinalIgnoreCase))
                //        (command as ICommand).Execute();
                //}
                var isDLLCommand = _commands.TryGetValue(action, out commandType);
                if (!isDLLCommand)
                {
                    //dynamic app = 
                    foreach (var method in _application.Item1.GetMethods())
                    {
                        if (method.Name.Equals(action, StringComparison.OrdinalIgnoreCase))
                            method.Invoke(_application.Item2, null);
                    }
                }
                //MethodInfo[] methods = commandType.GetType().GetMethods();
                //methods[0].Invoke(commandType, null);
                commandType?.Execute();
                //dynamic obj = Resolve(commandType);
                //obj.Execute();
            } while (running);
        }

        public CommandProcessor(Dictionary<string, object> commands, Tuple<Type, object> application)
        {
            _commands = commands;
            _application = application;
        }

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
            {
                return Activator.CreateInstance(contract);
            }

            var paramList = new List<object>(constructorParam.Length);

            foreach (var param in constructorParam)
                paramList.Add(Resolve(param.ParameterType));

            return constructor[0].Invoke(paramList.ToArray());
        }

        public static T Resolve<T>()
        {
            return (T)Resolve(typeof(T));
        }

        //object CreateInstance(Type elemType, IList<ICommand> cmds)
        //{

        //}

    }

    public static class DynamicObjectFactory
    {
        //public static object GetInstance(string assemblyName, string className)
        //{
        //    Assembly asm = Assembly.Load(assemblyName);
        //    return asm.CreateInstance(className, false, BindingFlags.CreateInstance, null, null, null, null);
        //}

        public static object GetInstance(string assemblyName, string className, object[] constructorParameters)
        {
            Assembly asm = Assembly.Load(assemblyName);
            return asm.CreateInstance(className, false, BindingFlags.CreateInstance, null, constructorParameters, null, null);
        }
        //public static object GetInstance(Type type, string className, object[] constructorParameters)
        //{
        //    //Assembly asm = Assembly.Load(assemblyName);
        //    return asm.CreateInstance(className, false, BindingFlags.CreateInstance, null, constructorParameters, null, null);
        //}
    }

}
