using System.Collections.Generic;
using System.IO;
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
    public sealed class FileSaverService : IFileSaverService
    {
        private readonly IDialogService _dialogService;

        public FileSaverService(IDialogService dialogService)
        {
            ThrowIf.Variable.IsNull(dialogService, nameof(dialogService));
            _dialogService = dialogService;
        }

        [MustUseReturnValue]
        public bool TrySaveFile(string fileName, IList<INote> notes)
        {
            var stream = BinaryFileUtils.Serialize(notes);
            if (fileName.Length <= 0)
            {
                stream.Dispose();
                return false;
            }

            try
            {
                using (var fileStream = new FileStream(fileName, FileMode.Create))
                {
                    stream.Position = 0;
                    stream.CopyTo(fileStream);
                }

                _dialogService.ShowMessage("Notebook saved successfully!");
                stream.Dispose();
                return true;
            }
            catch (SerializationException ex)
            {
                _dialogService.ShowMessage("Failed to serialize. " + ex.Message, "Error", MessageBoxButton.OK,
                    MessageBoxImage.Error);
                stream.Dispose();
                return false;
            }
        }
    }
}