using System.Windows;
using ContainerInterface;
using Microsoft.Win32;

namespace WpfUserInterface.Dialogs.Service
{
    [Component]
    public class DialogService : IDialogService
    {
        public string FilePath { get; set; }

        public bool OpenFileDialog()
        {
            var openFileDialog = new OpenFileDialog
            {
                Filter = "Notebook files (*.notebook)|*.notebook|All files (*.*)|*.*",
                Title = "Select a notebook file to load",
                InitialDirectory = @"c:\Users\Артем\source\repos\WpfNotebook\WpfUserInterface\Saves\"
            };
            if (openFileDialog.ShowDialog() != true)
            {
                return false;
            }

            FilePath = openFileDialog.FileName;
            return true;
        }

        public bool SaveFileDialog()
        {
            var saveFileDialog = new SaveFileDialog()
            {
                Filter = "Notebook files (*.notebook)|*.notebook|All files (*.*)|*.*",
                Title = "Save",
                InitialDirectory = @"c:\Users\Артем\source\repos\WpfNotebook\WpfUserInterface\Saves\"
            };
            if (saveFileDialog.ShowDialog() != true)
            {
                return false;
            }

            FilePath = saveFileDialog.FileName;
            return true;
        }

        public void ShowMessage(string message, string caption = "Info", MessageBoxButton button = MessageBoxButton.OK,
            MessageBoxImage image = MessageBoxImage.None)
        {
            MessageBox.Show(message, caption, button, image);
        }
    }
}