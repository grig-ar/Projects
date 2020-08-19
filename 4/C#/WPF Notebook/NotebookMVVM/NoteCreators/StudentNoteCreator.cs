//using ContainerInterface;
//using GuardUtils;
//using JetBrains.Annotations;
//using NotebookMVVM.Notebook;
//using NotebookMVVM.Notes;

//namespace NotebookMVVM.NoteCreators
//{
//    [Component]
//    public sealed class StudentNoteCreator : INoteCreator
//    {
//        //public StudentNoteCreator([NotNull] IUserInput userInput)
//        //{
//        //    ThrowIf.Variable.IsNull(userInput, nameof(userInput));

//        //    _userInput = userInput;
//        //}

//        public string NoteType => "StudentNote";
//        public string Info => "add student note to notebook (name + group)";

//        public INote TryCreateNote(string name, int groupNumber)
//        {
//            return new StudentNote(name, groupNumber);
            
//        }
//    }
//}