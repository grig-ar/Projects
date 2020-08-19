using System;
using System.Collections.Generic;
using ContainerInterface;
using JetBrains.Annotations;
using Notebook.UserInteraction;

namespace Notebook.Notebook
{
    [Component]
    public sealed class Notebook : INotebook
    {
        [NotNull] [ItemNotNull] private readonly List<INote> _notes = new List<INote>();
        public IReadOnlyList<INote> Notes => new List<INote>(_notes);

        [NotNull] private readonly IUserOutput _userOutput = new UserOutput();

        public void Add(INote note)
        {
            _notes.Add(note);
        }

        public void Remove(INote note)
        {
            _notes.Remove(note);
        }

        public void RemoveAllNotes()
        {
            _notes.Clear();
        }

        public void Print()
        {
            foreach (var note in _notes)
            {
                _userOutput.WriteMessage(note.ToString());
            }
        }
    }
}