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
//    public sealed class FileSaver : IFileSaver
//    {
//        [NotNull] private readonly IUserOutput _userOutput;

//        public FileSaver([NotNull] IUserOutput userOutput)
//        {
//            ThrowIf.Variable.IsNull(userOutput, nameof(userOutput));

//            _userOutput = userOutput;
//        }

//        [MustUseReturnValue]
//        public bool TrySaveFile(string fileName, INotebook notebook)
//        {
//            var stream = FileUtils.Serialize(notebook.Notes);
//            if (fileName.Length <= 0)
//            {
//                stream.Dispose();
//                return false;
//            }
//            try
//            {
//                using (var fileStream = new FileStream(fileName, FileMode.Create))
//                {
//                    stream.Position = 0;
//                    stream.CopyTo(fileStream);
//                }

//                _userOutput.WriteMessage("Notebook saved successfully!");
//                stream.Dispose();
//                return true;
//            }
//            catch (SerializationException ex)
//            {
//                _userOutput.WriteMessage("Failed to serialize. " + ex.Message);
//                stream.Dispose();
//                return false;
//            }
//        }
//    }
//}