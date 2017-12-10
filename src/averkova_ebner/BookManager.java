package averkova_ebner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BookManager extends Application implements Observer
{
	Subject s;
	ObservableList<Book> books = FXCollections.observableArrayList();
	ListView<Book> listView = new ListView<Book>(books);

	public static void main(String[] args)
	{
		launch(args);
	}

	/**
	 * Startup FX stuff
	 */
	@Override
	public void start(final Stage stage) throws Exception
	{
		s = new ConcreteSubject();
		s.registerObserver(this);
		BorderPane root = new BorderPane();
		Button cmdAdd = new Button("Add");
		Button cmdRemove = new Button("Remove");
		Button cmdRename = new Button("Rename");
		cmdAdd.setOnAction(new AddHandler());
		cmdRemove.setOnAction(new RemoveHandler());
		cmdRename.setOnAction(new RenameHandler());
		ToolBar toolBar = new ToolBar(cmdAdd, cmdRemove, cmdRename);
		root.setTop(toolBar);
		root.setCenter(listView);
		Scene scene = new Scene(root, 200, 200);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Reacts depending on button clicks see {@link Event}
	 */
	@Override
	public void update(Object src, Event<Book> arg)
	{
		Book a = arg.getTarget();
		switch (arg.getType())
		{
		case ADD:
		{
			books.add(new Book(a.getTitle(), a.getAuthor(), a.getIsbn()));
			break;
		}
		case REMOVE:
		{
			books.remove(a);
			break;
		}
		case CHANGE:
		{
			for (int i = 0; i < books.size(); i++)
			{
				Book current = books.get(i);
				if (current.getAuthor().equals(a.getAuthor()))
				{
					current.setTitle(a.getTitle());
					listView.refresh();
					break;
				}
			}
			break;
		}
		}

	}

	/**
	 * Handler for add action FX stuff again
	 */
	private class AddHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event) throws IllegalArgumentException
		{
			TextInputDialog tid = new TextInputDialog();
			tid.setTitle("Add new book");
			tid.setHeaderText("Add new book like so: title, author, isbn");
			String input = tid.showAndWait().orElse(null);
			String[] temp = input.split(",");
			if (temp.length != 3)
			{
				new Alert(AlertType.ERROR, "invalid input").showAndWait();
				return;
			}
			Book book = new Book(temp[0], temp[1], temp[2]);
			for (Book b : books)
			{
				if (b.getIsbn().equals(book.getIsbn()))
				{
					new Alert(AlertType.ERROR, "isbn already exists!").showAndWait();
					return;
				}
			}
			s.addBook(book);
		}
	}

	/**
	 * Same just with remove
	 */
	private class RemoveHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event)
		{
			final int selectedIdx = listView.getSelectionModel().getSelectedIndex();
			if (selectedIdx != -1)
			{
				s.removeBook((listView.getSelectionModel().getSelectedItem()));
			}
		}
	}

	/**
	 * Same with rename
	 */
	private class RenameHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event)
		{
			final int selectedIdx = listView.getSelectionModel().getSelectedIndex();
			if (selectedIdx != -1)
			{
				Book book = listView.getSelectionModel().getSelectedItem();
				TextInputDialog tid = new TextInputDialog();
				tid.setTitle("Add new book");
				tid.setHeaderText(
						"Add new title for book: " + book.getTitle() + " " + book.getAuthor() + " " + book.getIsbn());
				String title = tid.showAndWait().orElse(null);
				if (title.length() < 1)
				{
					new Alert(AlertType.ERROR, "no input!").showAndWait();
					return;
				}
				s.changeBook(book, title);
			}
		}
	}
}
