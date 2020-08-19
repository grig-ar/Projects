using System;
using ContainerInterface;
using JetBrains.Annotations;

namespace Notebook.UserInteraction
{
    [Component]
    public sealed class UserInput : IUserInput
    {
        [MustUseReturnValue]
        public bool TryGetString(string info, out string userInput)
        {
            if (info.Length != 0)
            {
                Console.WriteLine(info);
            }

            Console.Write("> ");
            userInput = Console.ReadLine();

            if (!string.IsNullOrEmpty(userInput))
            {
                return true;
            }

            userInput = null;
            return false;
        }

        [MustUseReturnValue]
        public bool TryGetInt(string info, out int? userInput)
        {
            Console.WriteLine(info);
            Console.Write("> ");
            var success = int.TryParse(Console.ReadLine(), out var number);
            if (success)
            {
                userInput = number;
                return true;
            }

            Console.WriteLine($"{number} is not a number");
            userInput = null;
            return false;
        }
    }
}