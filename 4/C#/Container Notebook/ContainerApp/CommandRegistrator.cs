//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Threading.Tasks;
//using CommandTypes;

//namespace ContainerApp
//{
//    class CommandRegistrator : ICommandRegistrator
//    {
//        private readonly IDictionary<string, ICommand> _commands;
//        //private readonly IReadOnlyList<ICommand> _commands;
//        public IDictionary<string, ICommand> Commands
//        {
//            get
//            {
//                return _commands;
//            }
//        } 

//        public CommandRegistrator(IReadOnlyList<ICommand> commands)
//        {
//            foreach (var command in commands)
//            {
//                _commands.Add(command.GetName(), command);
//            }
//        }

//        //TODO
//        //public object CreateInstance(Type elemType, IList<Comp> comps)
//        //{
//        //    genericMi.Invoke(this, comps);
//        //}



//        public ICommand GetCommand(string commandName)
//        {
//            ICommand command = null;
//            try
//            {
//                Commands.TryGetValue(commandName, out command);
//            } catch (Exception ex)
//            {
//                Console.WriteLine("Failed to get command: ", ex.Message);
//            }
//            return command;
//        }
//    }
//}
