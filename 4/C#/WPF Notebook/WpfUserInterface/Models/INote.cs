using JetBrains.Annotations;
using IComponent = ContainerInterface.IComponent;

namespace WpfUserInterface.Models
{
    public interface INote : IComponent
    {
        [NotNull] string Caption { get; set; }
    }
}