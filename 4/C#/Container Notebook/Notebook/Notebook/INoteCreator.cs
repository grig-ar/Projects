using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.Notebook
{
    public interface INoteCreator : IComponent
    {
        [NotNull] string NoteType { get; }
        [NotNull] string Info { get; }

        [MustUseReturnValue]
        bool TryCreateNote(out INote note);
    }
}