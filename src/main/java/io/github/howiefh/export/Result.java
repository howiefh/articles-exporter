package io.github.howiefh.export;

public interface Result {
	<T> void result(int index,T msg);
}
