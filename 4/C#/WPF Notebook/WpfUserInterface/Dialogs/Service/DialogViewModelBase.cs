using GalaSoft.MvvmLight;

namespace WpfUserInterface.Dialogs.Service
{
    public abstract class DialogViewModelBase<T> : ViewModelBase
    {
        public string Title { get; set; }

        public string Message { get; set; }

        public T DialogResult { get; set; }

        protected DialogViewModelBase() : this(string.Empty, string.Empty)
        {
        }

        protected DialogViewModelBase(string title) : this(title, string.Empty)
        {
        }

        protected DialogViewModelBase(string title, string message)
        {
            Title = title;
            Message = message;
        }

        public void CloseDialogWithResult(IDialogWindow dialog, T result)
        {
            DialogResult = result;

            if (dialog != null)
            {
                dialog.DialogResult = true;
            }
        }
    }
}