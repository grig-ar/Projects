using System;
using ContainerImplementation;
using Notebook.Commands;


namespace Notebook
{
    public sealed class Program
    {
        [STAThread]
        private static void Main(string[] args)
        {
            var containerBuilder = new ContainerBuilder();
            var container = containerBuilder.Build();
            var commandProcessor = container.Resolve<ICommandProcessor>();
            commandProcessor.Run();
        }
    }
}