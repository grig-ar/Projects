using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Reflection;
using System.Windows.Forms;
using CommandTypes;

namespace ContainerApp
{
    public class Saver
    {

        public Saver()
        {

        }

        public void Save(List<Note> list)
        {
            SaveFileDialog dlg = new SaveFileDialog
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

            BinaryFormatter formatter = new BinaryFormatter();
            try
            {
                using (var fileStream = new FileStream(dlg.FileName, FileMode.Create))
                {
                    formatter.Serialize(fileStream, list);
                }
                Console.WriteLine("Notebook saved successfully!");
            }
            catch (SerializationException ex)
            {
                Console.WriteLine("Failed to serialize. " + ex.Message);
            }
        }
    }
}
