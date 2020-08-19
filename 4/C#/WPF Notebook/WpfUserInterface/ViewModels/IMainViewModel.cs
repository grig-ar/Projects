using ContainerInterface;

namespace WpfUserInterface.ViewModels
{
    public interface IMainViewModel : IComponent
    {
        string Name { get; }
    }
}