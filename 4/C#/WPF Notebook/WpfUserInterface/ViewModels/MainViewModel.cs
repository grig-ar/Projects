using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using ContainerInterface;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.CommandWpf;
using WpfUserInterface.Dialogs.AddNote;
using WpfUserInterface.Dialogs.EditNote.Phone;
using WpfUserInterface.Dialogs.EditNote.Student;
using WpfUserInterface.Dialogs.Service;
using WpfUserInterface.Models;

namespace WpfUserInterface.ViewModels
{
    [Component]
    public class MainViewModel : ViewModelBase, IMainViewModel
    {
        private ObservableCollection<INote> _notes = new ObservableCollection<INote>();

        private INote _selectedNote;

        private string _textFilter;

        private IEnumerable _filteredNotes;

        private RelayCommand _downloadCommand;

        private RelayCommand _removeItemCommand;

        private RelayCommand _helpCommand;

        private RelayCommand _saveCommand;

        private RelayCommand _loadCommand;

        private RelayCommand _addNoteCommand;

        private RelayCommand _editNoteCommand;

        private readonly IDialogService _dialogService;

        private readonly IFileSaverService _fileSaverService;

        private readonly IFileLoaderService _fileLoaderService;

        private readonly ICustomDialogService _customDialogService;

        public MainViewModel(IDialogService dialogService, IFileSaverService fileSaverService,
            IFileLoaderService fileLoaderService, ICustomDialogService customDialogService)
        {
            _dialogService = dialogService;
            _fileSaverService = fileSaverService;
            _fileLoaderService = fileLoaderService;
            _customDialogService = customDialogService;
            StudentNoteViewModel = new StudentNoteViewModel();
            PhoneNoteViewModel = new PhoneNoteViewModel();
        }

        public IPageViewModel StudentNoteViewModel { get; }
        public IPageViewModel PhoneNoteViewModel { get; }

        public INote SelectedNote
        {
            get => _selectedNote;
            set => Set(ref _selectedNote, value);
        }

        public string TextFilter
        {
            get => _textFilter;
            set
            {
                Set(ref _textFilter, value);
                FilteredNotes = Notes.Where(x =>
                    string.IsNullOrWhiteSpace(_textFilter) || x.Caption.ToLower().Contains(_textFilter.ToLower()));
            }
        }

        public ObservableCollection<INote> Notes
        {
            get => _notes;
            set => Set(ref _notes, value);
        }

        public IEnumerable FilteredNotes
        {
            get => _filteredNotes;

            set => Set(ref _filteredNotes, value);
        }


        public RelayCommand EditNoteCommand
        {
            get
            {
                return _editNoteCommand ?? (_editNoteCommand = new RelayCommand(() =>
                {
                    if (SelectedNote == null)
                    {
                        return;
                    }

                    DialogViewModelBase<INote> dialog;
                    switch (SelectedNote)
                    {
                        case PhoneNote phoneNote:
                            dialog = new EditPhoneNoteDialogViewModel(phoneNote, "Edit Phone Note");
                            break;
                        case StudentNote studentNote:
                            dialog = new EditStudentNoteDialogViewModel(studentNote, "Edit Student Note");
                            break;
                        default:
                            dialog = null;
                            break;
                    }

                    if (dialog == null)
                    {
                        return;
                    }

                    var result = _customDialogService.OpenDialog(dialog);
                    if (result == null)
                    {
                        return;
                    }

                    var index = Notes.IndexOf(SelectedNote);
                    Notes[index] = result;
                    SelectedNote = result;
                    FilteredNotes = Notes.Where(x =>
                        string.IsNullOrWhiteSpace(_textFilter) || x.Caption.Contains(_textFilter));
                }));
            }
        }

        public RelayCommand AddNoteCommand
        {
            get
            {
                return _addNoteCommand ?? (_addNoteCommand = new RelayCommand(() =>
                {
                    var models = new List<IPageViewModel> {PhoneNoteViewModel, StudentNoteViewModel};
                    var dialog = new AddNoteDialogViewModel(models);
                    var result = _customDialogService.OpenDialog(dialog);
                    if (result == null)
                    {
                        return;
                    }

                    Notes.Add(result);
                    FilteredNotes = Notes.Where(x =>
                        string.IsNullOrWhiteSpace(_textFilter) || x.Caption.Contains(_textFilter));
                }));
            }
        }

        public RelayCommand LoadCommand =>
            _loadCommand ?? (_loadCommand = new RelayCommand(() =>
            {
                var result = _dialogService.OpenFileDialog();
                if (!result)
                {
                    return;
                }

                _fileLoaderService.TryLoadFile(_dialogService.FilePath, Notes);
                FilteredNotes = Notes.Where(x =>
                    string.IsNullOrWhiteSpace(_textFilter) || x.Caption.Contains(_textFilter));
            }));

        public RelayCommand SaveCommand
        {
            get
            {
                return _saveCommand ?? (_saveCommand = new RelayCommand(() =>
                {
                    var result = _dialogService.SaveFileDialog();
                    if (result)
                    {
                        _fileSaverService.TrySaveFile(_dialogService.FilePath, Notes);
                    }
                }));
            }
        }

        public RelayCommand HelpCommand
        {
            get
            {
                return _helpCommand ?? (_helpCommand = new RelayCommand(() =>
                {
                    _dialogService.ShowMessage(
                        "Author: Grigorovich Artyom; NSU, FIT\nGroup: 16203\nTask: WPF interface\nVersion: 0.0.1\n");
                }));
            }
        }

        public RelayCommand RemoveItemCommand
        {
            get
            {
                return _removeItemCommand ??
                       (_removeItemCommand = new RelayCommand(() =>
                       {
                           Notes.Remove(SelectedNote);
                           FilteredNotes = Notes.Where(x =>
                               string.IsNullOrWhiteSpace(_textFilter) || x.Caption.Contains(_textFilter));
                           SelectedNote = null;
                       }, () => SelectedNote != null));
            }
        }

        public RelayCommand DownloadCommand
        {
            get
            {
                return _downloadCommand ?? (_downloadCommand = new RelayCommand(async () =>
                {
                    var items = new List<INote>
                    {
                        new PhoneNote("Student abc", "888-777"),
                        new PhoneNote("Student def", "888-777-999"),
                        new StudentNote("Student jhi", 111),
                        new StudentNote("Student jkl", 222)
                    };
                    Notes = new ObservableCollection<INote>(items);
                    FilteredNotes = Notes.Where(x =>
                        string.IsNullOrWhiteSpace(_textFilter) || x.Caption.Contains(_textFilter));
                }));
            }
        }

        public string Name => "Main Page";
    }
}