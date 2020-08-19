using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.Commands
{
    public interface ICommand : IComponent
    {
        [NotNull] string Name { get; }

        [NotNull] string CommandInfo { get; }

        void Execute();
    }
}