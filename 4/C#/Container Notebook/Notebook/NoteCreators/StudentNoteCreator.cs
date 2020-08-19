using ContainerInterface;
using GuardUtils;
using JetBrains.Annotations;
using Notebook.Notebook;
using Notebook.Notes;
using Notebook.UserInteraction;

namespace Notebook.NoteCreators
{
    [Component]
    public sealed class StudentNoteCreator : INoteCreator
    {
        [NotNull] private readonly IUserInput _userInput;

        public StudentNoteCreator([NotNull] IUserInput userInput)
        {
            ThrowIf.Variable.IsNull(userInput, nameof(userInput));

            _userInput = userInput;
        }

        public string NoteType => "StudentNote";
        public string Info => "add student note to notebook (name + group)";

        public bool TryCreateNote(out INote note)
        {
            if (!_userInput.TryGetString("Student name:", out var name))
            {
                note = null;
                return false;
            }

            if (!_userInput.TryGetInt("Student group:", out var groupNumber))
            {
                note = null;
                return false;
            }

            if (groupNumber != null)
            {
                note = new StudentNote(name, (int) groupNumber);
                return true;
            }

            note = null;
            return false;
        }
    }
}