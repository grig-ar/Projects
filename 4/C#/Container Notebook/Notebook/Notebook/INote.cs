using JetBrains.Annotations;

namespace Notebook.Notebook
{
    public interface INote
    {
        [NotNull] string Name { get; }
    }
}