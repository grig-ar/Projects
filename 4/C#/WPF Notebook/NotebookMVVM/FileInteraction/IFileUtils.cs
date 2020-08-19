using System.IO;
using JetBrains.Annotations;

namespace NotebookMVVM.FileInteraction
{
    public interface IFileUtils
    {
        Stream Serialize(object source);
        T Deserialize<T>([NotNull] Stream stream);
        T Clone<T>(object source);
    }
}