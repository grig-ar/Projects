using System;

namespace ContainerInterface
{
    [AttributeUsage(AttributeTargets.Class, Inherited = false)]
    public sealed class ComponentAttribute : Attribute
    {
    }
}