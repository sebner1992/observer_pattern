package averkova_ebner;

public class Event<T>
{
	public enum eventType
	{
		ADD, REMOVE, CHANGE
	}

	private eventType type;
	private T target;

	public Event(eventType type, T target)
	{
		this.type = type;
		this.target = target;
	}

	public eventType getType()
	{
		return type;
	}

	public T getTarget()
	{
		return target;
	}

}