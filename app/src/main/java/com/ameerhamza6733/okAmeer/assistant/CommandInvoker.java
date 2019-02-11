package com.ameerhamza6733.okAmeer.assistant;

import android.content.Context;
import android.util.Log;

import com.ameerhamza6733.okAmeer.assistant.commands.BluetoothOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.BluetoothOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.CallCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.FlashLightOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.FlashlightOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.DecreaseVolumeCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.PlayMusicCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.PreviousSongCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.SetAlarmCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.increaseVolumeCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.NextSongCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.OpenCamra;
import com.ameerhamza6733.okAmeer.assistant.commands.PauseMuusicCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.PostOnFaceBook;
import com.ameerhamza6733.okAmeer.assistant.commands.ReadSmsCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.SendGmailCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.SendSmsCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.SendWhatsAppCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.SilentCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.WifiOffCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.WifiOnCommand;
import com.ameerhamza6733.okAmeer.assistant.commands.YoutubePlayCommand;
import com.ameerhamza6733.okAmeer.utial.myTextToSpeech;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

/**
 * Created by AmeerHamza on 6/17/2017.
 */

public class CommandInvoker {
    private static Command [] commands;
    private final static String TAG ="Command invoker";

    public static Command[] getCommands() {
        if(commands==null)
        {
            commands = new Command[] {new FlashlightOnCommand(),new FlashLightOffCommand(),new SetAlarmCommand(),new CallCommand(),new ReadSmsCommand(),new PauseMuusicCommand(),new PreviousSongCommand(),new NextSongCommand(),new YoutubePlayCommand(),new PlayMusicCommand()
            ,new increaseVolumeCommand(),new DecreaseVolumeCommand(),new SendWhatsAppCommand(), new SendSmsCommand(),new WifiOnCommand(),new WifiOffCommand(), new SilentCommand(), new SendGmailCommand(), new BluetoothOnCommand(), new BluetoothOffCommand(), new PostOnFaceBook(), new OpenCamra()};
        }
        return commands;
    }
    public static boolean excute(final Context context, ArrayList<String> phrases)
    {
        Crashlytics.log(Log.DEBUG,TAG,"excuting the commander with phrases: "+phrases.get(0));
        boolean isCommandFound=false;
        for (final Command command : getCommands())
        {
            String[] activationPhrases = command.getDefaultPhrase().split(",");
           for(String phrase : phrases){
               for(String activatingParts : activationPhrases)
               {
                   if(isCommandFound)
                       break;
                   if(phrase.toLowerCase().startsWith(activatingParts.toLowerCase()) || phrase.toLowerCase().endsWith(activatingParts.toLowerCase()))
                   {
                       isCommandFound=true;
                       command.execute(new CommandModel(phrase.replace(activatingParts.trim().toLowerCase(),""),phrase,context));
                       Crashlytics.log(Log.DEBUG,TAG,"command founded: "+command);
                       try {
                           myTextToSpeech.intiTextToSpeech(context,"hi",command.getTtsPhrase(context));
                       }catch (Exception e)
                       {
                           e.printStackTrace();
                       }


                   }
               }
           }
        }
        return isCommandFound;
    }
}
