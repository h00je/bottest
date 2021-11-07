package com.bot.forksendr.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) throws NullPointerException, FileNotFoundException {

        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);

        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler (BotState currentState) throws NullPointerException {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.FILLING_PROFILE);
        }

        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case ASK_SURNAME:
            case ASK_AGE:
            case ERROR_AGE:
            case ASK_GENDER:
            case ASK_CONTACT:
            case PROFILE_FILLED:
            case FILLING_PROFILE:
                return true;
            default:
                return false;
        }
    }


}