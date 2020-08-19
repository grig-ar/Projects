using System;
using Notebook.Notebook;
using Notebook.Notes;
using NUnit.Framework;
using NSubstitute;
using NSubstitute.Core.Arguments;

namespace NotebookProjectTests
{
    [TestFixture]
    class NotebookTests
    {
        [Test]
        public void NotebookTest()
        {
            var notebook = Substitute.For<INotebook>();
            var note = Substitute.For<INote>();
            var counter = 0;
            notebook.When(x => x.Add(null)).Do(x => throw new ArgumentNullException());
            notebook.When(x => x.Add(Arg.Any<INote>())).Do(x => counter++);
            notebook.When(x => x.Remove(Arg.Any<INote>())).Do(x => counter--);
            notebook.Add(note);
            notebook.Remove(note);
            Assert.AreEqual(0, counter);
            Assert.Throws<ArgumentNullException>(() => notebook.Add(null));
        }
    }
}