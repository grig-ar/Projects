using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommandTypes;

namespace TestModule
{
    [Component]
    public class X : ITest
    {
        public X(int z)
        {   this.Z = z;
            Console.WriteLine("In X Constructor!");
        }
        public int Z;

        public void PrintNumber()
        {
            Console.WriteLine($"Number = {Z}");
        }
    }
}
