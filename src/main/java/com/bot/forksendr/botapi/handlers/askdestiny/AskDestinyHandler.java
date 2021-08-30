package com.bot.forksendr.botapi.handlers.askdestiny;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.servise.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 Вопросс заполнения анкеты.
 */

@Slf4j
@Component
public class AskDestinyHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;

    public AskDestinyHandler(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askDestiny");
        replyToUser.setReplyMarkup(getInlineMessageButtons());

        return replyToUser;
    }
 /*Метод для создания клавиатур*/

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonQuestionnaire = new InlineKeyboardButton().setText("Заполнить анкету");
        InlineKeyboardButton buttonInfo = new InlineKeyboardButton().setText("Рассказать о себе");
        InlineKeyboardButton buttonFreeInfo = new InlineKeyboardButton().setText("Получить полезную информаю");
        InlineKeyboardButton buttonInfoPrice = new InlineKeyboardButton().setText("Узнать о тарифах и стоимости");

        //Every button must have callBackData, or else not work !
        buttonQuestionnaire.setCallbackData("buttonQuestionnaire");
        buttonInfo.setCallbackData("buttonInfo");
        buttonFreeInfo.setCallbackData("buttonFreeInfo");
        buttonInfoPrice.setCallbackData("buttonPrice");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonQuestionnaire);
        keyboardButtonsRow1.add(buttonInfo);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonFreeInfo);
        keyboardButtonsRow2.add(buttonInfoPrice);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }


}
