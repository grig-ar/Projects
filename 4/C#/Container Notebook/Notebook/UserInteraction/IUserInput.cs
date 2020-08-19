using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.UserInteraction
{
    public interface IUserInput : IComponent
    {
        [MustUseReturnValue]
        bool TryGetString([NotNull] string info, out string userInput);

        [MustUseReturnValue]
        bool TryGetInt([NotNull] string info, out int? userInput);
    }
}