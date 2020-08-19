using CommandTypes;
using System;
using System.Text;

namespace ContainerApp

{
    [Serializable]
    internal class PhoneNote : Note
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
