//using System;
//using System.Collections.Generic;
//using System.Collections.ObjectModel;
//using System.Linq;
//using System.Text;
//using NUnit.Framework;
//using Notebook;
//using System.Threading.Tasks;
//using Notebook.Commands;
//using Notebook.Notebook;
//using Notebook.NotebookCommands;
//using Notebook.NoteCreators;

//namespace NotebookProjectTests
//{
//    [TestFixture]
//    public class CommandsTests
//    {
//        [Test]
//        public void CommandProcessorTest()
//        {
//            var notebook = new Notebook.Notebook.Notebook();
//            var userInput = new Notebook.UserInteraction.UserInput();
//            var userFileInput = new Notebook.UserInteraction.UserFileInput();
//            var userOutput = new Notebook.UserInteraction.UserOutput();
//            var fileLoader = new Notebook.FileInteraction.FileLoader(userOutput);
//            var fileSaver = new Notebook.FileInteraction.FileSaver(userOutput);
//            var creators = new List<INoteCreator>
//            {
//                new PhoneNoteCreator(userInput),
//                new StudentNoteCreator(userInput)
//            };
//            var commands = new List<ICommand>
//            {
//                new AddNoteCommand(notebook, userInput, creators, userOutput),
//                new LoadNoteCommand(notebook, userFileInput, userOutput, fileLoader),
//                new SaveNoteCommand(notebook, userFileInput, fileSaver),
//                new PrintCommand(notebook)
//            };

//            var commandProcessor = new CommandProcessor(userInput, userOutput, commands);
//            Assert.NotNull(commandProcessor);
//        }
//    }
//}