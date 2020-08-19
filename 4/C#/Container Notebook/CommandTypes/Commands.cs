using System;
using System.Collections.Generic;
using NoteTypes;

namespace CommandTypes
{

    public interface ICommand
    {
        string GetName();
        void Execute();
    }

    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Interface)]
    public sealed class ComponentAttribute : Attribute
    {
        public string CommandInfo { get; set; }
    }

    //public interface ICommandRegistrator
    //{
    //    ICommand GetCommand(string commandName);
    //}

    //public interface IGetData
    //{

    //}

    //public interface IInputData
    //{

    //}

    //public interface INoteBook
    //{
    //    void Add(Note note);

    //    void Find(String element);

    //    void PrintHelp();

    //}

}
