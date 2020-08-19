using System.Collections.Generic;
using ContainerInterface;
using JetBrains.Annotations;
using WpfUserInterface.Models;

namespace WpfUserInterface.Dialogs.Service
{
    public interface IFileLoaderService : IComponent
    {
        bool TryLoadFile([NotNull] string fileName, [NotNull] IList<INote> notes);
    }
}