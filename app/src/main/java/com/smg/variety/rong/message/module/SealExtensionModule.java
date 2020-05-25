package com.smg.variety.rong.message.module;


import java.util.ArrayList;
import java.util.List;

import io.rong.callkit.AudioPlugin;
import io.rong.callkit.VideoPlugin;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.sight.SightPlugin;

public class SealExtensionModule extends DefaultExtensionModule {
    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
    }

    @Override
    public void onConnect(String token) {
        super.onConnect(token);
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        super.onAttachedToExtension(extension);
    }

    @Override
    public void onDetachedFromExtension() {
        super.onDetachedFromExtension();
    }

    @Override
    public void onReceivedMessage(Message message) {
        super.onReceivedMessage(message);
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModuleList = new ArrayList<>();
        IPluginModule image = new ImagePlugin();
        pluginModuleList.add(image);
        IPluginModule sight = new SightPlugin();
        pluginModuleList.add(sight);
        VideoPlugin video = new VideoPlugin();
        pluginModuleList.add(video);

        IPluginModule file = new FilePlugin();
        pluginModuleList.add(file);


        if(conversationType == Conversation.ConversationType.PRIVATE) {

            AudioPlugin audio = new AudioPlugin();
            pluginModuleList.add(audio);

            IPluginModule locationPlugin = new DefaultLocationPlugin();
            pluginModuleList.add(locationPlugin);
        }else {

            IPluginModule locationPlugin = new DefaultLocationPlugin();
            pluginModuleList.add(locationPlugin);


        }



        return pluginModuleList;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return super.getEmoticonTabs();
    }
}
