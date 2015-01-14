package hu.sebcsaba.gitrelocate;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class OnlyOnceQueue<E> extends LinkedList<E> implements Queue<E> {

	private static final long serialVersionUID = -6396975739860043097L;
	
	private final Set<E> seenItems;

	public OnlyOnceQueue() {
		super();
		seenItems = new HashSet<E>();
	}

	public OnlyOnceQueue(Collection<? extends E> c) {
		super();
		seenItems = new HashSet<E>();
		addAll(c);
	}

	@Override
	public boolean add(E e) {
		if (!seenItems.contains(e)) {
			seenItems.add(e);
			return super.add(e);
		}
		return false;
	}
	
	@Override
	public void addFirst(E e) {
		if (!seenItems.contains(e)) {
			seenItems.add(e);
			super.addFirst(e);
		}
	}

	@Override
	public void addLast(E e) {
		if (!seenItems.contains(e)) {
			seenItems.add(e);
			super.addLast(e);
		}
	}

	@Override
	public boolean offer(E e) {
		if (!seenItems.contains(e)) {
			seenItems.add(e);
			return super.offer(e);
		}
		return false;
	}

	@Override
	public boolean offerFirst(E e) {
		if (!seenItems.contains(e)) {
			seenItems.add(e);
			return super.offerFirst(e);
		}
		return false;
	}

	@Override
	public boolean offerLast(E e) {
		if (!seenItems.contains(e)) {
			seenItems.add(e);
			return super.offerLast(e);
		}
		return false;
	}

	@Override
	public void push(E e) {
		if (!seenItems.contains(e)) {
			seenItems.add(e);
			super.push(e);
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = false;
		for (E e : c) {
			result |= this.add(e);
		}
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

}
