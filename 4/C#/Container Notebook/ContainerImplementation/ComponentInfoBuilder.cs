using System;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class ComponentInfoBuilder
    {
        [NotNull] private readonly InterfaceExtractor _interfaceExtractor = new InterfaceExtractor();

        [NotNull] private readonly ComponentConstructorInfoBuilder _componentConstructorInfoBuilder =
            new ComponentConstructorInfoBuilder();

        [NotNull]
        internal ComponentInfo BuildComponentInfo([NotNull] Type componentType)
        {
            var componentInterfaces = _interfaceExtractor.GetComponentInterfaces(componentType);
            var componentConstructorInfo = _componentConstructorInfoBuilder.BuildConstructorInfo(componentType);

            return new ComponentInfo(componentType, componentInterfaces, componentConstructorInfo);
        }
    }
}