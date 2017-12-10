package averkova_ebner;

import java.util.HashMap;

public interface Subject
{
	public void registerObserver(Observer o);

	public void removeObserver(Observer o);

	public HashMap<String, Book> getBookMap();

	public void removeBook(Book b);

	public void changeBook(Book b, String name);

	public void addBook(Book b);

}
