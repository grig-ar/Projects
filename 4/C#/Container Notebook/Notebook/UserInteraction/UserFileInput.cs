using System;
using System.IO;
using System.Reflection;
using System.Windows.Forms;
using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.UserInteraction
{
    [Component]
    public sealed class UserFileInput : IUserFileInput
    {
        [MustUseReturnValue]
        public bool TryGetFileName(string type, out string userFileInput)
        {
            if (type.Equals("save", StringComparison.OrdinalIgnoreCase))
            {
                var dlg = new SaveFileDialog
                {
                    InitialDirectory = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location),
                    Filter = "notebooks (*.notebook)|*.notebook|All files (*.*)|*.*",
                    FilterIndex = 1
                };

                if (dlg.ShowDialog() != DialogResult.OK)
                {
                    userFileInput = null;
                    return false;
                }

                userFileInput = dlg.FileName;
                return true;
            }

            if (type.Equals("open", StringComparison.OrdinalIgnoreCase))
            {
                var dlg = new OpenFileDialog
                {
                    InitialDirectory = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location),
                    Filter = "notebooks (*.notebook)|*.notebook|All files (*.*)|*.*",
                    FilterIndex = 1
                };

                if (dlg.ShowDialog() != DialogResult.OK)
                {
                    userFileInput = null;
                    return false;
                }

                userFileInput = dlg.FileName;
                return true;
            }

            userFileInput = null;
            return false;
        }
    }
}