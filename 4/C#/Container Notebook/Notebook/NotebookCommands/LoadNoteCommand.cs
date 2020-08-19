using ContainerInterface;
using GuardUtils;
using JetBrains.Annotations;
using Notebook.Commands;
using Notebook.FileInteraction;
using Notebook.Notebook;
using Notebook.UserInteraction;

namespace Notebook.NotebookCommands
{
    [Component]
    public sealed class LoadNoteCommand : ICommand
    {
        [NotNull] private readonly INotebook _notebook;
        [NotNull] private readonly IUserFileInput _userFileInput;
        [NotNull] private readonly IUserOutput _userOutput;
        [NotNull] private readonly IFileLoader _fileLoader;

        public string Name => "LoadNotes";
        public string CommandInfo => "tries to load notes from filename";

        public LoadNoteCommand([NotNull] INotebook notebook, [NotNull] IUserFileInput userFileInput,
            [NotNull] IUserOutput userOutput, [NotNull] IFileLoader fileLoader)
        {
            ThrowIf.Variable.IsNull(notebook, nameof(notebook));
            ThrowIf.Variable.IsNull(userFileInput, nameof(userFileInput));
            ThrowIf.Variable.IsNull(userOutput, nameof(userOutput));
            ThrowIf.Variable.IsNull(fileLoader, nameof(fileLoader));

            _notebook = notebook;
            _userFileInput = userFileInput;
            _userOutput = userOutput;
            _fileLoader = fileLoader;
        }

        public void Execute()
        {
            if (!_userFileInput.TryGetFileName("open", out var userFileInput))
            {
                return;
            }

            _fileLoader.TryLoadFile(userFileInput, _notebook);
        }
    }
}