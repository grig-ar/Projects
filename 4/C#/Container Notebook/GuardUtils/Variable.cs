using System.Runtime.CompilerServices;
using JetBrains.Annotations;

namespace GuardUtils
{
    public sealed class Variable
    {
        [ContractAnnotation("variable:null => halt")]
        [MethodImpl(MethodImplOptions.AggressiveInlining)]
        public void IsNull<T>([CanBeNull] T variable, [NotNull] string variableName)
            where T : class
        {
            if (variable != null)
            {
                return;
            }

            ThrowHelper.ThrowVariableNullException(variableName);
        }
    }
}