using NUnit.Framework;
using Notebook.NoteCreators;
using Notebook.UserInteraction;
using NSubstitute;

namespace NotebookProjectTests
{
    [TestFixture]
    class NoteCreatorsTests
    {
        [Test]
        public void PhoneNoteCreatorTests()
        {
            var userInput = Substitute.For<IUserInput>();
            userInput.TryGetString(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            {
                x[1] = "qwerty";
                return true;
            });
            userInput.TryGetInt(Arg.Any<string>(), out Arg.Any<int?>()).Returns(x =>
            {
                x[1] = 123456;
                return true;
            });
            var phoneNoteCreator = new PhoneNoteCreator(userInput);
            Assert.NotNull(phoneNoteCreator);
            Assert.AreEqual(true, phoneNoteCreator.TryCreateNote(out var note));
            Assert.NotNull(note);
            //fields
        }

        [Test]
        public void StudentNoteCreatorTests()
        {
            var userInput = Substitute.For<IUserInput>();
            userInput.TryGetString(Arg.Any<string>(), out Arg.Any<string>()).Returns(x =>
            {
                x[1] = "qwerty";
                return true;
            });
            userInput.TryGetInt(Arg.Any<string>(), out Arg.Any<int?>()).Returns(x =>
            {
                x[1] = 123456;
                return true;
            });
            var studentNoteCreator = new StudentNoteCreator(userInput);
            Assert.NotNull(studentNoteCreator);
            Assert.AreEqual(studentNoteCreator.TryCreateNote(out var note), true);
            Assert.NotNull(note);
        }
    }
}