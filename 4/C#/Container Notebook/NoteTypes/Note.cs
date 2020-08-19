using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NoteTypes
{
    [Serializable]
    public abstract class Note
    {
        public string Name { get; set; }
    }
}
