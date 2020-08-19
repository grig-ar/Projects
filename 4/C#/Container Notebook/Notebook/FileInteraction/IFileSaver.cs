using ContainerInterface;
using JetBrains.Annotations;
using Notebook.Notebook;

namespace Notebook.FileInteraction
{
    public interface IFileSaver : IComponent
    {
        bool TrySaveFile([NotNull] string fileName, [NotNull] INotebook notebook);
    }
}