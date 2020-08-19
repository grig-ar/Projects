using ContainerInterface;
using GuardUtils;
using JetBrains.Annotations;
using Notebook.Notebook;
using Notebook.Notes;
using Notebook.UserInteraction;

namespace Notebook.NoteCreators
{
    [Component]
    public sealed class PhoneNoteCreator : INoteCreator
    {
        [NotNull] private readonly IUserInput _userInput;

        public string NoteType => "PhoneNote";
        public string Info => "add phone note to notebook (name + phone number)";

        public PhoneNoteCreator([NotNull] IUserInput userInput)
        {
            ThrowIf.Variable.IsNull(userInput, nameof(userInput));

            _userInput = userInput;
        }


        public bool TryCreateNote(out INote note)
        {
            if (!_userInput.TryGetString("Student name:", out var name))
            {
                note = null;
                return false;
            }

            if (!_userInput.TryGetString("Student phone:", out var phoneNumber))
            {
                note = null;
                return false;
            }

            note = new PhoneNote(name, phoneNumber);
            return true;
        }
    }
}