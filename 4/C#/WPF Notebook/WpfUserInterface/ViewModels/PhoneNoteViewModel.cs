using ContainerInterface;
using GalaSoft.MvvmLight;
using WpfUserInterface.Models;

namespace WpfUserInterface.ViewModels
{
    [Component]
    public sealed class PhoneNoteViewModel : ViewModelBase, IPageViewModel
    {
        private string _caption;
        private string _phoneNumber;

        public string Caption
        {
            get => _caption;
            set => Set(ref _caption, value);
        }

        public string PhoneNumber
        {
            get => _phoneNumber;
            set => Set(ref _phoneNumber, value);
        }

        public string Name => "Phone Note";

        public bool TryCreateNote(out INote note)
        {
            if (string.IsNullOrEmpty(Caption))
            {
                note = null;
                return false;
            }

            if (string.IsNullOrEmpty(PhoneNumber))
            {
                note = null;
                return false;
            }

            note = new PhoneNote(Caption, PhoneNumber);
            Caption = "";
            PhoneNumber = "";
            return true;
        }
    }
}