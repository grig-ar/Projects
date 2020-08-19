using GalaSoft.MvvmLight.CommandWpf;
using WpfUserInterface.Dialogs.Service;
using WpfUserInterface.Models;

namespace WpfUserInterface.Dialogs.EditNote.Phone
{
    public sealed class EditPhoneNoteDialogViewModel : DialogViewModelBase<INote>
    {
        private string _caption;

        private string _phoneNumber;

        private RelayCommand<IDialogWindow> _editNoteCommand;

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

        public RelayCommand<IDialogWindow> EditNoteCommand =>
            _editNoteCommand ?? (_editNoteCommand = new RelayCommand<IDialogWindow>(EditNote));

        public EditPhoneNoteDialogViewModel(PhoneNote note, string caption) : base(caption, string.Empty)
        {
            Caption = note.Caption;
            PhoneNumber = note.PhoneNumber;
        }

        private void EditNote(IDialogWindow window)
        {
            if (!string.IsNullOrEmpty(Caption) && !string.IsNullOrEmpty(PhoneNumber))
            {
                var result = new PhoneNote(Caption, PhoneNumber);
                Caption = "";
                PhoneNumber = "";
                CloseDialogWithResult(window, result);
            }
            else
            {
                CloseDialogWithResult(window, null);
            }
        }
    }
}