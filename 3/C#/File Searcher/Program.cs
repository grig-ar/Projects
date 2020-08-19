using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Lab3
{
    class Program
    {
        private void FileSearchFunction(string Dir, string fileType)
        {
            System.IO.DirectoryInfo directoryInfo = new System.IO.DirectoryInfo(Dir);
            System.IO.DirectoryInfo[] SubDir = null;
            try
            {
                SubDir = directoryInfo.GetDirectories();
            }
            catch
            {
                return;
            }
            for (int i = 0; i < SubDir.Length; ++i)
                FileSearchFunction(SubDir[i].FullName, fileType);
            System.IO.FileInfo[] filesInfo = directoryInfo.GetFiles();
            for (int i = 0; i < filesInfo.Length; ++i)
            {
                if (filesInfo[i].Extension == fileType)
                {
                    Console.WriteLine(filesInfo[i].FullName);
                    var strings = System.IO.File.ReadAllLines(filesInfo[i].FullName);
                    var count = 0;
                    foreach (var str in strings)
                    {
                        var match = Regex.Match(str, @"^\s*$|^(\s*\/{2})");
                        if (!match.Success)
                            ++count;
                    }
                    Console.WriteLine(count);
                }
            }
        }

        static void Main(string[] args)
        {
            Program program = new Program();
            var fileType = Console.ReadLine();
            program.FileSearchFunction((string)System.IO.Directory.GetCurrentDirectory(), fileType);
        }
    }
}
