using System;
using System.Text;
using GuardUtils;
using JetBrains.Annotations;

namespace WpfUserInterface.Models
{
    [Serializable]
    public sealed class PhoneNote : IPhoneNote
    {
        [NotNull] private string _caption;
        [NotNull] private string _phoneNumber;

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

        public string PhoneNumber
        {
            get => _phoneNumber;
            set
            {
                if (_phoneNumber == value)
                {
                    return;
                }

                _phoneNumber = value;
            }
        }

        public PhoneNote([NotNull] string caption, [NotNull] string phoneNumber)
        {
            ThrowIf.Variable.IsNull(caption, nameof(caption));
            ThrowIf.Variable.IsNull(phoneNumber, nameof(phoneNumber));

            Caption = caption;
            PhoneNumber = phoneNumber;
        }

        public override string ToString()
        {
            var stringBuilder = new StringBuilder();
            stringBuilder.Append("Student caption: ").AppendLine(Caption).Append("Student phone: ")
                .AppendLine(PhoneNumber)
                .Append("------");
            return stringBuilder.ToString();
        }
    }
}