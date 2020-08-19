using System;
using System.Collections.Generic;
using System.Linq;
using ContainerInterface;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class InterfaceExtractor
    {
        [NotNull]
        [ItemNotNull]
        internal IReadOnlyList<Type> GetComponentInterfaces([NotNull] Type componentType)
        {
            return componentType.GetInterfaces().Where(x => typeof(IComponent).IsAssignableFrom(x)).ToList();
        }
    }
}