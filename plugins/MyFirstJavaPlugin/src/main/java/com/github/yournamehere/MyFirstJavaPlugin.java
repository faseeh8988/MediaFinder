package com.github.yournamehere;

import android.content.Context;
import com.aliucord.api.CommandsAPI;
import com.discord.api.message.attachment.MessageAttachment;
import com.aliucord.entities.Plugin;
import com.discord.models.message.Message;
import com.discord.stores.StoreStream;
import java.util.ArrayList;
import java.util.List;

public class MyFirstJavaPlugin extends Plugin {

    @Override
    public void start(Context context) {
        // Hum "findmedia" command register kar rahe hain
        commands.registerCommand(
            "findmedia",
            "Recent messages mein se media links dhoondain",
            new ArrayList<>(),
            ctx -> {
                long channelId = ctx.getChannelId();
                List<Message> messages = StoreStream.getMessages().getMessages(channelId).snapshot();
                
                StringBuilder result = new StringBuilder("🔍 **Recent Media Found:**\n");
                boolean found = false;

                int count = 0;
                for (Message msg : messages) {
                    if (count > 20) break; // Sirf last 20 messages check kare ga
                    
                    List<MessageAttachment> attachments = msg.getAttachments();
                    if (attachments != null && !attachments.isEmpty()) {
                        for (MessageAttachment attachment : attachments) {
                            result.append("📸 ").append(attachment.getUrl()).append("\n");
                            found = true;
                        }
                    }
                    count++;
                }

                if (!found) {
                    return new CommandsAPI.CommandResult("Koi media nahi mila bhai! 🤷‍♂️", null, false);
                }

                return new CommandsAPI.CommandResult(result.toString(), null, false);
            }
        );
    }

    @Override
    public void stop(Context context) {
        // Plugin stop hone par command khatam
        commands.unregisterAll();
    }
}
