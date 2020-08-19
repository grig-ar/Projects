using System;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using GuardUtils;
using JetBrains.Annotations;

namespace NotebookMVVM.FileInteraction
{
    public static class BinaryFileUtils
    {
        public static Stream Serialize(object source)
        {
            IFormatter formatter = new BinaryFormatter();
            Stream stream = new MemoryStream();
            formatter.Serialize(stream, source);
            return stream;
        }

        public static T Deserialize<T>([NotNull] Stream stream)
        {
            ThrowIf.Variable.IsNull(stream, nameof(stream));
            IFormatter formatter = new BinaryFormatter();
            stream.Position = 0;
            return (T) formatter.Deserialize(stream);
        }

        public static T Clone<T>(object source)
        {
            return Deserialize<T>(Serialize(source) ?? throw new InvalidOperationException());
        }
    }
}