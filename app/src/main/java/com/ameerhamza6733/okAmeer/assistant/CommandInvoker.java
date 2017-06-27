package com.ameerhamza6733.okAmeer.assistant;

import android.content.Context;
import android.os.Handler;

import com.ameerhamza6733.okAmeer.assistant.commands.CallCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.FlashLightOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.FlashlightOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.CallingActivity;
import com.ameerhamza6733.okAmeer.assistant.commands.setAlarm;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class CommandInvoker {
    private static Command [] commands;

    public static Command[] getCommands() {
        if(commands==null)
        {
            commands = new Command[] {new FlashlightOnCommand(),new FlashLightOffCommand(),new setAlarm(),new CallCommand()};
        }
        return commands;
    }
    public static boolean excute(final Context context, String phrase)
    {
        boolean isCommandFound=false;
        for (final Command command : getCommands())
        {
            String[] activationPhrases = command.getDefaultPhrase().split(",");
            for(String activatingParts : activationPhrases)
            {
             if(phrase.trim().contains(activatingParts.trim()))
             {
                 isCommandFound=true;
                 command.execute(context,phrase);


                try {




                }catch (Exception e)
                {
                    e.printStackTrace();
                }


             }
            }
        }
        return isCommandFound;
    }
}
