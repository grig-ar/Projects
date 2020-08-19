using GalaSoft.MvvmLight;

namespace WpfUserInterface.ViewModels
{
    public sealed class CaptionViewModel : ViewModelBase
    {
        private string _caption;
        private bool _isValid;

        public string Caption
        {
            get => _caption;

            set
            {
                Set(ref _caption, value);
                SetIsValid();
            }
        }

        public bool IsValid
        {
            get => _isValid;

            set => Set(ref _isValid, value);
        }

        private void SetIsValid()
        {
            IsValid = !string.IsNullOrEmpty(Caption);
        }
    }
}