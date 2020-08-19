using ContainerInterface;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    public interface IContainer
    {
        [NotNull]
        [MustUseReturnValue]
        T Resolve<T>() where T : class, IComponent;
    }
}