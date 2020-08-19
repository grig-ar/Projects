using System.Collections.Generic;
using System.Linq;
using ContainerInterface;
using GuardUtils;
using JetBrains.Annotations;
using Notebook.Commands;
using Notebook.Notebook;
using Notebook.UserInteraction;

namespace Notebook.NotebookCommands
{
    [Component]
    public sealed class AddNoteCommand : ICommand
    {
        [NotNull] private readonly INotebook _notebook;
        [NotNull] private readonly IUserInput _userInput;
        [NotNull] private readonly IUserOutput _userOutput;
        [NotNull] private readonly IReadOnlyDictionary<string, INoteCreator> _creators;

        public AddNoteCommand([NotNull] INotebook notebook, [NotNull] IUserInput userInput,
            [NotNull] [ItemNotNull] IReadOnlyList<INoteCreator> creators,
            [NotNull] IUserOutput userOutput)
        {
            ThrowIf.Variable.IsNull(notebook, nameof(notebook));
            ThrowIf.Variable.IsNull(userInput, nameof(userInput));
            ThrowIf.Variable.IsNull(userOutput, nameof(userOutput));
            ThrowIf.Variable.IsNull(creators, nameof(creators));

            _notebook = notebook;
            _userInput = userInput;
            _creators = creators.ToDictionary(x => x.NoteType.ToLower());
            _userOutput = userOutput;
        }

        public string Name => "AddNote";
        public string CommandInfo => "adds note to notebook";

        public void Execute()
        {
            _userOutput.WriteMessage("Choose one of note types:");

            foreach (var pair in _creators)
            {
                ThrowIf.Variable.IsNull(pair.Value, nameof(pair.Value));
                _userOutput.WriteMessage($"{pair.Value.NoteType} -- {pair.Value.Info}");
            }

            if (!_userInput.TryGetString("\nEnter note type", out var userInput))
            {
                _userOutput.WriteMessage("Incorrect note type!");
            }

            if (!_creators.TryGetValue(userInput.ToLower(), out var creator))
            {
                _userOutput.WriteMessage("Incorrect note type!");
                return;
            }
            if (creator.TryCreateNote(out var note))
            {
                _notebook.Add(note);
            }
        }
    }
}