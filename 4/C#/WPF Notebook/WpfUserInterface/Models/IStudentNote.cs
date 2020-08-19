namespace WpfUserInterface.Models
{
    public interface IStudentNote : INote
    {
        int GroupNumber { get; set; }
    }
}