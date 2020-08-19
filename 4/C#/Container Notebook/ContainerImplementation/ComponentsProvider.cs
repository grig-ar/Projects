using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using ContainerInterface;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class ComponentsProvider
    {
        [NotNull]
        [ItemNotNull]
        public IReadOnlyList<Type> GetComponents()
        {
            var components = new List<Type>();

            foreach (var assembly in AppDomain.CurrentDomain.GetAssemblies())
            {
                components.AddRange(assembly.GetTypes().Where(x => x.GetCustomAttribute<ComponentAttribute>() != null));
            }

            return components;
        }
    }
}