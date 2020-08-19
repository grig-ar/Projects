//using ContainerInterface;
//using Notebook.Commands;
//using Notebook.Notebook;
//using Notebook.Notes;
//using Notebook.UserInteraction;

//namespace Notebook.NotebookCommands
//{
//    [Component]
//    public sealed class AddPhoneNoteCommand : ICommand
//    {
//        private readonly INotebook _notebook;
//        private readonly IUserInput _userInput;

//        public string Name => "AddPhoneNote";
//        public string CommandInfo => "add phone note to notebook";

//        public AddPhoneNoteCommand(INotebook notebook, IUserInput userInput)
//        {
//            _notebook = notebook;
//            _userInput = userInput;
//        }



//        public void Execute()
//        {
//            if (!_userInput.TryGetString("Student name:", out var name))
//            {
//                return;
//            }

//            if (!_userInput.TryGetString("Student phone:", out var phoneNumber))
//            {
//                return;
//            }

//            _notebook.Add(new PhoneNote(name, phoneNumber));
//        }
//    }
//}