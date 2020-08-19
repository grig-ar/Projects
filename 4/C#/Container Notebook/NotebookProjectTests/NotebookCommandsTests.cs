using System;
using System.IO;
using NUnit.Framework;
using NSubstitute;
using Notebook.Commands;
using Notebook.Notebook;
using Notebook.NotebookCommands;
using Notebook.FileInteraction;
using Notebook.Notes;
using Notebook.UserInteraction;

namespace NotebookProjectTests
{
    [TestFixture]
    class NotebookCommandsTests
    {
        [Test]
        public void AddNoteCommandTest()
        {
            var notebook = Substitute.For<INotebook>();
            var note = Substitute.For<INote>();
            var userInput = Substitute.For<IUserInput>();
            userInput.TryGetString(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            {
                x[1] = "qwerty";
                return true;
            });

            var counter = 0;
            var addNoteCommand = Substitute.For<ICommand>();
            notebook.When(x => x.Add(Arg.Any<INote>())).Do(x => counter++);
            addNoteCommand.When(x => x.Execute()).Do(x => notebook.Add(note));
            addNoteCommand.Execute();
            notebook.Received().Add(note);
            Assert.AreEqual(1, counter);
        }

        [Test]
        public void LoadNoteCommandTest()
        {
            //var notebook = new Notebook.Notebook.Notebook();
            //var userFileInput = Substitute.For<IUserFileInput>();
            //userFileInput.TryGetFileName(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            //{
            //    x[1] = "c:\\Users\\Артем\\source\\repos\\Container\\NotebookProjectTests\\Saves\\111.notebook";
            //    return true;
            //});
            //var userOutput = new UserOutput();
            //var fileLoader = new FileLoader(userOutput);

            var notebook = Substitute.For<INotebook>();
            var userFileInput = Substitute.For<IUserFileInput>();
            userFileInput.TryGetFileName(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            {
                x[1] = "1";
                return true;
            });
            var userOutput = Substitute.For<IUserOutput>();
            var fileLoader = Substitute.For<IFileLoader>();

            var loadNoteCommand = new LoadNoteCommand(notebook, userFileInput, userOutput, fileLoader);
            loadNoteCommand.Execute();
            fileLoader.Received().TryLoadFile("1", notebook);

            //Assert.AreEqual(3, notebook.Notes.Count);
        }

        [Test]
        public void SaveNoteCommandTest()
        {
            var notebook = Substitute.For<INotebook>();
            var userFileInput = Substitute.For<IUserFileInput>();
            userFileInput.TryGetFileName(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            {
                x[1] = "1";
                return true;
            });
            var fileSaver = Substitute.For<IFileSaver>();

            var saveNoteCommand = new SaveNoteCommand(notebook, userFileInput, fileSaver);
            saveNoteCommand.Execute();
            fileSaver.Received().TrySaveFile("1", notebook);

            //var notebook = new Notebook.Notebook.Notebook();
            //var userInput = Substitute.For<IUserInput>();
            //userInput.TryGetString(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            //{
            //    x[1] = "qwerty";
            //    return true;
            //});
            //userInput.TryGetInt(Arg.Any<string>(), out Arg.Any<int?>()).Returns(x =>
            //{
            //    x[1] = 123;
            //    return true;
            //});

            //Assert.AreEqual(true, userInput.TryGetString("name", out var str));
            //Assert.AreEqual(true, userInput.TryGetInt("group", out var num));
            //Assert.NotNull(num);

            //notebook.Add(new PhoneNote(str, str));
            //notebook.Add(new PhoneNote(str, str));
            //notebook.Add(new StudentNote(str, (int) num));

            //var userFileInput = Substitute.For<IUserFileInput>();
            //userFileInput.TryGetFileName(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            //{
            //    x[1] = "c:\\Users\\Артем\\source\\repos\\Container\\NotebookProjectTests\\Saves\\111.notebook";
            //    return true;
            //});

            //var userOutput = new UserOutput();
            //var counter = 0;
            //var fileSaver = Substitute.For<IFileSaver>();
            //fileSaver.TrySaveFile(Arg.Any<string>(), Arg.Any<INotebook>()).Returns(true);
            //fileSaver.When(x => x.TrySaveFile(Arg.Any<string>(), Arg.Any<INotebook>())).Do(x => counter++);
            //var saveNoteCommand = Substitute.For<ICommand>();
            //saveNoteCommand.When(x => x.Execute()).Do(x => fileSaver.TrySaveFile("123", notebook));
            //saveNoteCommand.Execute();
            //fileSaver.Received().TrySaveFile("123", notebook);
            //Assert.AreEqual(1, counter);
        }

        [Test]
        public void PrintNotesCommand()
        {
            var notebook = new Notebook.Notebook.Notebook();
            var firstNote = new PhoneNote("abc", "222-222");
            var secondNote = new PhoneNote("qwer", "222-333");
            var thirdNote = new StudentNote("rrr", 123);

            notebook.Add(firstNote);
            notebook.Add(secondNote);
            notebook.Add(thirdNote);
            var printNotesCommand = new PrintCommand(notebook);

            using (var stringWriter = new StringWriter())
            {
                Console.SetOut(stringWriter);
                printNotesCommand.Execute();
                var expectedString = firstNote.ToString() + "\r\n" + secondNote.ToString() + "\r\n" +
                                        thirdNote.ToString() + "\r\n";
                Assert.AreEqual(true, stringWriter.ToString().Equals(expectedString));
            }

            Console.SetOut(new StreamWriter(Console.OpenStandardError()));
        }
    }
}