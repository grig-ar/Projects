using System;
using ContainerInterface;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class Container : IContainer
    {
        [NotNull] private readonly IInstanceResolver _instanceResolver;

        public Container([NotNull] IInstanceResolver instanceResolver)
        {
            _instanceResolver = instanceResolver;
        }

        [MustUseReturnValue]
        public T Resolve<T>() where T : class, IComponent
        {
            if (!typeof(T).IsInterface)
            {
                throw new InvalidOperationException($"{typeof(T).FullName} must be interface type");
            }

            var instance = _instanceResolver.ResolveByInterface(typeof(T));
            if (instance == null)
            {
                throw new InvalidOperationException($"Can't resolve instance for {typeof(T).FullName}");
            }

            return (T) instance;
        }
    }
}