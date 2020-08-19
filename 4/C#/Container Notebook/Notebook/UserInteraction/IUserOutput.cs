using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.UserInteraction
{
    public interface IUserOutput : IComponent
    {
        void WriteMessage([NotNull] string message);
    }
}