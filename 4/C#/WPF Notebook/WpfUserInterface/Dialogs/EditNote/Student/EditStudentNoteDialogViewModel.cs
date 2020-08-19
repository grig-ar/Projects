using GalaSoft.MvvmLight.CommandWpf;
using WpfUserInterface.Dialogs.Service;
using WpfUserInterface.Models;

namespace WpfUserInterface.Dialogs.EditNote.Student
{
    public sealed class EditStudentNoteDialogViewModel : DialogViewModelBase<INote>
    {
        private string _caption;

        private int _groupNumber;

        private RelayCommand<IDialogWindow> _editNoteCommand;

        public string Caption
        {
            get => _caption;
            set => Set(ref _caption, value);
        }

        public int GroupNumber
        {
            get => _groupNumber;
            set => Set(ref _groupNumber, value);
        }

        public RelayCommand<IDialogWindow> EditNoteCommand =>
            _editNoteCommand ?? (_editNoteCommand = new RelayCommand<IDialogWindow>(EditNote));

        public EditStudentNoteDialogViewModel(StudentNote note, string caption) : base(caption, string.Empty)
        {
            Caption = note.Caption;
            GroupNumber = note.GroupNumber;
        }

        private void EditNote(IDialogWindow window)
        {
            if (!string.IsNullOrEmpty(Caption))
            {
                var result = new StudentNote(Caption, GroupNumber);
                Caption = "";
                GroupNumber = 0;
                CloseDialogWithResult(window, result);
            }
            else
            {
                CloseDialogWithResult(window, null);
            }
        }
    }
}