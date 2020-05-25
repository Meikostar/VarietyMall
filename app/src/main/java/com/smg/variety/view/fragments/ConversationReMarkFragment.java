package com.smg.variety.view.fragments;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class ConversationReMarkFragment extends ConversationListFragment {

    public void onEventMainThread(Message message) {

        Conversation.ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();

        if (Conversation.ConversationType.SYSTEM == conversationType) {
            return;
        } else {
            super.onEventMainThread(message);
        }
    }

    public boolean shouldFilterConversation(Conversation.ConversationType type, String targetId) {
        if (Conversation.ConversationType.SYSTEM == type) return true;
        return false;
    }
}
