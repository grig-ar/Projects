using System;
using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class ComponentInfo
    {
        public ComponentInfo([NotNull] Type componentType,
            [NotNull] [ItemNotNull] IReadOnlyList<Type> componentInterfaces,
            [NotNull] ConstructorInfo constructorInfo)
        {
            ComponentType = componentType;
            ComponentInterfaces = componentInterfaces;
            ConstructorInfo = constructorInfo;
        }

        [NotNull] public Type ComponentType { get; }
        [NotNull] [ItemNotNull] public IReadOnlyList<Type> ComponentInterfaces { get; }
        [NotNull] public ConstructorInfo ConstructorInfo { get; }

        public bool SupportInterface([NotNull] Type interfaceType)
        {
            return ComponentInterfaces.Contains(interfaceType);
        }
    }
}