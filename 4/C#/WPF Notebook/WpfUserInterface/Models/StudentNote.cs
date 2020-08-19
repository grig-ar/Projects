using System;
using System.Text;
using GuardUtils;
using JetBrains.Annotations;

namespace WpfUserInterface.Models
{
    [Serializable]
    public sealed class StudentNote : IStudentNote
    {
        [NotNull] private string _caption;
        private int _groupNumber;

        public string Caption
        {
            get => _caption;
            set
            {
                if (_caption == value)
                {
                    return;
                }

                _caption = value;
            }
        }

        public int GroupNumber
        {
            get => _groupNumber;
            set
            {
                if (_groupNumber == value)
                {
                    return;
                }

                _groupNumber = value;
            }
        }

        public StudentNote([NotNull] string caption, int groupNumber)
        {
            ThrowIf.Variable.IsNull(caption, nameof(caption));

            Caption = caption;
            GroupNumber = groupNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student caption: ").AppendLine(Caption).Append("Student group: ")
                .AppendLine(GroupNumber.ToString()).Append("------");
            return stringBuilder.ToString();
        }
    }
}