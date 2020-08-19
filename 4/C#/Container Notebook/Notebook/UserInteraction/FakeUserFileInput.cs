using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Notebook.UserInteraction
{
    public sealed class FakeUserFileInput : IUserFileInput
    {
        public bool TryGetFileName(string type, out string userFileInput)
        {
            userFileInput = "c:\\Users\\Артем\\source\\repos\\Container\\NotebookProjectTests\\Saves\\111.notebook";
            return true;
        }
    }
}
