using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Windows;
using ContainerInterface;
using GuardUtils;
using JetBrains.Annotations;
using NotebookMVVM.FileInteraction;
using WpfUserInterface.Models;

namespace WpfUserInterface.Dialogs.Service
{
    [Component]
    public sealed class FileLoaderService : IFileLoaderService
    {
        private readonly IDialogService _dialogService;

        public FileLoaderService(IDialogService dialogService)
        {
            _dialogService = dialogService;
        }

        [MustUseReturnValue]
        public bool TryLoadFile(string fileName, IList<INote> notes)
        {
            try
            {
                using (var fileStream = new FileStream(fileName, FileMode.Open))
                {
                    var items = BinaryFileUtils.Deserialize<IList<INote>>(fileStream);
                    ThrowIf.Variable.IsNull(items, nameof(items));

                    var itemsToRemove = notes.ToList();
                    foreach (var item in itemsToRemove)
                    {
                        notes.Remove(item);
                    }
                    foreach (var item in items)
                    {
                        notes.Add(item);
                    }
                }

                _dialogService.ShowMessage("Successfully loaded!");
                return true;
            }
            catch (SerializationException ex)
            {
                _dialogService.ShowMessage("Failed to deserialize. " + ex.Message, "Error", MessageBoxButton.OK,
                    MessageBoxImage.Error);
                return false;
            }
            catch (FileNotFoundException)
            {
                _dialogService.ShowMessage($"File {fileName} doesn't exist", "Error", MessageBoxButton.OK,
                    MessageBoxImage.Error);
                return false;
            }
        }
    }
}