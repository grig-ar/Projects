using System;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal interface IParameterInfo
    {
        bool IsList { get; }
        [NotNull] Type OwnerType { get; }
        [NotNull] Type ParameterType { get; }
    }
}