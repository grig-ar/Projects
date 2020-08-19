using CommandTypes;
using System;
using System.Text;

namespace ContainerApp
{
    [Serializable]
    internal class StudentNote : Note
    {
        public int GroupNumber { get; }

        public StudentNote(string name, int groupNumber)
        {
            Name = name;
            GroupNumber = groupNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student name: ").AppendLine(Name).Append("Student group: ").AppendLine(GroupNumber.ToString()).Append("------");
            return stringBuilder.ToString();
        }
    }
}
