using System;
using System.Runtime.CompilerServices;
using System.Windows;

namespace WpfNotebook
{
    public class App : Application
    {
        [STAThread]
        public static void Main(string[] args)
        {
            var app = new App();
            var main = new MainWindow();
            app.Startup += (s, e) =>
            {
                main.Show();
                /* Start up the app */
            };
            app.Exit += (s, e) =>
            {
                main.Close();
                /* Exit the app */
            };
        }
    }
}