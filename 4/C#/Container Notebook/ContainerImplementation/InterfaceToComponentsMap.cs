using System;
using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class InterfaceToComponentsMap
    {
        [NotNull] private IReadOnlyDictionary<Type, IReadOnlyList<ComponentInfo>> _map;

        public InterfaceToComponentsMap([NotNull] [ItemNotNull] IReadOnlyList<ComponentInfo> components)
        {
            var interfaces = GetComponentInterfaces(components);
            var map = interfaces.ToDictionary(interfaceType => interfaceType,
                interfaceType => GetInterfaceComponents(interfaceType, components));
            _map = map;
        }

        [NotNull]
        [ItemNotNull]
        private IReadOnlyList<Type> GetComponentInterfaces(
            [NotNull] [ItemNotNull] IReadOnlyList<ComponentInfo> components)
        {
            return components.SelectMany(x => x.ComponentInterfaces).Distinct().ToList();
        }

        [NotNull]
        [ItemNotNull]
        private IReadOnlyList<ComponentInfo> GetInterfaceComponents([NotNull] Type interfaceType,
            [NotNull] [ItemNotNull] IReadOnlyList<ComponentInfo> components)
        {
            return components.Where(x => x.SupportInterface(interfaceType)).ToList();
        }

        [MustUseReturnValue]
        public bool TryGetComponentsOfInterface([NotNull] Type interfaceType,
            out IReadOnlyList<ComponentInfo> components)
        {
            return _map.TryGetValue(interfaceType, out components);
        }
    }
}