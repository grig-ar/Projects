using System;
using System.Text;
using GuardUtils;
using JetBrains.Annotations;

namespace Notebook.Notes
{
    [Serializable]
    public sealed class StudentNote : IStudentNote
    {
        public string Name { get; }
        public int GroupNumber { get; }

        public StudentNote([NotNull] string name, int groupNumber)
        {
            ThrowIf.Variable.IsNull(name, nameof(name));

            Name = name;
            GroupNumber = groupNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student name: ").AppendLine(Name).Append("Student group: ")
                .AppendLine(GroupNumber.ToString()).Append("------");
            return stringBuilder.ToString();
        }
    }
}