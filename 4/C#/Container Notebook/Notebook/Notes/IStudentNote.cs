using JetBrains.Annotations;
using Notebook.Notebook;

namespace Notebook.Notes
{
    public interface IStudentNote : INote
    {
        int GroupNumber { get; }
    }
}