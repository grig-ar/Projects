using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CommandTypes;
using NoteBookDLL;
using NoteTypes;

namespace SaveModule
{
    [Component(CommandInfo = "saves notes to file")]
    public class Save : ICommand
    {
        private NoteBook _noteBook;

        public Save(NoteBook noteBook)
        {
            _noteBook = noteBook;
        }

        public void Execute()
        {
            var dlg = new SaveFileDialog
            {
                InitialDirectory = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location),
                Filter = "notebooks (*.notebook)|*.notebook|All files (*.*)|*.*",
                FilterIndex = 1
            };
            if (dlg.ShowDialog() != DialogResult.OK)
            {
                Console.WriteLine("User cancelled out of the save file dialog.");
                return;
            }

            var formatter = new BinaryFormatter();
            try
            {
                using (var fileStream = new FileStream(dlg.FileName, FileMode.Create))
                {
                    formatter.Serialize(fileStream, _noteBook.Notes);
                }
                Console.WriteLine("Notebook saved successfully!");
            }
            catch (SerializationException ex)
            {
                Console.WriteLine("Failed to serialize. " + ex.Message);
            }
        }

        public string GetName()
        {
            return "Save";
        }
    }
}
