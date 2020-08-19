using System;
using System.Collections.Generic;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class SingletonInstanceContainer
    {
        [NotNull] private readonly Dictionary<Type, object> _singletonInstances = new Dictionary<Type, object>();

        [MustUseReturnValue]
        public bool TryGetInstance([NotNull] Type instanceType, out object singletonInstance)
        {
            return _singletonInstances.TryGetValue(instanceType, out singletonInstance);
        }

        public void AddSingletonInstance([NotNull] Type instanceType, object singletonInstance)
        {
            _singletonInstances.Add(instanceType, singletonInstance);
        }
    }
}