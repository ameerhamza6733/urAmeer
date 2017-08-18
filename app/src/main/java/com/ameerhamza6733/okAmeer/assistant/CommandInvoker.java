package com.ameerhamza6733.okAmeer.assistant;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.commands.BluetoothOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.BluetoothOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.CallCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.FlashLightOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.FlashlightOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.Receivers.smsUnreadActivity;
import com.ameerhamza6733.okAmeer.assistant.commands.decreaseVolumeCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.increaseVolumeCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.nextSongCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.pauseMuusicCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.playMusicCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.postOnFaceBook;
import com.ameerhamza6733.okAmeer.assistant.commands.previousSongCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.readSmsCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.sendGmailCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.sendSmsCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.sendWhatsAppCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.setAlarmCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.silentCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.wifiOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.wifiOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.youtubePlayCommand;
import com.ameerhamza6733.okAmeer.utial.TTSService;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class CommandInvoker {
    private static Command [] commands;

    public static Command[] getCommands() {
        if(commands==null)
        {
            commands = new Command[] {new FlashlightOnCommand(),new FlashLightOffCommand(),new setAlarmCommand(),new CallCommand(),new readSmsCommand(),new pauseMuusicCommand(),new previousSongCommand(),new nextSongCommand(),new youtubePlayCommand(),new playMusicCommand()
            ,new increaseVolumeCommand(),new decreaseVolumeCommand(),new sendWhatsAppCommand(), new sendSmsCommand(),new wifiOnCommand(),new wifiOffCommand(), new silentCommand(), new sendGmailCommand(), new BluetoothOnCommand(), new BluetoothOffCommand(), new postOnFaceBook()};
        }
        return commands;
    }
    public static boolean excute(final Context context, String phrase)
    {
        Log.d("commander invoker","ecuted"+phrase);
        boolean isCommandFound=false;
        for (final Command command : getCommands())
        {
            String[] activationPhrases = command.getDefaultPhrase().split(",");
            for(String activatingParts : activationPhrases)
            {
                if(isCommandFound)
                    break;
             if(phrase.trim().contains(activatingParts.trim()))
             {
                 isCommandFound=true;
                 command.execute(context,phrase);
                try {
                    myTextToSpeech.intiTextToSpeech(context,"hi",command.getTtsPhrase(context));
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
