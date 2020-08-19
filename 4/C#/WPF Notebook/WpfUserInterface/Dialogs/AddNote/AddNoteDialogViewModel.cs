using System.Collections.Generic;
using System.Linq;
using GalaSoft.MvvmLight.CommandWpf;
using WpfUserInterface.Dialogs.Service;
using WpfUserInterface.Models;
using WpfUserInterface.ViewModels;

namespace WpfUserInterface.Dialogs.AddNote
{
    public sealed class AddNoteDialogViewModel : DialogViewModelBase<INote>
    {
        private IPageViewModel _currentPageViewModel;

        private List<IPageViewModel> _pageViewModels;

        private RelayCommand<IDialogWindow> _addNoteCommand;

        private RelayCommand _changePageCommand;

        public List<IPageViewModel> PageViewModels => _pageViewModels ?? (_pageViewModels = new List<IPageViewModel>());

        public IPageViewModel CurrentPageViewModel
        {
            get => _currentPageViewModel;
            set => Set(ref _currentPageViewModel, value);
        }

        public RelayCommand<IDialogWindow> AddNoteCommand =>
            _addNoteCommand ?? (_addNoteCommand = new RelayCommand<IDialogWindow>(AddNote));

        public AddNoteDialogViewModel(List<IPageViewModel> models) : base("Creation", "")
        {
            foreach (var model in models)
            {
                PageViewModels.Add(model);
            }
        }

        public RelayCommand ChangePageCommand
        {
            get
            {
                return _changePageCommand ?? (_changePageCommand =
                           new RelayCommand(() => { ChangeViewModel(CurrentPageViewModel); },
                               () => CurrentPageViewModel != null));
            }
        }

        private void ChangeViewModel(IPageViewModel viewModel)
        {
            if (!PageViewModels.Contains(viewModel))
            {
                PageViewModels.Add(viewModel);
            }

            CurrentPageViewModel = PageViewModels.FirstOrDefault(vm => vm == viewModel);
        }

        private void AddNote(IDialogWindow window)
        {
            var result = CurrentPageViewModel.TryCreateNote(out var note);
            CloseDialogWithResult(window, note);
        }
    }
}