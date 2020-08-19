using System.Linq;
using GuardUtils;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class InstanceCreator
    {
        [NotNull] private readonly ComponentInfo _component;

        public InstanceCreator([NotNull] ComponentInfo component)
        {
            _component = component;
        }

        [NotNull]
        public object CreateInstance([NotNull] IInstanceResolver componentResolver)
        {
            var constructorInfo = _component.ConstructorInfo;
            var parameters = constructorInfo.Parameters.Select(componentResolver.Resolve).ToArray();
            var newInstance = constructorInfo.Constructor.Invoke(parameters);
            ThrowIf.Variable.IsNull(newInstance, nameof(newInstance));

            return newInstance;
        }
    }
}