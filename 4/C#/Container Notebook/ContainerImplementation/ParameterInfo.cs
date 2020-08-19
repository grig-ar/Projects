using System;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal abstract class ParameterInfo : IParameterInfo
    {
        protected ParameterInfo([NotNull] Type ownerType, [NotNull] Type parameterType, bool isList)
        {
            OwnerType = ownerType;
            ParameterType = parameterType;
            IsList = isList;
        }

        public bool IsList { get; }

        public Type ParameterType { get; }

        public Type OwnerType { get; }
    }
}