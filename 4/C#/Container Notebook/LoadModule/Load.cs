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
using NoteTypes;
using NoteBookDLL;

namespace LoadModule
{
    [Component(CommandInfo = "tries to load notes from filename")]
    public class Load : ICommand
    {

        private NoteBook _noteBook;

        public Load(NoteBook noteBook)
        {
            _noteBook = noteBook;
        }

        public void Execute()
        {
            var dlg = new OpenFileDialog
            {
                InitialDirectory = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location),
                Filter = "notebooks (*.notebook)|*.notebook|All files (*.*)|*.*",
                FilterIndex = 1
            };
            if (dlg.ShowDialog() != DialogResult.OK)
            {
                Console.WriteLine("User cancelled out of the open file dialog.");
                return;
            }
            var formatter = new BinaryFormatter();
            try
            {
                using (var fileStream = new FileStream(dlg.FileName, FileMode.Open))
                {
                    _noteBook.Notes = (List<Note>)formatter.Deserialize(fileStream);
                }
                Console.WriteLine("Successfully loaded!");
            }
            catch (SerializationException ex)
            {
                Console.WriteLine("Failed to deserialize. " + ex.Message);
            }
            catch (FileNotFoundException)
            {
                Console.WriteLine("File {0} doesn't exist", dlg.FileName);
            }

        }

        public string GetName()
        {
            return "Load";
        }
    }
}
