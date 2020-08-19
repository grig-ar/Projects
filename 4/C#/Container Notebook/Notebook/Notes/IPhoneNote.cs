using JetBrains.Annotations;
using Notebook.Notebook;

namespace Notebook.Notes
{
    public interface IPhoneNote : INote
    {
        [NotNull] string PhoneNumber { get; }
    }
}