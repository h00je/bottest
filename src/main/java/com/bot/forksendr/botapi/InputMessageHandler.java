package com.bot.forksendr.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileNotFoundException;

/**Обработчик сообщений
 */
public interface InputMessageHandler {
    SendMessage handle(Message message) throws FileNotFoundException;

    BotState getHandlerName();
}