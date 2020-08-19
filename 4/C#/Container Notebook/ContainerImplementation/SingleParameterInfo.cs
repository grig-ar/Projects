using System;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class SingleParameterInfo : ParameterInfo
    {
        public SingleParameterInfo([NotNull] Type ownerType, [NotNull] Type parameterType) : base(ownerType,
            parameterType, false)
        {
        }
    }
}