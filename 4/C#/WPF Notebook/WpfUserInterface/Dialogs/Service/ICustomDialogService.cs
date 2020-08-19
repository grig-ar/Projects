using ContainerInterface;

namespace WpfUserInterface.Dialogs.Service
{
    public interface ICustomDialogService : IComponent
    {
        T OpenDialog<T>(DialogViewModelBase<T> viewModel);
    }
}