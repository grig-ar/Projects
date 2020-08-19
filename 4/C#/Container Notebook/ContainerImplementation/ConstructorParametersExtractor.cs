    using System.Collections.Generic;
using System.Linq;
    using GuardUtils;
    using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class ConstructorParametersExtractor
    {
        [NotNull]
        [ItemNotNull]
        public IReadOnlyList<IParameterInfo> GetParameters([NotNull] System.Reflection.ConstructorInfo constructor)
        {
            var parameters = new List<IParameterInfo>();

            var ownerType = constructor.ReflectedType;
            ThrowIf.Variable.IsNull(ownerType, nameof(ownerType));

            foreach (var parameter in constructor.GetParameters())
            {
                var parameterType = parameter.ParameterType;

                if (parameterType.IsGenericType && parameterType.GetGenericTypeDefinition() == typeof(IReadOnlyList<>))
                {
                    var itemType = parameterType.GetGenericArguments().First();
                    ThrowIf.Variable.IsNull(itemType, nameof(itemType));

                    parameters.Add(new ListParameterInfo(ownerType, itemType));
                }
                else
                {
                    parameters.Add(new SingleParameterInfo(ownerType, parameterType));
                }
            }

            return parameters;
        }
    }
}