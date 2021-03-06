﻿using System;
using System.Collections.Generic;
using System.Linq;
using ContainerInterface;
using GuardUtils;
using JetBrains.Annotations;
using Notebook.UserInteraction;

namespace Notebook.Commands
{
    [Component]
    public sealed class CommandProcessor : ICommandProcessor
    {
        [NotNull] [ItemNotNull] private readonly IReadOnlyList<string> _exitConditions = new List<string> {"exit", "quit", "q"};
        [NotNull] private readonly IUserInput _userInput;
        [NotNull] private readonly IUserOutput _userOutput;
        [NotNull] private readonly IReadOnlyDictionary<string, ICommand> _commands;

        public CommandProcessor([NotNull] IUserInput userInput, [NotNull] IUserOutput userOutput,
            [NotNull] [ItemNotNull] IReadOnlyList<ICommand> commands)
        {
            ThrowIf.Variable.IsNull(userInput, nameof(userInput));
            ThrowIf.Variable.IsNull(userOutput, nameof(userOutput));
            ThrowIf.Variable.IsNull(commands, nameof(commands));

            _userInput = userInput;
            _userOutput = userOutput;
            _commands = commands.ToDictionary(x => x.Name.ToLower());
        }

        public void Run()
        {
            do
            {
                if (!_userInput.TryGetString("", out var userInput))
                {
                    PrintHelp();
                    continue;
                }

                if (_commands.TryGetValue(userInput.ToLower(), out var command))
                {
                    command.Execute();
                    continue;
                }

                if (_exitConditions.Contains(userInput.ToLower()))
                {
                    break;
                }

                PrintHelp();
            } while (true);
        }

        private void PrintHelp()
        {
            Console.WriteLine(
                "Author: Grigorovich Artyom; NSU, FIT\nGroup: 16203\nTask: Container notebook\nVersion: 0.0.4\n");
            Console.WriteLine("Notebook commands: ");
            foreach (var command in _commands.Values)
            {
                _userOutput.WriteMessage($"{command.Name} -- {command.CommandInfo}");
            }
        }
    }
}