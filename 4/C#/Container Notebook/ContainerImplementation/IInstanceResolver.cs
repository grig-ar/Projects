using System;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal interface IInstanceResolver
    {
        [NotNull]
        object Resolve([NotNull] IParameterInfo parameterInfo);

        [NotNull]
        object ResolveByInterface([NotNull] Type interfaceType);
    }
}