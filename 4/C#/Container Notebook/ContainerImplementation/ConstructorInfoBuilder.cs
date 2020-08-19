using System;
using System.Linq;
using GuardUtils;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class ComponentConstructorInfoBuilder
    {
        [NotNull] private readonly ConstructorParametersExtractor _constructorParametersExtractor =
            new ConstructorParametersExtractor();

        [NotNull]
        internal ConstructorInfo BuildConstructorInfo([NotNull] Type type)
        {
            var constructors = type.GetConstructors();
            if (constructors.Length == 0)
            {
                throw new InvalidOperationException($"Type has no public constructors {type.FullName}");
            }

            if (constructors.Length > 1)
            {
                throw new InvalidOperationException($"Type multiple public constructors {type.FullName}");
            }

            var constructor = constructors.First();
            ThrowIf.Variable.IsNull(constructor, nameof(constructor));

            var parameters = _constructorParametersExtractor.GetParameters(constructor);

            return new ConstructorInfo(constructor, parameters);
        }
    }
}