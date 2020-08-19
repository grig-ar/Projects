using System;
using JetBrains.Annotations;

namespace ContainerImplementation
{
    internal sealed class ListParameterInfo : ParameterInfo
    {
        public ListParameterInfo([NotNull] Type ownerType, [NotNull] Type itemType) : base(ownerType, itemType, true)
        {
        }
    }
}