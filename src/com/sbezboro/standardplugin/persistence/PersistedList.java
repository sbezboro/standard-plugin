package com.sbezboro.standardplugin.persistence;

import java.util.ArrayList;
import java.util.Iterator;

import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.exceptions.NotPersistableException;
import com.sbezboro.standardplugin.util.MiscUtil;

public class PersistedList<T> implements Persistable, Iterable<T> {
	private PersistedObject object;
	private String name;
	
	private ArrayList<T> list;
	
	@SuppressWarnings("unchecked")
	public PersistedList(PersistedObject object, Class<T> cls, String name) {
		this.name = name;
		this.object = object;
		this.list = new ArrayList<T>();
		
		ArrayList<Object> objects = (ArrayList<Object>) object.loadProperty(name, null);
		if (objects != null) {
			for (Object obj : objects) {
				try {
					if (Persistable.class.isAssignableFrom(cls)) {
						Persistable persistable = (Persistable) cls.newInstance();
						persistable.loadFromPersistance(obj);
						this.list.add((T) persistable);
					} else if (MiscUtil.isWrapperType(cls)){
						this.list.add((T) obj);
					} else {
						throw new NotPersistableException("Class " + cls.getName() + " does implement Persistable nor is a primative wrapper.");
					}
				} catch (Exception e) {
					StandardPlugin.getPlugin().getLogger().severe(e.toString());
				}
			}
		}
	}
	
	public void add(T obj) {
		this.list.add(obj);
		
		object.saveProperty(name, this);
	}
	
	public void remove(T obj) {
		this.list.remove(obj);
		
		object.saveProperty(name, this);
	}
	
	public boolean contains(T obj) {
		return this.list.contains(obj);
	}
	
	public T get(int index) {
		return this.list.get(index);
	}

	@Override
	public void loadFromPersistance(Object object) {
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object persistableRepresentation() {
		ArrayList<T> copy = new ArrayList<T>();
		
		for (T obj : this.list) {
			if (obj instanceof Persistable) {
				Persistable persistable = (Persistable) obj;
				copy.add((T) persistable.persistableRepresentation());
			} else {
				copy.add(obj);
			}
		}
		
		return copy;
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}
}
