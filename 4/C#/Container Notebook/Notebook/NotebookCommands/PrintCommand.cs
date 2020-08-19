using ContainerInterface;
using GuardUtils;
using JetBrains.Annotations;
using Notebook.Commands;
using Notebook.Notebook;

namespace Notebook.NotebookCommands
{
    [Component]
    public sealed class PrintCommand : ICommand
    {
        [NotNull] private readonly INotebook _notebook;

        public string Name => "Print";
        public string CommandInfo => "prints all notes from notebook";

        public PrintCommand([NotNull] INotebook notebook)
        {
            ThrowIf.Variable.IsNull(notebook, nameof(notebook));

            _notebook = notebook;
        }

        public void Execute()
        {
            _notebook.Print();
        }
    }
}