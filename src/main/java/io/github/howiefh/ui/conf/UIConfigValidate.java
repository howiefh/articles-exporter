package io.github.howiefh.ui.conf;

public class UIConfigValidate {


	public static boolean validateLookAndFeel(String name) {
		return UIOptions.getInstance().getLookAndFeelNames().contains(name);
	}
}
