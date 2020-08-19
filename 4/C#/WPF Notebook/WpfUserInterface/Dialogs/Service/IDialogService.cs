using System.Windows;
using ContainerInterface;

namespace WpfUserInterface.Dialogs.Service
{
    public interface IDialogService : IComponent
    {
        void ShowMessage(string message, string caption = "Info", MessageBoxButton button = MessageBoxButton.OK,
            MessageBoxImage image = MessageBoxImage.None);

        string FilePath { get; set; }
        bool OpenFileDialog();
        bool SaveFileDialog();
    }
}