using ContainerInterface;

namespace Notebook.Commands
{
    public interface ICommandProcessor : IComponent
    {
        void Run();
    }
}