package io.github.howiefh;

import io.github.howiefh.export.cli.MainCli;
import io.github.howiefh.ui.MainUI;

public class App 
{
    public static void main( String[] args )
    {
    	if (args.length > 0) {
	    	MainCli.export(args);
		} else {
			MainUI.main(args); 
		}
    }
}
