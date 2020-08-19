using System.Collections.Generic;
using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.Notebook
{
    public interface INotebook : IComponent
    {
        [NotNull] [ItemNotNull] IReadOnlyList<INote> Notes { get; }
        void Add([NotNull] INote note);
        void Remove([NotNull] INote note);
        void RemoveAllNotes();
        void Print();
    }
}