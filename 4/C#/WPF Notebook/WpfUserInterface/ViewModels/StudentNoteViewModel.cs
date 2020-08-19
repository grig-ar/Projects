using ContainerInterface;
using GalaSoft.MvvmLight;
using WpfUserInterface.Models;
using RelayCommand = GalaSoft.MvvmLight.CommandWpf.RelayCommand;

namespace WpfUserInterface.ViewModels
{
    [Component]
    public sealed class StudentNoteViewModel : ViewModelBase, IPageViewModel
    {
        private string _caption;

        //private bool _isCaptionValid;
        private int _groupNumber;
        //private bool _isGroupNumberValid;

        //private RelayCommand _addCommand;

        //public RelayCommand AddCommand
        //{
        //    get
        //    {
        //        return _addCommand ?? (_addCommand = new RelayCommand(() =>
        //        {
        //            INote note = new StudentNote(Caption, GroupNumber);
        //            Caption = "";
        //            GroupNumber = 0;
        //        }, !string.IsNullOrEmpty(Caption)));
        //    }
        //}

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

        public string Name => "Student Note";
        public bool TryCreateNote(out INote note)
        {
            if (string.IsNullOrEmpty(Caption))
            {
                note = null;
                return false;
            }

            note = new StudentNote(Caption, GroupNumber);
            Caption = "";
            GroupNumber = 0;
            return true;
        }
    }
}