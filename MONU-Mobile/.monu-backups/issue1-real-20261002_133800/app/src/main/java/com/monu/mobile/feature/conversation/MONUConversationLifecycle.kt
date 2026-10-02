package com.monu.mobile.feature.conversation

class MONUConversationLifecycle(
    private val stateController:
        MONUConversationStateController =
        MONUConversationStateController()
) {

    fun start(): MONUConversationState {
        stateController.restoreConversation()
        return stateController.state()
    }

    fun snapshot(): MONUConversationSnapshot {
        return stateController.snapshot()
    }

    fun clear(): MONUConversationState {
        stateController.clearConversation()
        return stateController.state()
    }
}
