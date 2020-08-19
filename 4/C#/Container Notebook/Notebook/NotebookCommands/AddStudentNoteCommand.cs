//using ContainerInterface;
//using Notebook.Commands;
//using Notebook.Notebook;
//using Notebook.Notes;
//using Notebook.UserInteraction;

//namespace Notebook.NotebookCommands
//{
//    [Component]
//    public sealed class AddStudentNoteCommand : ICommand
//    {
//        private readonly INotebook _notebook;
//        private readonly IUserInput _userInput;

//        public string Name => "AddStudentNote";
//        public string CommandInfo => "add student note to notebook";

//        public AddStudentNoteCommand(INotebook notebook, IUserInput userInput)
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

//            if (!_userInput.TryGetInt("Student group:", out var groupNumber))
//            {
//                return;
//            }

//            if (groupNumber != null)
//            {
//                _notebook.Add(new StudentNote(name, (int) groupNumber));
//            }
//        }
//    }
//}