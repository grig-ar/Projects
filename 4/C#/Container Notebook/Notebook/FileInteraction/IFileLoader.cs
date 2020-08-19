using ContainerInterface;
using JetBrains.Annotations;
using Notebook.Notebook;

namespace Notebook.FileInteraction
{
    public interface IFileLoader : IComponent
    {
        bool TryLoadFile([NotNull] string fileName, [NotNull] INotebook notebook);
    }
}