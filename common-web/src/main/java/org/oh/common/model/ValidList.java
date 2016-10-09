package org.oh.common.model;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

public class ValidList<E> extends AbstractList<E> implements Serializable {
	@Valid
	protected List<E> list = new ArrayList<E>();

	public ValidList() {
	}

	public ValidList(List<E> list) {
		this.list = list;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public E set(int index, E element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		list.add(index, element);
	}

	@Override
	public E remove(int index) {
		return list.remove(index);
	}
}