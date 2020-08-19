using System;
using ContainerInterface;

namespace Notebook.UserInteraction
{
    [Component]
    public sealed class UserOutput : IUserOutput
    {
        public void WriteMessage(string message)
        {
            Console.WriteLine(message);
        }
    }
}