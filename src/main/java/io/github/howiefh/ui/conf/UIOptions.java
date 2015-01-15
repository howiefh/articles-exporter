package io.github.howiefh.ui.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import com.alee.laf.WebLookAndFeel;

public class UIOptions {
	private static Map<String, String> lookAndFeels = new HashMap<String, String>();
	private static UIOptions instance;
    private UIOptions() {
		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();  
	    for (LookAndFeelInfo info : infos) {  
	    	lookAndFeels.put(info.getName(), info.getClassName());
	    }  
	    lookAndFeels.put("WebLookAndFeel", WebLookAndFeel.class.getCanonicalName());
	}
    public String getLookAndFeel(String name) {
		return lookAndFeels.get(name);
	}
    public Set<String> getLookAndFeelNames() {
    	return lookAndFeels.keySet();
    }
    public static UIOptions getInstance() {
		if (instance == null) {
			instance = new UIOptions();
		}
		return instance;
	}
}
