using System.Collections.Generic;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class ConstructorInfo
    {
        [NotNull] public System.Reflection.ConstructorInfo Constructor { get; }
        [NotNull] public IReadOnlyList<IParameterInfo> Parameters { get; }

        public ConstructorInfo([NotNull] System.Reflection.ConstructorInfo constructor,
            [NotNull] IReadOnlyList<IParameterInfo> parameters)
        {
            Constructor = constructor;
            Parameters = parameters;
        }
    }
}