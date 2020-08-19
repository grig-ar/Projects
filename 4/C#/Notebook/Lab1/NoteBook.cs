using System;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.Serialization;

namespace Lab1
{

    class NoteBook
    {
        public MyList list = new MyList();

        public void Run()
        {
            bool running = true;
            var action = "";
            const string IsReady = "> ";
            PrintHelp();
            while (running)
            {
                Console.Write(IsReady);
                action = Console.ReadLine().ToLower();
                switch (action)
                {
                    case "help":
                        PrintHelp();
                        break;
                    case "add student":
                        AddStudent();
                        break;
                    case "add phone":
                        AddPhone();
                        break;
                    case "print":
                        PrintList();
                        break;
                    case "search":
                        Search();
                        break;
                    case "save":
                        Save();
                        break;
                    case "load":
                        Load();
                        break;
                    case "q":
                    case "quit":
                    case "exit":
                        running = false;
                        break;
                    default:
                        Console.WriteLine("Incorrect command!");
                        break;
                }
            }
        }

        void PrintHelp()
        {
            Console.WriteLine("Author: Grigorovich Artyom; NSU, FIT\nGroup: 16203\nTask: NoteBook\nVersion: 0.0.1\n");
            Console.WriteLine("Available commands:");
            Console.WriteLine("help -- this message");
            Console.WriteLine("exit -- closes notebook");
            Console.WriteLine("add student -- adds new student note to notebook;");
            Console.WriteLine("add phone  -- adds new phone note to notebook;");
            Console.WriteLine("print -- shows all notes;");
            Console.WriteLine("search -- searches by name, group or phone");
            Console.WriteLine("save -- saves notes to file");
            Console.WriteLine("load filename -- tries to load notes from filename");
        }

        void AddStudent()
        {
            var name = "";
            var group = "";
            int groupNumber = -1;
            Console.WriteLine("Student name:");
            name = Console.ReadLine();
            Console.WriteLine("Student group:");
            group = Console.ReadLine();
            var success = Int32.TryParse(group, out groupNumber);
            if (success)
            {
                Node temp = new Node
                {
                    Current = new StudentNote(name, groupNumber)
                };
                list.AddNode(temp);
            }
            else
            {
                Console.WriteLine("{0} is not a number", group);
            }
        }

        void AddPhone()
        {
            var name = "";
            var phone = "";
            Console.WriteLine("Student name:");
            name = Console.ReadLine();
            Console.WriteLine("Student phone:");
            phone = Console.ReadLine();
            Node temp = new Node
            {
                Current = new PhoneNote(name, phone)
            };
            list.AddNode(temp);
        }

        void PrintList()
        {
            list.PrintAll();
        }

        void Search()
        {
            Console.WriteLine("Select category:(name, phone, group)");
            var category = Console.ReadLine().ToLower();
            bool result = false;
            switch (category)
            {
                case "name":
                    Console.WriteLine("Input name: ");
                    result = SearchByName(Console.ReadLine());
                    break;
                case "phone":
                    Console.WriteLine("Input phone: ");
                    result = SearchByPhone(Console.ReadLine());
                    break;
                case "group":
                    Console.WriteLine("Input group: ");
                    var group = "";
                    int groupNumber = -1;
                    group = Console.ReadLine();
                    var success = Int32.TryParse(group, out groupNumber);
                    if (success)
                    {
                        result = SearchByGroup(groupNumber);
                    }
                    else
                    {
                        Console.WriteLine("{0} is not a number", group);
                    }
                    break;
                default:
                    Console.WriteLine("Incorrect category!");
                    return;
            }
            if (!result)
            {
                Console.WriteLine("No records found.");
            }
        }

        bool SearchByName(string name)
        {
            bool result = false;
            Node currentNode = list.Head;
            while (currentNode.Current != null)
            {
                if (currentNode.Current.Name.Equals(name))
                {
                    Console.WriteLine(currentNode.Current.ToString());
                    result = true;
                }

                currentNode = currentNode.Next;
            }
            return result;
        }

        bool SearchByPhone(string phone)
        {
            bool result = false;
            Node currentNode = list.Head;
            while (currentNode.Current != null)
            {

                if (currentNode.Current is PhoneNote temp)
                {
                    if (temp.PhoneNumber.Equals(phone))
                        Console.WriteLine(currentNode.Current.ToString());
                    result = true;
                }
                currentNode = currentNode.Next;
            }
            return result;
        }

        bool SearchByGroup(int group)
        {
            bool result = false;
            Node currentNode = list.Head;
            while (currentNode.Current != null)
            {

                if (currentNode.Current is StudentNote temp)
                {
                    if (temp.GroupNumber.Equals(group))
                        Console.WriteLine(currentNode.Current.ToString());
                    result = true;
                }
                currentNode = currentNode.Next;
            }
            return result;
        }

        void Save()
        {
            Console.WriteLine("Enter file name:");
            var fileName = Console.ReadLine();
            BinaryFormatter formatter = new BinaryFormatter();
            if (File.Exists(@"c:\Users\Артем\source\repos\Lab1\Lab1\bin\Debug\" + fileName + ".notebook"))
            {
                Console.WriteLine(@"File {0} will be overwritten. Continue? (Y/N)", fileName);
                if (!Console.ReadLine().ToUpper().Equals("Y"))
                {
                    Console.WriteLine();
                    return;
                }
            }

            try
            {
                using (var fileStream = new FileStream(fileName + ".notebook", FileMode.Create))
                {
                    formatter.Serialize(fileStream, list);
                }
                Console.WriteLine("Notebook saved successfully!");
            }
            catch (SerializationException ex)
            {
                Console.WriteLine("Failed to serialize. " + ex.Message);
            }
        }

        void Load()
        {
            Console.WriteLine("Enter the file name:");
            var fileName = Console.ReadLine();
            BinaryFormatter formatter = new BinaryFormatter();
            try
            {
                using (var fileStream = new FileStream(fileName + ".notebook", FileMode.Open))
                {
                    list = (MyList)formatter.Deserialize(fileStream);
                }
                Console.WriteLine();
                PrintList();
            }
            catch (SerializationException ex)
            {
                Console.WriteLine("Failed to deserialize. " + ex.Message);
            }
            catch (FileNotFoundException)
            {
                Console.WriteLine("File {0} doesn't exist", fileName);
            }
        }
    }
}
