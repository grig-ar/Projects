namespace Notebook.UserInteraction
{
    public sealed class FakeUserInput : IUserInput
    {
        public bool TryGetString(string info, out string userInput)
        {
            userInput = "qwerty";
            return true;
        }

        public bool TryGetInt(string info, out int? userInput)
        {
            userInput = 111;
            return true;
        }
    }
}