//using System.Collections.Generic;
//using System.IO;
//using System.Runtime.Serialization;
//using System.Runtime.Serialization.Formatters.Binary;
//using ContainerInterface;
//using GuardUtils;
//using JetBrains.Annotations;
//using NotebookMVVM.Notebook;


//namespace NotebookMVVM.FileInteraction
//{
//    [Component]
//    public sealed class FileLoader : IFileLoader
//    {
//        [NotNull] private readonly IUserOutput _userOutput;

//        public FileLoader([NotNull] IUserOutput userOutput)
//        {
//            ThrowIf.Variable.IsNull(userOutput, nameof(userOutput));

//            _userOutput = userOutput;
//        }

//        [MustUseReturnValue]
//        public bool TryLoadFile(string fileName, INotebook notebook)
//        {
//            try
//            {
//                using (var fileStream = new FileStream(fileName, FileMode.Open))
//                {
//                    notebook.RemoveAllNotes();
//                    var notes = FileUtils.Deserialize<List<INote>>(fileStream);
//                    ThrowIf.Variable.IsNull(notes, nameof(notes));
//                    //var notes = (List<INote>) formatter.Deserialize(fileStream);
//                    foreach (var note in notes)
//                    {
//                        notebook.Add(note);
//                    }
//                }

//                _userOutput.WriteMessage("Successfully loaded!");
//                return true;
//            }
//            catch (SerializationException ex)
//            {
//                _userOutput.WriteMessage("Failed to deserialize. " + ex.Message);
//                return false;
//            }
//            catch (FileNotFoundException)
//            {
//                _userOutput.WriteMessage($"File {fileName} doesn't exist");
//                return false;
//            }
//        }
//    }
//}