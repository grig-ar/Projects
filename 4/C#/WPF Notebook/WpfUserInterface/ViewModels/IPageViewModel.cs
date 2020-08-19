using ContainerInterface;
using WpfUserInterface.Models;

namespace WpfUserInterface.ViewModels
{
    public interface IPageViewModel : IComponent
    {
        string Name { get; }
        bool TryCreateNote(out INote note);
    }
}