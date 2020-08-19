using System;
using System.Text;

namespace Lab1
{
    [Serializable]
    class StudentNote : Note
    {
        public int GroupNumber { get; set; }

        public StudentNote(string name, int groupNumber)
        {
            Name = name;
            GroupNumber = groupNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student name: ").Append(Name).Append("\nStudent group: ").Append(GroupNumber.ToString()).Append("\n------");
            return stringBuilder.ToString();
        }
    }
}
