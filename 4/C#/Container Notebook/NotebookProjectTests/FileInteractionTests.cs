using GuardUtils;
using Notebook.Notebook;
using NUnit.Framework;
using Notebook.Notes;
using Notebook.UserInteraction;
using Notebook.FileInteraction;
using NSubstitute;

namespace NotebookProjectTests
{
    [TestFixture]
    public class FileInteractionTests
    {
        //private readonly Notebook.Notebook.Notebook _notebook = new Notebook.Notebook.Notebook();

        [Test]
        public void FileSaverLoaderTests()
        {
            var phoneNote = new PhoneNote("123", "123");
            var cloneNote = FileUtils.Clone<PhoneNote>(phoneNote);
            ThrowIf.Variable.IsNull(cloneNote, nameof(cloneNote));
            Assert.AreEqual(phoneNote.Name, cloneNote.Name);
            Assert.AreEqual(phoneNote.PhoneNumber, cloneNote.PhoneNumber);



            //ThrowIf.Variable.IsNull(_notebook, nameof(_notebook));
            //var notebook = new Notebook.Notebook.Notebook();
            //var userInput = Substitute.For<IUserInput>();
            //userInput.TryGetString(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            //{
            //    x[1] = "qwerty";
            //    return true;
            //});
            //userInput.TryGetInt(Arg.Any<string>(), out Arg.Any<int?>()).Returns(x =>
            //{
            //    x[1] = 123456;
            //    return true;
            //});
            //Assert.AreEqual(true, userInput.TryGetString("name", out var str));
            //Assert.AreEqual(true, userInput.TryGetInt("group", out var num));
            //Assert.NotNull(num);

            //_notebook.Add(new PhoneNote(str, str));
            //_notebook.Add(new PhoneNote(str, str));
            //_notebook.Add(new StudentNote(str, (int) num));


            //var userOutput = new UserOutput();
            //var userFileInput = Substitute.For<IUserFileInput>();
            //userFileInput.TryGetFileName(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            //{
            //    x[1] = "c:\\Users\\Артем\\source\\repos\\Container\\NotebookProjectTests\\Saves\\111.notebook";
            //    return true;
            //});

            //var fileSaver = new FileSaver(userOutput);
            //Assert.AreEqual(true, userFileInput.TryGetFileName("load", out var fileName));
            //Assert.AreEqual(true, fileSaver.TrySaveFile(fileName, _notebook));

            //var fileLoader = new FileLoader(userOutput);
            //Assert.AreEqual(fileLoader.TryLoadFile(fileName, notebook),
            //    true);
            //Assert.AreEqual(_notebook.Notes.Count, notebook.Notes.Count);
            //for (var i = 0; i < _notebook.Notes.Count; ++i)
            //{
            //    Assert.True(_notebook.Notes[i].Name.Equals(notebook.Notes[i].Name));
            //}
        }
    }
}