using CommandTypes;
using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Windows.Forms;

namespace ContainerApp
{
    public class Loader
    {

        public Loader()
        {

        }

        public List<Note> Load()
        {
            List<Note> notes = null;
            OpenFileDialog dlg = new OpenFileDialog
            {
                InitialDirectory = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location),
                Filter = "notebooks (*.notebook)|*.notebook|All files (*.*)|*.*",
                FilterIndex = 1
            };
            if (dlg.ShowDialog() != DialogResult.OK)
            {
                Console.WriteLine("User cancelled out of the open file dialog.");
                return notes;
            }

            BinaryFormatter formatter = new BinaryFormatter();
            try
            {
                using (var fileStream = new FileStream(dlg.FileName, FileMode.Open))
                {
                    notes = (List<Note>)formatter.Deserialize(fileStream);
                }
                Console.WriteLine();
                foreach (var note in notes)
                {
                    Console.WriteLine(note.ToString());
                };
            }
            catch (SerializationException ex)
            {
                Console.WriteLine("Failed to deserialize. " + ex.Message);
            }
            return notes;
        }
    }
}
