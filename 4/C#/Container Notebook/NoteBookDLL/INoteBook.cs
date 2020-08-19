using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NoteTypes;

namespace NoteBookDLL
{
    public interface INoteBook
    {
        void Add(Note note);

        void Find();

        void Help();

        void Print();

    }

    [AttributeUsage(AttributeTargets.Class)]
    public sealed class ApplicationAttribute : Attribute
    {
        public string ApplicationInfo { get; set; }
    }

}
