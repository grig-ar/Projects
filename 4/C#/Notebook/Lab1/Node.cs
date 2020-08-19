using System;

namespace Lab1
{
    [Serializable]
    class Node
    {
        public Note Current { get; set; }

        public Node Next { get; set; }
    }
}
