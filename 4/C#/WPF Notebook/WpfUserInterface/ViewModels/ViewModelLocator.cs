using ContainerImplementation;

namespace WpfUserInterface.ViewModels
{
    public class ViewModelLocator
    {
        private readonly IContainer _container;

        private MainViewModel _main;

        public ViewModelLocator()
        {
            var containerBuilder = new ContainerBuilder();
            _container = containerBuilder.Build();
        }

        public MainViewModel Main => _main ?? (_main = (MainViewModel) _container.Resolve<IMainViewModel>());
    }
}