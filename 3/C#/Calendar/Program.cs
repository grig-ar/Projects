using System;

namespace Lab1
{
    class Program
    {
        static void Main(string[] args)
        {
            var dateString = Console.ReadLine();
            var flag = DateTime.TryParse(dateString, out DateTime date);
            if (!flag)
            {
                Console.WriteLine("Incorrect date!");
                return;
            }
            DateTime firstDay = new DateTime(date.Year, date.Month, 1);
            Console.WriteLine("\nMo Tu We Th Fr Sa Su");
            var days = DateTime.DaysInMonth(date.Year, date.Month);
            var space = "   ";
            int workDays = 0;
            int currentDay = 1;
            var dayOfWeek = (int)firstDay.DayOfWeek;
            for (int i = 0; i < 6; ++i)
            {
                for (int j = 0; j < 7; ++j)
                {
                    if (i * 7 + j < ((dayOfWeek + 6) % 7) || currentDay > days)
                        Console.Write(space);
                    else
                    {
                        if ((dayOfWeek - 1 + i * 7 + j) % 6 != 0 && (dayOfWeek - 1 + i * 7 + j) % 7 != 0)
                            ++workDays;
                        Console.Write($"{(currentDay < 10 ? $" {currentDay} " : $"{currentDay} ")}");
                        ++currentDay;
                    }
                }
                Console.WriteLine();
            }
            Console.WriteLine($"\nWork days: {workDays}");
        }
    }
}
