using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NoteTypes;

namespace StudentNoteDLL
{
    [Serializable]
    public class StudentNote : Note
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