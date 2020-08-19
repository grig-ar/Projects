using System;
using System.Text;
using GuardUtils;
using JetBrains.Annotations;

namespace Notebook.Notes
{
    [Serializable]
    public sealed class PhoneNote : IPhoneNote
    {
        public string Name { get; }
        public string PhoneNumber { get; }

        public PhoneNote([NotNull] string name, [NotNull] string phoneNumber)
        {
            ThrowIf.Variable.IsNull(name, nameof(name));
            ThrowIf.Variable.IsNull(phoneNumber, nameof(phoneNumber));

            Name = name;
            PhoneNumber = phoneNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student name: ").AppendLine(Name).Append("Student phone: ").AppendLine(PhoneNumber)
                .Append("------");
            return stringBuilder.ToString();
        }
    }
}