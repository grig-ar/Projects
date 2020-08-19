using System;
using System.Text;

namespace Lab1
{
    [Serializable]
    class PhoneNote : Note
    {
        public string PhoneNumber { get; set; }

        public PhoneNote(string name, string phoneNumber)
        {
            Name = name;
            PhoneNumber = phoneNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student name: ").Append(Name).Append("\nStudent phone: ").Append(PhoneNumber).Append("\n------");
            return stringBuilder.ToString();
        }
    }
}
