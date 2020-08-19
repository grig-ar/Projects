using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using GuardUtils;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class InstanceResolver : IInstanceResolver
    {
        [NotNull] private readonly InterfaceToComponentsMap _interfaceToComponentsMap;
        [NotNull] private readonly IReadOnlyDictionary<Type, InstanceCreator> _instanceCreators;
        [NotNull] private readonly SingletonInstanceContainer _singletonInstanceContainer;

        [NotNull] private readonly MethodInfo _genericCreateListMethodInfo;

        public InstanceResolver([NotNull] IReadOnlyList<ComponentInfo> components)
        {
            _singletonInstanceContainer = new SingletonInstanceContainer();
            _interfaceToComponentsMap = new InterfaceToComponentsMap(components);

            var instanceCreators = new Dictionary<Type, InstanceCreator>();

            foreach (var component in components)
            {
                instanceCreators.Add(component.ComponentType, new InstanceCreator(component));
            }

            _instanceCreators = instanceCreators;

            var genericCreateListMethodInfo = GetType().GetMethod(nameof(CreateList),
                BindingFlags.Instance | BindingFlags.NonPublic);

            ThrowIf.Variable.IsNull(genericCreateListMethodInfo, nameof(genericCreateListMethodInfo));

            _genericCreateListMethodInfo = genericCreateListMethodInfo;
        }

        public object Resolve(IParameterInfo parameterInfo)
        {
            if (parameterInfo.IsList)
            {
                return CreateComponentList(parameterInfo.ParameterType);
            }

            try
            {
                return ResolveByInterface(parameterInfo.ParameterType);
            }
            catch (Exception)
            {
                throw new InvalidOperationException(
                    $"{parameterInfo.OwnerType} Can't resolve constructor parameter {parameterInfo.ParameterType}");
            }
        }

        public object ResolveByInterface(Type interfaceType)
        {
            var componentInfo = GetComponentInfo(interfaceType);
            return GetComponentInstance(componentInfo);
        }

        [NotNull]
        private object GetComponentInstance([NotNull] ComponentInfo component)
        {
            if (_singletonInstanceContainer.TryGetInstance(component.ComponentType, out var instance))
            {
                return instance;
            }

            instance = _instanceCreators[component.ComponentType]?.CreateInstance(this);
            ThrowIf.Variable.IsNull(instance, nameof(instance));

            _singletonInstanceContainer.AddSingletonInstance(component.ComponentType, instance);
            return instance;
        }

        [NotNull]
        private ComponentInfo GetComponentInfo([NotNull] Type interfaceType)
        {
            if (!_interfaceToComponentsMap.TryGetComponentsOfInterface(interfaceType, out var components))
            {
                components = new List<ComponentInfo>();
            }

            if (components.Count == 0)
            {
                throw new InvalidOperationException($"{interfaceType} has no any implementation");
            }

            if (components.Count > 1)
            {
                throw new InvalidOperationException($"{interfaceType} has multiply implementations");
            }

            var component = components.First();
            ThrowIf.Variable.IsNull(component, nameof(component));

            return component;
        }

        [NotNull]
        private object CreateComponentList([NotNull] Type itemType)
        {
            if (!_interfaceToComponentsMap.TryGetComponentsOfInterface(itemType, out var components))
            {
                components = new List<ComponentInfo>();
            }

            var parameterListInstance = _genericCreateListMethodInfo.MakeGenericMethod(itemType)
                .Invoke(this, new object[] {components});
            ThrowIf.Variable.IsNull(parameterListInstance, nameof(parameterListInstance));

            return parameterListInstance;
        }

        [NotNull]
        [ItemNotNull]
        private IReadOnlyList<T> CreateList<T>([NotNull] [ItemNotNull] IReadOnlyList<ComponentInfo> components)
            where T : class
        {
            var list = new List<T>(components.Count);

            foreach (var component in components)
            {
                list.Add((T) GetComponentInstance(component));
            }

            return list;
        }
    }
}