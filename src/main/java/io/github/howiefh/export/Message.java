package io.github.howiefh.export;

public interface Message {
	void warn(String text);
	void info(String text);
	void error(String text);
}
