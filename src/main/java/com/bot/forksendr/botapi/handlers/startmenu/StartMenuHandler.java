package com.bot.forksendr.botapi.handlers.startmenu;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.servise.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 Вопросс заполнения анкеты.
 */

@Slf4j
@Component
public class StartMenuHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;


    public StartMenuHandler(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
                  }

    @Override
    public SendMessage handle(Message message) {

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START_MENU_BOT;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.startBotMessage");
        replyToUser.setReplyMarkup(getMainMenuKeyboardStart());

        return replyToUser;
    }




    private ReplyKeyboardMarkup getMainMenuKeyboardStart() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        //KeyboardRow row3 = new KeyboardRow();

        row1.add(new KeyboardButton("Заполнить анкету"));
        row2.add(new KeyboardButton("Результаты работы"));
        row2.add(new KeyboardButton("Рассказать о себе"));
        row2.add(new KeyboardButton("Контактные данные"));
        keyboard.add(row1);
        keyboard.add(row2);
        //keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }


}
