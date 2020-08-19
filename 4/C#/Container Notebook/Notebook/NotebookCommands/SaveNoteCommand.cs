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
    public sealed class SaveNoteCommand : ICommand
    {
        [NotNull] private readonly INotebook _notebook;
        [NotNull] private readonly IUserFileInput _userFileInput;
        [NotNull] private readonly IFileSaver _fileSaver;
        public string Name => "SaveNotes";
        public string CommandInfo => "saves notes to file";

        public SaveNoteCommand([NotNull] INotebook notebook, [NotNull] IUserFileInput userFileInput,
            [NotNull] IFileSaver fileSaver)
        {
            ThrowIf.Variable.IsNull(notebook, nameof(notebook));
            ThrowIf.Variable.IsNull(userFileInput, nameof(userFileInput));
            ThrowIf.Variable.IsNull(fileSaver, nameof(fileSaver));

            _notebook = notebook;
            _userFileInput = userFileInput;
            _fileSaver = fileSaver;
        }


        public void Execute()
        {
            if (!_userFileInput.TryGetFileName("save", out var userFileInput))
            {
                return;
            }

            _fileSaver.TrySaveFile(userFileInput, _notebook);
        }
    }
}