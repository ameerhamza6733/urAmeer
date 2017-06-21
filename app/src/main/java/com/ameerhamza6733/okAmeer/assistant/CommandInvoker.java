package com.ameerhamza6733.okAmeer.assistant;

import android.content.Context;

import com.ameerhamza6733.okAmeer.assistant.commands.FlashLightOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.FlashlightOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.setAlarm;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class CommandInvoker {
    private static Command [] commands;

    public static Command[] getCommands() {
        if(commands==null)
        {
            commands = new Command[] {new FlashlightOnCommand(),new FlashLightOffCommand(),new setAlarm()};
        }
        return commands;
    }
    public static boolean excute(Context context,String phrase)
    {
        boolean isCommandFound=false;
        for (Command command : getCommands())
        {
            String[] activationPhrases = command.getDefaultPhrase().split(",");
            for(String activatingParts : activationPhrases)
            {
             if(phrase.trim().contains(activatingParts.trim()))
             {
                 isCommandFound=true;
                 command.execute(context,phrase);
             }
            }
        }
        return isCommandFound;
    }
}
