using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    public sealed class ContainerBuilder
    {
        [NotNull] private readonly ComponentsProvider _componentsProvider = new ComponentsProvider();
        [NotNull] private readonly ComponentInfoBuilder _componentInfoBuilder = new ComponentInfoBuilder();

        [NotNull]
        [MustUseReturnValue]
        public IContainer Build()
        {
            var instanceResolver = new InstanceResolver(GetComponents());
            return new Container(instanceResolver);
        }

        [NotNull]
        [ItemNotNull]
        private IReadOnlyList<ComponentInfo> GetComponents()
        {
            var components = _componentsProvider.GetComponents();
            return components.Select(_componentInfoBuilder.BuildComponentInfo).ToList();
        }
    }
}