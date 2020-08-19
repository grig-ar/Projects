using System;

namespace Lab1
{
    [Serializable]
    class MyList
    {
        public Node Head { get; set; }

        public MyList()
        {
            Head = new Node
            {
                Next = null
            };
        }

        public void AddNode(Node n)
        {
            Node temp = new Node
            {
                Current = n.Current,
                Next = Head
            };
            Head = temp;
        }

        public void PrintAll()
        {
            Node currentNode = Head;
            if (currentNode.Current == null)
            {
                Console.WriteLine("No records found.");
                return;
            }

            while (currentNode.Current != null)
            {
                Console.WriteLine(currentNode.Current.ToString());
                currentNode = currentNode.Next;
            }
        }
    }
}
