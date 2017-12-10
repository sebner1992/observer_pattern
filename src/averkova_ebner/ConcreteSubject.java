package averkova_ebner;

import java.util.ArrayList;
import java.util.HashMap;

public class ConcreteSubject implements Subject
{
	private ArrayList<Observer> observers;
	// ISNB + BOOK
	private HashMap<String, Book> bookMap;

	public ConcreteSubject()
	{
		observers = new ArrayList<Observer>();
		bookMap = new HashMap<String, Book>();
	}

	@Override
	public void registerObserver(Observer o)
	{
		observers.add(o);
		System.out.println("OBSERVER ADDED: " + o);
	}

	@Override
	public void removeObserver(Observer o)
	{
		observers.remove(o);
		System.out.println("OBSERVER REMOVED: " + o);
	}

	private void notifyObservers(Event<Book> e)
	{
		for (Observer observer : observers)
		{
			observer.update(this, e);
		}
		System.out.println("NOTIFY OBSERVERS!");
	}

	@Override
	public void addBook(Book book)
	{
		String isbn = book.getIsbn();
		if (!(bookMap.containsKey(isbn)))
		{
			bookMap.put(isbn, book);
			System.out.println("New Book: " + book.getTitle() + ", " + book.getAuthor() + ", " + book.getIsbn());
			notifyObservers(new Event<Book>(Event.eventType.ADD, book));
		}
	}

	@Override
	public void changeBook(Book book, String title)
	{
		String isbn = book.getIsbn();
		if (bookMap.containsKey(isbn))
		{
			bookMap.get(isbn).setTitle(title);
			System.out.println("Book title has been changed to: " + title);
			notifyObservers(new Event<Book>(Event.eventType.CHANGE,
					new Book(bookMap.get(isbn).getTitle(), bookMap.get(isbn).getAuthor(), isbn)));

		}
	}

	@Override
	public void removeBook(Book book)
	{
		String isbn = book.getIsbn();
		if (bookMap.containsKey(isbn))
		{
			bookMap.remove(isbn);
			System.out.println("Book with isbn '" + isbn + "' has been removed");
			notifyObservers(new Event<Book>(Event.eventType.REMOVE, book));
		}
	}

	@Override
	public HashMap<String, Book> getBookMap()
	{
		return bookMap;
	}

}
