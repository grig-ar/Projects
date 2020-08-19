using JetBrains.Annotations;

namespace WpfUserInterface.Models
{
    public interface IPhoneNote : INote
    {
        [NotNull] string PhoneNumber { get; set; }
    }
}