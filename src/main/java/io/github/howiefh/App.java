package io.github.howiefh;

import io.github.howiefh.export.cli.MainCli;
import io.github.howiefh.ui.MainUI;
import io.github.howiefh.util.LogUtil;

public class App 
{
    public static void main( String[] args )
    {
    	//从配置文件，初始化logger
    	LogUtil.log();
    	if (args.length > 0) {
	    	MainCli.export(args);
		} else {
			MainUI.main(args); 
		}
    }
}
