using System;
using System.Windows.Input;
using GuardUtils;
using JetBrains.Annotations;

namespace NotebookMVVM.Notebook
{
    public class RelayCommand : ICommand
    {
        [NotNull] private readonly Action<object> _execute;
        private readonly Func<object, bool> _canExecute;

        public event EventHandler CanExecuteChanged
        {
            add => CommandManager.RequerySuggested += value;
            remove => CommandManager.RequerySuggested -= value;
        }

        public RelayCommand([NotNull] Action<object> execute, Func<object, bool> canExecute = null)
        {
            ThrowIf.Variable.IsNull(execute, nameof(execute));
            _execute = execute;
            _canExecute = canExecute;
        }

        public bool CanExecute(object parameter)
        {
            return _canExecute == null || _canExecute(parameter);
        }

        public void Execute(object parameter)
        {
            _execute(parameter);
        }
    }
}