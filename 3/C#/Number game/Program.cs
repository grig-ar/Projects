using System;
using System.Text;

namespace Lab2
{
    class Program
    {
        static void Main(string[] args)
        {
            TimeSpan timerStart = DateTime.Now.TimeOfDay, timerEnd, timePassed;
            StringBuilder gameLog = new StringBuilder();
            int tries = 0;
            Random random = new Random();
            int numberToGuess = random.Next(0, 51);
            string nonParsedNumber = "";
            int currentNumber = -1;
            Console.Write("Hello! Please enter your name: ");
            var name = Console.ReadLine();
            string[] cheerUps = { $"Good luck, {name}!", $"{name}, you're almost there!", $"Good job {name}", $"Keep up the good work {name}" };
            Console.WriteLine("Try to guess the number [0, 50]");
            timerStart = DateTime.Now.TimeOfDay;
            while (currentNumber != numberToGuess)
            {
                nonParsedNumber = Console.ReadLine();
                ++tries;
                if (nonParsedNumber == "q")
                {
                    Console.WriteLine("Bye-bye!");
                    return;
                }
                try
                {
                    currentNumber = Int32.Parse(nonParsedNumber);
                    gameLog.Append(currentNumber).Append(" - ");
                    if (numberToGuess > currentNumber)
                    {
                        Console.WriteLine("Less");
                        gameLog.Append("Less\n");
                    }
                    else if (numberToGuess < currentNumber)
                    {
                        Console.WriteLine("Greater");
                        gameLog.Append("Greater\n");
                    }
                    else
                    {
                        gameLog.Append("You've guessed right!\n");
                    }
                    if (tries % 4 == 0 && currentNumber != numberToGuess)
                    {
                        Console.WriteLine(cheerUps[random.Next(cheerUps.Length)]);
                    }
                }
                catch (FormatException)
                {
                    Console.WriteLine("Unable to convert '{0}'.", nonParsedNumber);
                }
                catch (OverflowException)
                {
                    Console.WriteLine("'{0}' is out of range of the Int32 type.", nonParsedNumber);
                }

            }
            
            timerEnd = DateTime.Now.TimeOfDay;
            timePassed = timerEnd - timerStart;
            Console.WriteLine("Tries: {0}", tries);
            Console.Write(gameLog);
            Console.WriteLine("Time passed: {0}", timePassed.ToString(@"mm\:ss"));

        }
    }
}
