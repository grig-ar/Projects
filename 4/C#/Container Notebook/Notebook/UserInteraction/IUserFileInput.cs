using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.UserInteraction
{
    public interface IUserFileInput : IComponent
    {
        [MustUseReturnValue]
        bool TryGetFileName([NotNull] string type, out string userFileInput);
    }
}