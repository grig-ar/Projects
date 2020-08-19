using CommandTypes;
using System;

namespace TestModule
{
    [Component]
    public class Y
    {
        public X _x;

        public Y(X x)
        {
            _x = x;
            Console.WriteLine("In Y constructor!");
        }

        public void PrintNumber()
        {
            Console.WriteLine(_x.Z);
        }

    }
}
