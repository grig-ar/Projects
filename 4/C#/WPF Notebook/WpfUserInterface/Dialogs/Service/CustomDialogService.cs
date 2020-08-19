using ContainerInterface;

namespace WpfUserInterface.Dialogs.Service
{
    [Component]
    public class CustomDialogService : ICustomDialogService
    {
        public T OpenDialog<T>(DialogViewModelBase<T> viewModel)
        {
            IDialogWindow window = new DialogWindow
            {
                DataContext = viewModel
            };
            window.ShowDialog();
            return viewModel.DialogResult;
        }
    }
}