using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NoteTypes;

namespace PhoneNoteDLL
{
    [Serializable]
    public class PhoneNote : Note
    {
        public string PhoneNumber { get; }

        public PhoneNote(string name, string phoneNumber)
        {
            Name = name;
            PhoneNumber = phoneNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student name: ").AppendLine(Name).Append("Student phone: ").AppendLine(PhoneNumber).Append("------");
            return stringBuilder.ToString();
        }
    }
}
